package com.assignment.asm.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequestParam {
    private String email;
    private String firstName;
    private String lastName;
}
