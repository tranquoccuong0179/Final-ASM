package com.assignment.asm.controller;

import com.assignment.asm.dto.ApiResponse;
import com.assignment.asm.dto.request.order.CreateOrderRequest;
import com.assignment.asm.dto.request.order.UpdateOrderRequest;
import com.assignment.asm.dto.response.order.CreateOrderResponse;
import com.assignment.asm.dto.response.order.GetOrderResponse;
import com.assignment.asm.dto.response.order.UpdateOrderResponse;
import com.assignment.asm.service.IOrderService;
import com.assignment.asm.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {
    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {

        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Create Success", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateOrderResponse>> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest request) {

        UpdateOrderResponse response = orderService.updateOrder(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Create Success", response));
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<ApiResponse<GetOrderResponse>> ReceiveOrder(@PathVariable Long id) {

        GetOrderResponse response = orderService.receiveOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Create Success", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetOrderResponse>>> getOrdersForUser() {

        List<GetOrderResponse> response = orderService.getOrdersForUser();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Create Success", response));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<GetOrderResponse>>> getOrdersForAdmin() {

        List<GetOrderResponse> response = orderService.getOrdersForAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Create Success", response));
    }

}
