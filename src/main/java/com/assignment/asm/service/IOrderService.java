package com.assignment.asm.service;

import com.assignment.asm.dto.request.order.CreateOrderRequest;
import com.assignment.asm.dto.request.order.UpdateOrderRequest;
import com.assignment.asm.dto.response.order.CreateOrderResponse;
import com.assignment.asm.dto.response.order.GetOrderResponse;
import com.assignment.asm.dto.response.order.UpdateOrderResponse;

import java.util.List;

public interface IOrderService {
    public CreateOrderResponse createOrder(CreateOrderRequest request);
    public UpdateOrderResponse updateOrder(Long id, UpdateOrderRequest request);
    public GetOrderResponse receiveOrder(Long id);
    public List<GetOrderResponse> getOrdersForUser();
    public List<GetOrderResponse> getOrdersForAdmin();
}
