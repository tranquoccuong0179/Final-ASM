package com.assignment.asm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_EXISTED(1008, "Username existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1009, "Email existed", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1009, "Username is empty", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1010, "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1011, "Invalid credentials", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_PASSWORD(1012, "Invalid username or password", HttpStatus.BAD_REQUEST),
    RUNTIME_EXCEPTION(1013, "Runtime exception", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
