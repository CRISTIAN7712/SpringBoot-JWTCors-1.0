package com.example.jwtcors.controller;

import com.example.jwtcors.dto.AdminPanelResponse;
import com.example.jwtcors.entity.User;
import com.example.jwtcors.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminPanelResponse adminPanel(Authentication auth){
        String username = auth.getName();

        User user = userService.findByUsername(username);

        return new AdminPanelResponse(
                "Panel Administrativo",
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getRole().permissions
        );
    }
}
