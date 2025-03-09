package com.assignment.asm.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int quantity;
}
