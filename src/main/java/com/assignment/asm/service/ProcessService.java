package com.assignment.asm.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessService implements IProcessService {
    private final RuntimeService runtimeService;
    @Override
    public String startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_order", UUID.randomUUID().toString());
        return processInstance.getBusinessKey();
    }
}
