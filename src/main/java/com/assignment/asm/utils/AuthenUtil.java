package com.assignment.asm.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenUtil {
    public static Long getUserId(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        String id = name.split(":")[2];
        return Long.valueOf(id);
    }
}
