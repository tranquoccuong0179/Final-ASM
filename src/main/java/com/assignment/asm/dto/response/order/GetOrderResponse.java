package com.assignment.asm.dto.response.order;

import com.assignment.asm.dto.response.orderDetail.CreateOrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Double totalPrice;

    private int totalProduct;

    private String status;

    private List<CreateOrderDetailResponse> createOrderDetailResponses;
}
