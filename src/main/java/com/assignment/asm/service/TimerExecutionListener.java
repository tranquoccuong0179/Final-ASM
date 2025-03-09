package com.assignment.asm.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class TimerExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String orderId = (String) execution.getVariable("orderId");
        if (orderId == null) {
            orderId = (String) execution.getVariableLocal("orderId");
        }
        execution.setVariable("orderId", orderId);
    }
}

