package com.assignment.asm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private LocalDate dob;
}
