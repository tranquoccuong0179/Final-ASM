package com.assignment.asm.delegate;

import com.assignment.asm.model.Order;
import com.assignment.asm.model.OrderDetail;
import com.assignment.asm.model.Product;
import com.assignment.asm.repository.OrderRepository;
import com.assignment.asm.repository.ProductRepository;
import com.assignment.asm.service.IOrderService;
import com.assignment.asm.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelegateAutoProcessOrder implements JavaDelegate {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long orderId = (Long) delegateExecution.getVariable("orderId");
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        List<OrderDetail> orderDetails = order.getDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
            productRepository.save(product);
        }

        delegateExecution.setVariable("orderId", orderId);
    }
}
