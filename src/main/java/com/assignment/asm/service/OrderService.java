package com.assignment.asm.service;

import com.assignment.asm.dto.request.order.CreateOrderRequest;
import com.assignment.asm.dto.request.order.UpdateOrderRequest;
import com.assignment.asm.dto.request.orderDetail.CreateOrderDetailRequest;
import com.assignment.asm.dto.response.order.CreateOrderResponse;
import com.assignment.asm.dto.response.order.GetOrderResponse;
import com.assignment.asm.dto.response.order.UpdateOrderResponse;
import com.assignment.asm.dto.response.orderDetail.CreateOrderDetailResponse;
import com.assignment.asm.enums.OrderEnum;
import com.assignment.asm.mapper.OrderDetailMapper;
import com.assignment.asm.model.Order;
import com.assignment.asm.model.OrderDetail;
import com.assignment.asm.model.Product;
import com.assignment.asm.model.User;
import com.assignment.asm.repository.OrderDetailRepository;
import com.assignment.asm.repository.OrderRepository;
import com.assignment.asm.repository.ProductRepository;
import com.assignment.asm.repository.UserRepository;
import com.assignment.asm.utils.AuthenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static org.camunda.bpm.admin.impl.plugin.resources.MetricsRestService.objectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final UserRepository userRepository;
    private final RuntimeService runtimeService;
    private final TaskService taskService;


    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_order", UUID.randomUUID().toString());
        Long userId = AuthenUtil.getUserId();
        User user = userRepository.findById(userId).orElse(null);

        double totalPrice = 0;
        int totalProduct = 0;
        boolean orderIsValid = true;
        List<CreateOrderDetailResponse> createOrderDetailResponses = new ArrayList<>();

        Order order = new Order();
        order.setStatus(OrderEnum.PENDING.toString());
        orderRepository.save(order);

        for (CreateOrderDetailRequest detailRequest : request.getDetails()) {
            Product product = productRepository.findById(detailRequest.getProduct_id()).orElse(null);
            if (product == null) {
                continue;
            }

            if (detailRequest.getQuantity() > product.getQuantity()) {
                orderIsValid = false;
            }

            OrderDetail orderDetail = orderDetailMapper.toModel(detailRequest);
            double itemPrice = product.getPrice() * detailRequest.getQuantity();
            orderDetail.setPrice(itemPrice);
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetailRepository.save(orderDetail);

            totalPrice += itemPrice;
            totalProduct += detailRequest.getQuantity();

            createOrderDetailResponses.add(orderDetailMapper.toResponse(orderDetail));
        }

        order.setTotalPrice(totalPrice);
        order.setTotalProduct(totalProduct);
        order.setBusinessKey(processInstance.getBusinessKey());
        order.setUser(user);
        orderRepository.save(order);

        CreateOrderResponse createOrderResponse = new CreateOrderResponse();
        createOrderResponse.setTotalPrice(totalPrice);
        createOrderResponse.setTotalProduct(totalProduct);
        createOrderResponse.setStatus(order.getStatus());
        createOrderResponse.setCreateOrderDetailResponses(createOrderDetailResponses);

        String businessKey = processInstance.getBusinessKey();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("Activity_Create_Order")
                .singleResult();

        if (task != null) {
            Map<String, Object> variables = new HashMap<>();
            try {
                String jsonResponse = objectMapper.writeValueAsString(createOrderResponse);
                variables.put("orderResponse", jsonResponse);
                variables.put("orderIsValid", orderIsValid);

                taskService.complete(task.getId(), variables);
            } catch (Exception e) {
                log.error("Error processing order task: ", e);
            }
        } else {
            log.warn("Do not find task với businessKey: {}", businessKey);
        }

        return createOrderResponse;
    }

    @Override
    public UpdateOrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        double totalPrice = 0;
        int totalProduct = 0;
        boolean orderIsValid = true;
        boolean hasUpdated = false;
        List<CreateOrderDetailResponse> updateOrderDetailResponses = new ArrayList<>();

        for (CreateOrderDetailRequest detailRequest : request.getDetails()) {
            OrderDetail orderDetail = orderDetailRepository.findByOrderAndProductId(order, detailRequest.getProduct_id())
                    .orElse(null);
            if (orderDetail == null) {
                continue;
            }

            Product product = productRepository.findById(detailRequest.getProduct_id()).orElse(null);
            if (product == null) {
                continue;
            }

            if (detailRequest.getQuantity() > product.getQuantity()) {
                orderIsValid = false;
            }

            if (orderDetail.getQuantity() != detailRequest.getQuantity()) {
                hasUpdated = true;
            }

            double itemPrice = product.getPrice() * detailRequest.getQuantity();
            orderDetail.setQuantity(detailRequest.getQuantity());
            orderDetail.setPrice(itemPrice);
            orderDetailRepository.save(orderDetail);

            totalPrice += itemPrice;
            totalProduct += detailRequest.getQuantity();

            updateOrderDetailResponses.add(orderDetailMapper.toResponse(orderDetail));
        }
        if(hasUpdated){
            order.setTotalPrice(totalPrice);
            order.setTotalProduct(totalProduct);
            orderRepository.save(order);
        }

        UpdateOrderResponse updateOrderResponse = new UpdateOrderResponse();
        updateOrderResponse.setTotalPrice(totalPrice);
        updateOrderResponse.setTotalProduct(totalProduct);
        updateOrderResponse.setStatus(order.getStatus());
        updateOrderResponse.setCreateOrderDetailResponses(updateOrderDetailResponses);

        if (hasUpdated) {
            String businessKey = order.getBusinessKey();
            Task task = taskService.createTaskQuery()
                    .processInstanceBusinessKey(businessKey)
                    .taskDefinitionKey("Activity_Request_Update")
                    .singleResult();

            if (task != null) {
                Map<String, Object> variables = new HashMap<>();
                try {
                    variables.put("orderIsValid", orderIsValid);
                    variables.put("customerHasCorrected", hasUpdated);

                    taskService.complete(task.getId(), variables);
                } catch (Exception e) {
                    log.error("Error processing update order task: ", e);
                }
            } else {
                log.warn("Do not find task with businessKey: {}", businessKey);
            }
        }

        return updateOrderResponse;
    }

    @Override
    public GetOrderResponse receiveOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }
        order.setStatus(OrderEnum.ACCEPTED.toString());
        orderRepository.save(order);
        GetOrderResponse getOrderResponse = new GetOrderResponse();
        getOrderResponse.setTotalPrice(order.getTotalPrice());
        getOrderResponse.setTotalProduct(order.getTotalProduct());
        getOrderResponse.setStatus(order.getStatus());
        getOrderResponse.setCreateOrderDetailResponses(order.getDetails().stream().map(orderDetailMapper::toResponse).collect(Collectors.toList()));
        String businessKey = order.getBusinessKey();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("Activity_Receive_Order")
                .singleResult();
        if (task != null) {
            Map<String, Object> variables = new HashMap<>();
            try {
                variables.put("orderId", order.getId());
                taskService.complete(task.getId(), variables);
            } catch (Exception e) {
                log.error("Error processing update order task: ", e);
            }
        } else {
            log.warn("Do not find task with businessKey: {}", businessKey);
        }
        return getOrderResponse;
    }

    @Override
    public List<GetOrderResponse> getOrdersForUser() {
        Long userId = AuthenUtil.getUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Optional<List<Order>> orders = orderRepository.findAllOrder(userId);
        List<GetOrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders.get()) {
            GetOrderResponse getOrderResponse = new GetOrderResponse();
            getOrderResponse.setTotalPrice(order.getTotalPrice());
            getOrderResponse.setTotalProduct(order.getTotalProduct());
            getOrderResponse.setStatus(order.getStatus());
            orderResponses.add(getOrderResponse);
        }

        return orderResponses;
    }

    @Override
    public List<GetOrderResponse> getOrdersForAdmin() {
        Optional<List<Order>> orders = orderRepository.findAllOrder();
        List<GetOrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders.get()) {
            GetOrderResponse getOrderResponse = new GetOrderResponse();
            getOrderResponse.setId(order.getId());
            getOrderResponse.setTotalPrice(order.getTotalPrice());
            getOrderResponse.setTotalProduct(order.getTotalProduct());
            getOrderResponse.setStatus(order.getStatus());
            orderResponses.add(getOrderResponse);
        }

        return orderResponses;
    }

}
