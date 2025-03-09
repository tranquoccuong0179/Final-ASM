package com.assignment.asm.repository;

import com.assignment.asm.dto.keycloak.*;
import com.assignment.asm.dto.request.UpdateUserRequest;
import com.assignment.asm.dto.response.LoginResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface KeyCloakRepository {
    @PostMapping(value = "/realms/Provider-User/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/realms/Provider-User/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse exchangeToken(@QueryMap LoginRequestParam param);

    @PostMapping(value = "/admin/realms/Provider-User/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)

    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationParam param);

    @DeleteMapping(value = "/admin/realms/Provider-User/users/{userId}")
    ResponseEntity<?> deleteUser(
            @RequestHeader("authorization") String token,
            @PathVariable("userId") String userId);

    @PutMapping(value = "/admin/realms/Provider-User/users/{userId}")
    ResponseEntity<?> updateUser(
            @RequestHeader("authorization") String token,
            @PathVariable("userId") String userId,
            @RequestBody UpdateRequestParam request);

    @PutMapping(value = "/admin/realms/Provider-User/users/{userId}/reset-password")
    ResponseEntity<?> changePassword(
            @RequestHeader("authorization") String token,
            @PathVariable("userId") String userId,
            @RequestBody Credential request);

}
