package com.assignment.asm.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IVNPayService {
    public String payWithVNPAYOnline(HttpServletRequest request, String businessKey) throws UnsupportedEncodingException;
    public boolean processPaymentCallback(Map<String, String> queryParams, HttpServletResponse response) throws IOException;
}
