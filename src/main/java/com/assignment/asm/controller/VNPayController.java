package com.assignment.asm.controller;

import com.assignment.asm.service.IVNPayService;
import com.assignment.asm.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Slf4j
public class VNPayController {
    private final IVNPayService VNPayService;
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> rechargeWallet(HttpServletRequest request, @RequestParam String businessKey) throws UnsupportedEncodingException {
        String paymentUrl = VNPayService.payWithVNPAYOnline(request, businessKey);
        return ResponseEntity.ok(paymentUrl);
    }
}
