package com.assignment.asm.mapper;

import com.assignment.asm.dto.request.product.CreateProductRequest;
import com.assignment.asm.dto.request.product.UpdateProductRequest;
import com.assignment.asm.dto.response.product.CreateProductResponse;
import com.assignment.asm.dto.response.product.GetProductResponse;
import com.assignment.asm.dto.response.product.UpdateProductResponse;
import com.assignment.asm.model.Product;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductMapper {
    ModelMapper modelMapper;

    public Product toModel(CreateProductRequest product) {
        return modelMapper.map(product, Product.class);
    }

    public CreateProductResponse toResponse(Product product) {
        return modelMapper.map(product, CreateProductResponse.class);
    }

    public GetProductResponse fromProductToResponse(Product product){
        return modelMapper.map(product, GetProductResponse.class);
    }

    public UpdateProductResponse toUpdateResponse(Product product) {
        return modelMapper.map(product, UpdateProductResponse.class);
    }
}
