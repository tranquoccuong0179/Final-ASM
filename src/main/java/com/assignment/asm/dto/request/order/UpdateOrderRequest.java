package com.assignment.asm.dto.request.order;

import com.assignment.asm.dto.request.orderDetail.CreateOrderDetailRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequest {
    List<CreateOrderDetailRequest> details;
}
