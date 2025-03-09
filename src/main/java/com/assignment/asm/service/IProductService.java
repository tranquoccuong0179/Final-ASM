package com.assignment.asm.service;

import com.assignment.asm.dto.request.product.CreateProductRequest;
import com.assignment.asm.dto.request.product.UpdateProductRequest;
import com.assignment.asm.dto.response.product.CreateProductResponse;
import com.assignment.asm.dto.response.product.GetProductResponse;
import com.assignment.asm.dto.response.product.UpdateProductResponse;

import java.util.List;

public interface IProductService {
    public CreateProductResponse createProduct(CreateProductRequest request);
    public GetProductResponse getProduct(Long id);
    public List<GetProductResponse> getAllProducts();
    public boolean deleteProduct(Long id);
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request);
}
