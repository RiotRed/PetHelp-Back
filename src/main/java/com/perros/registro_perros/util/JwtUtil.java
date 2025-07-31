package com.perros.registro_perros.util;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    
    private static final String TOKEN_PREFIX = "fake-jwt-token-";
    
    public String generateToken(Long userId) {
        return TOKEN_PREFIX + userId;
    }
    
    public Long extractUserId(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("Token inválido");
        }
        String idStr = token.replace(TOKEN_PREFIX, "").trim();
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Token inválido");
        }
    }
    
    public boolean validateToken(String token) {
        try {
            extractUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Header de autorización inválido");
        }
        return authHeader.substring(7);
    }
} 