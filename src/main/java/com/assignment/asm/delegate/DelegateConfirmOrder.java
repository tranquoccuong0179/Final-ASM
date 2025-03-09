package com.assignment.asm.delegate;

import com.assignment.asm.enums.OrderEnum;
import com.assignment.asm.model.Order;
import com.assignment.asm.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelegateConfirmOrder implements JavaDelegate {
    private final OrderRepository orderRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long orderId = (Long) delegateExecution.getVariable("orderId");
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        order.setStatus(OrderEnum.APPROVED.toString());
        orderRepository.save(order);
        delegateExecution.setVariable("orderId",orderId);

    }
}
