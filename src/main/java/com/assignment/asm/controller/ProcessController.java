package com.assignment.asm.controller;

import com.assignment.asm.service.IProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ProcessController {
    private final IProcessService processService;

    @PostMapping("/start")
    public String start() {
        return processService.startProcess();
    }
}
