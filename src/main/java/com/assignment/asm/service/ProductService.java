package com.assignment.asm.service;

import com.assignment.asm.dto.request.product.CreateProductRequest;
import com.assignment.asm.dto.request.product.UpdateProductRequest;
import com.assignment.asm.dto.response.product.CreateProductResponse;
import com.assignment.asm.dto.response.product.GetProductResponse;
import com.assignment.asm.dto.response.product.UpdateProductResponse;
import com.assignment.asm.mapper.ProductMapper;
import com.assignment.asm.model.Product;
import com.assignment.asm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toModel(request);
        product.setActive(true);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public GetProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.fromProductToResponse(product);
    }

    @Override
    public List<GetProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAllActive();
        return products.stream().map(productMapper::fromProductToResponse).toList();
    }

    @Override
    public boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
        return true;
    }

    @Override
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(request.getName() != null ? request.getName() : existingProduct.getName());
        existingProduct.setPrice(request.getPrice() != null ? request.getPrice() : existingProduct.getPrice());
        existingProduct.setDescription(request.getDescription() != null ? request.getDescription() : existingProduct.getDescription());
        existingProduct.setQuantity(request.getQuantity() != 0 ? request.getQuantity() : existingProduct.getQuantity());
        productRepository.save(existingProduct);
        return productMapper.toUpdateResponse(existingProduct);
    }
}
