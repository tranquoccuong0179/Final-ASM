package com.assignment.asm.delegate;

import com.assignment.asm.model.Order;
import com.assignment.asm.model.OrderDetail;
import com.assignment.asm.model.User;
import com.assignment.asm.repository.OrderRepository;
import com.assignment.asm.repository.UserRepository;
import com.assignment.asm.service.JavaMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Component
@Slf4j
@RequiredArgsConstructor
public class DelegateEmailOrderInformation implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(DelegateEmailOrderInformation.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final JavaMailService javaMailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws IOException {
        Long orderId = (Long) delegateExecution.getVariable("orderId");

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            log.error("Order not found with id: {}", orderId);
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        User user = userRepository.findById(order.getUser().getId()).orElse(null);
        if (user == null) {
            log.error("User not found with id: {}", order.getUser().getId());
            throw new RuntimeException("User not found with id: " + order.getUser().getId());
        }

        String emailBody = loadEmailTemplate(order, user);

        try {
            javaMailService.sendEmail(user.getEmail(), "Thông báo đơn hàng", emailBody);
            log.info("Email sent to: {} for order id: {}", user.getEmail(), orderId);
        } catch (Exception e) {
            log.error("Failed to send email to: {} for order id: {}", user.getEmail(), orderId, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String loadEmailTemplate(Order order, User user) {
        try {
            Path path = new ClassPathResource("static/order-details.html").getFile().toPath();
            String emailBody = Files.readString(path, StandardCharsets.UTF_8);

            emailBody = emailBody.replace("{{username}}", user.getUsername());

            StringBuilder orderDetailsHtml = new StringBuilder();
            double totalAmount = 0;

            for (OrderDetail item : order.getDetails()) {
                double itemTotal = item.getPrice();
                totalAmount += itemTotal;

                orderDetailsHtml.append("<tr>")
                        .append("<td>").append(item.getProduct().getName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>").append(String.format("%,.2f VND", itemTotal)).append("</td>")
                        .append("</tr>");
            }

            emailBody = emailBody.replace("{{orderDetails}}", orderDetailsHtml.toString());
            emailBody = emailBody.replace("{{totalAmount}}", String.format("%,.2f VND", totalAmount));

            return emailBody;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read email template", e);
        }
    }


}
