package com.assignment.asm.dto.request.orderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetailRequest {
    private int quantity;
    private Long product_id;
}
