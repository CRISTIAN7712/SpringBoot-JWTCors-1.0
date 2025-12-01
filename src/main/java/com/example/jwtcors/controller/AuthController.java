package com.example.jwtcors.controller;

import com.example.jwtcors.dto.LoginRequest;
import com.example.jwtcors.dto.RegisterRequest;
import com.example.jwtcors.entity.Role;
import com.example.jwtcors.entity.User;
import com.example.jwtcors.service.JwtUtil;
import com.example.jwtcors.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request){
        return userService.register(request.getUsername(), request.getPassword(), request.getRole());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        User u = userService.findByUsername(request.getUsername());

        if(u == null || !userService.checkPassword(request.getPassword(), u.getPassword())){
            throw  new RuntimeException("Credenciales invalidas");
        }

        return jwtUtil.generateToken(u.getUsername(), u.getRole());
    }
}
