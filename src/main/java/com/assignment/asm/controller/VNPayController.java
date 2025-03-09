package com.assignment.asm.controller;

import com.assignment.asm.service.IVNPayService;
import com.assignment.asm.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Slf4j
public class VNPayController {
    private final IVNPayService vnPayService;
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> Create(HttpServletRequest request, @RequestParam String businessKey) throws UnsupportedEncodingException {
        String paymentUrl = vnPayService.payWithVNPAYOnline(request, businessKey);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/payment-callback")
    public ResponseEntity<Boolean> paymentCallback(
            @RequestParam Map<String, String> queryParams,
            HttpServletResponse response) throws IOException {
        boolean paymentStatus = vnPayService.processPaymentCallback(queryParams, response);
        return ResponseEntity.ok(paymentStatus);
    }
}
