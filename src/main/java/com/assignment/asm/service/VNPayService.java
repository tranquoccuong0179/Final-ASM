package com.assignment.asm.service;

import com.assignment.asm.config.VNPayConfig;
import com.assignment.asm.model.Order;
import com.assignment.asm.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService implements IVNPayService {
    private final OrderRepository orderRepository;
    public String payWithVNPAYOnline(HttpServletRequest request, String businessKey) throws UnsupportedEncodingException {

        Optional<Order> order = orderRepository.findByBusinessKey(businessKey);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        int totalPrice = (int) Math.round(order.get().getTotalPrice() * 100);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 10);

        String vnp_ExpireDate = formatter.format(cld.getTime());
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalPrice));
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", VNPayConfig.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_Locale", VNPayConfig.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", "Nạp tiền");
        vnp_Params.put("vnp_OrderType", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", "HD" + RandomStringUtils.randomNumeric(6) + "-" + vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldList.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }
}
