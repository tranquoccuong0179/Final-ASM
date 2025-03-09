package com.assignment.asm.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestParam {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;
}
