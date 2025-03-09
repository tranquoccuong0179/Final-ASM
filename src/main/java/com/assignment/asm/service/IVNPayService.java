package com.assignment.asm.service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IVNPayService {
    public String payWithVNPAYOnline(HttpServletRequest request, String businessKey) throws UnsupportedEncodingException;
}
