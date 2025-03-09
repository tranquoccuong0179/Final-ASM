package com.assignment.asm.mapper;

import com.assignment.asm.dto.request.RegistrationRequest;
import com.assignment.asm.dto.request.UpdateUserRequest;
import com.assignment.asm.dto.response.UserResponse;
import com.assignment.asm.dto.response.UpdateUserResponse;
import com.assignment.asm.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegistrationRequest request);
    UserResponse toUserReponse(User user);
//    User toUpdateProfile(UpdateUserRequest request);
    UpdateUserResponse toUpdateUserResponse(User user);
}
