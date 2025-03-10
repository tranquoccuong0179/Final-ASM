package com.assignment.asm.service;

import com.assignment.asm.config.VNPayConfig;
import com.assignment.asm.model.Order;
import com.assignment.asm.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.camunda.bpm.admin.impl.plugin.resources.MetricsRestService.objectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService implements IVNPayService {
    private final OrderRepository orderRepository;
    private final TaskService taskService;
    public String payWithVNPAYOnline(HttpServletRequest request, String businessKey) throws UnsupportedEncodingException {

        Order order = orderRepository.findByBusinessKey(businessKey).get();
    //        Task task = taskService.createTaskQuery()
    //                .processVariableValueEquals("businessKey", businessKey)
    ////                .processInstanceBusinessKey(businessKey)
    //                .singleResult();
    //
    //
    //        if (task != null) {
    //            Map<String, Object> variables = new HashMap<>();
    //            try {
    //                String jsonResponse = objectMapper.writeValueAsString(order);
    //                variables.put("orderResponse", jsonResponse);
    //
    //                taskService.complete(task.getId(), variables);
    //            } catch (Exception e) {
    //                log.error("Error processing order task: ", e);
    //            }
    //        } else {
    //            log.warn("Do not find task với businessKey: {}", businessKey);
    //        }
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        long totalPrice = Math.round(order.getTotalPrice() * 100);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 10);

        String vnp_ExpireDate = formatter.format(cld.getTime());
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalPrice));
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", VNPayConfig.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_Locale", VNPayConfig.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", String.valueOf(order.getId()));
        vnp_Params.put("vnp_OrderType", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", "HD" + RandomStringUtils.randomNumeric(6) + "-" + vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldList.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    public boolean processPaymentCallback(Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        try {
            String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
            Long orderId = Long.parseLong(queryParams.get("vnp_OrderInfo"));
            long vnp_Amount = Long.parseLong(queryParams.get("vnp_Amount")) / 100;

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                log.error("Order not found for ID: {}", orderId);
                response.sendRedirect("https://yourfrontend.com/payment/failed/" + orderId);
                return false;
            }

            Order order = orderOpt.get();
            Task task = taskService.createTaskQuery()
                    .processInstanceBusinessKey(order.getBusinessKey())
                    .taskDefinitionKey("Activity_Order_Payment")
                    .singleResult();

            if (task != null) {
                Map<String, Object> variables = new HashMap<>();
                try {
                    String jsonResponse = objectMapper.writeValueAsString(order);
                    variables.put("orderResponse", jsonResponse);

                    if ("00".equals(vnp_ResponseCode)) {
                        variables.put("order", "success");
                        taskService.complete(task.getId(), variables);
                        response.sendRedirect("https://yourfrontend.com/payment/success/" + orderId);
                        return true;
                    } else {
                        variables.put("order", "failed");
                        taskService.complete(task.getId(), variables);
                        response.sendRedirect("https://yourfrontend.com/payment/failed/" + orderId);
                        return false;
                    }
                } catch (Exception e) {
                    log.error("Error processing order task: ", e);
                }
            } else {
                log.warn("Do not find task với businessKey: {}", order.getBusinessKey());
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback: ", e);
            response.sendRedirect("https://yourfrontend.com/payment/failed/");
        }

        return false;
    }

}
