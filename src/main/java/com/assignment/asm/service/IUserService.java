package com.assignment.asm.service;

import com.assignment.asm.dto.request.ChangePasswordRequest;
import com.assignment.asm.dto.request.LoginRequest;
import com.assignment.asm.dto.request.RegistrationRequest;
import com.assignment.asm.dto.request.UpdateUserRequest;
import com.assignment.asm.dto.response.LoginResponse;
import com.assignment.asm.dto.response.UserResponse;
import com.assignment.asm.dto.response.UpdateUserResponse;

import java.util.List;

public interface IUserService {
    public List<UserResponse> getAllProfiles();
    public UserResponse registration(RegistrationRequest request);
    public UserResponse getProfileById();
    public LoginResponse login(LoginRequest request);
    public boolean deleteProfile(Long id);
    public UpdateUserResponse updateUser(UpdateUserRequest request);
    public boolean changePassword(ChangePasswordRequest newPassword);
}
