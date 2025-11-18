package com.venclima.controller;

import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserDTO user) {
        UserDTO userDTO = authService.registerUser(user);
        return ResponseEntity.ok(userDTO);
    }

}
