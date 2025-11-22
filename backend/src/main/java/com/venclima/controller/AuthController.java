package com.venclima.controller;

import com.venclima.dto.LoginUserDTO;
import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.model.User;
import com.venclima.service.AuthService;
import com.venclima.service.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import responses.LoginResponse;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;

    public AuthController(AuthService authService, JWTService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserDTO user) {
        UserDTO userDTO = authService.registerUser(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

}
