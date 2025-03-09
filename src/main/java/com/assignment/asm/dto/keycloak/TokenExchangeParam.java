package com.assignment.asm.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenExchangeParam {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
}
