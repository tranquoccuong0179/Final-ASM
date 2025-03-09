package com.assignment.asm.controller;

import com.assignment.asm.dto.ApiResponse;
import com.assignment.asm.dto.request.product.CreateProductRequest;
import com.assignment.asm.dto.request.product.UpdateProductRequest;
import com.assignment.asm.dto.response.product.CreateProductResponse;
import com.assignment.asm.dto.response.product.GetProductResponse;
import com.assignment.asm.dto.response.product.UpdateProductResponse;
import com.assignment.asm.service.IProductService;
import com.assignment.asm.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final IProductService productService;

    @PostMapping
    @Operation(summary = "API tạo sản phẩm mới")
    public ResponseEntity<ApiResponse<CreateProductResponse>> addProduct(@RequestBody CreateProductRequest request) {
        try {
            CreateProductResponse response = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(200, "Product created", response)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, "Create product failed", null));
        }
    }

    @GetMapping
    @Operation(summary = "API xem tất cả các sản phẩm có trong hệ thống")
    public ResponseEntity<ApiResponse<List<GetProductResponse>>> getProducts() {
        List<GetProductResponse> list = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Products List", list)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "API xem sản phẩm với id đó có trong hệ thống")
    public ResponseEntity<ApiResponse<GetProductResponse>> getProductById(@PathVariable Long id) {
        GetProductResponse product = productService.getProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Product", product)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "API xóa sản phẩm có trong hệ thống")
    public ResponseEntity<ApiResponse<Boolean>> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Delete successful", result)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "API cập nhật sản phẩm có trong hệ thống")
    public ResponseEntity<ApiResponse<UpdateProductResponse>> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        UpdateProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Update successful", response)
        );
    }

//    @PostMapping
//    public ResponseEntity<ApiResponse<CreateProductResponse>> addProduct(@RequestBody CreateProductRequest request) {
//        try {
//            CreateProductResponse response = productService.createProduct(request);
//            return ResponseEntity.ok(new ApiResponse<>(200, "Product created", response));
//            );
//        }catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, "Create product failed", null));
//        }
//    }
}
