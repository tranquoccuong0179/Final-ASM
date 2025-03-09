package com.assignment.asm.controller;

import com.assignment.asm.dto.ApiResponse;
import com.assignment.asm.dto.request.ChangePasswordRequest;
import com.assignment.asm.dto.request.LoginRequest;
import com.assignment.asm.dto.request.RegistrationRequest;
import com.assignment.asm.dto.request.UpdateUserRequest;
import com.assignment.asm.dto.response.LoginResponse;
import com.assignment.asm.dto.response.UserResponse;
import com.assignment.asm.dto.response.UpdateUserResponse;
import com.assignment.asm.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    IUserService IUserService;


    @PostMapping("/register")
    @Operation(summary = "API đăng kí tài khoản mới")
    ApiResponse<UserResponse> register(@RequestBody @Valid RegistrationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .data(IUserService.registration(request))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/profiles")
    @Operation(summary = "API xem tất cả người dùng trong hệ thống giành cho admin")
    ApiResponse<List<UserResponse>> getAllProfiles() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .data(IUserService.getAllProfiles())
                .build();
    }

    @GetMapping("/profile")
    @Operation(summary = "API xem profile của người dùng")
    ApiResponse<UserResponse> getUserProfiles() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .data(IUserService.getProfileById())
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "API đăng nhập tài khoản")
    ApiResponse<LoginResponse> Login(@RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .data(IUserService.login(request))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "API xóa tài khoản")
    ApiResponse<Boolean> Delete(@PathVariable Long id) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .data(IUserService.deleteProfile(id))
                .build();
    }

    @PutMapping("/update")
    @Operation(summary = "API thay đổi thông tin người dùng")
    ApiResponse<UpdateUserResponse> Delete(@RequestBody @Valid UpdateUserRequest request) {
        return ApiResponse.<UpdateUserResponse>builder()
                .code(200)
                .data(IUserService.updateUser(request))
                .build();
    }

    @PutMapping("/change-password")
    @Operation(summary = "API thay đổi mật khẩu người dùng")
    ApiResponse<Boolean> Delete(@RequestBody ChangePasswordRequest password) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .data(IUserService.changePassword(password))
                .build();
    }
}
