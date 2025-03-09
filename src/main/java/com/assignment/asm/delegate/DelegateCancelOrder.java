package com.assignment.asm.delegate;

import com.assignment.asm.enums.OrderEnum;
import com.assignment.asm.model.Order;
import com.assignment.asm.repository.OrderRepository;
import com.assignment.asm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelegateCancelOrder implements JavaDelegate {
    private final OrderRepository orderRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String businessKey = delegateExecution.getProcessBusinessKey();

        Order order = orderRepository.findByBusinessKey(businessKey).orElse(null);

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        order.setStatus(OrderEnum.CANCELLED.toString());
        orderRepository.save(order);
    }
}
