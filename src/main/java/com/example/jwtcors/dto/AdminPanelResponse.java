package com.example.jwtcors.dto;

public record AdminPanelResponse(String title,
                                 Long useId,
                                 String username,
                                 String role,
                                 Object permissions) {
}
