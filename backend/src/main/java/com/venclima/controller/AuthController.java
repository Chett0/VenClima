package com.venclima.controller;

import com.venclima.dto.LoginUserDTO;
import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.model.User;
import com.venclima.responses.LoginResponse;
import com.venclima.service.AuthService;
import com.venclima.service.RefreshTokenService;
import com.venclima.service.JWTService;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", "Not authenticated"));
        }

        String email = authentication.getName();
        try {
            UserDTO dto = authService.getUserDTOByEmail(email);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("message", "User not found"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO user) {
        try {
            User persistedUser = authService.registerUser(user);
            // create refresh token and jwt
            String jwtToken = jwtService.generateToken(persistedUser);
            String refreshRaw = refreshTokenService.createRefreshToken(persistedUser);
            LoginResponse resp = new LoginResponse(
                    jwtToken,
                    jwtService.getExpirationTime(),
                    refreshRaw,
                    refreshTokenService.refreshTokenServiceDurationMs()
            );
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Errore nella registrazione";
            return ResponseEntity.badRequest().body(msg);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        try {
            User authenticatedUser = authService.authenticate(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            String refreshRaw = refreshTokenService.createRefreshToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime(), refreshRaw, refreshTokenService.refreshTokenServiceDurationMs());

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Autenticazione fallita";
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", msg));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody java.util.Map<String, String> body) {
        String refreshRaw = body.get("refreshToken");
        if (refreshRaw == null || refreshRaw.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "refreshToken missing"));
        }

        var opt = refreshTokenService.findValidByRaw(refreshRaw);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired refresh token"));
        }

        var stored = opt.get();
        // revoke old one and create another one to preserve session
        refreshTokenService.revoke(stored);
        String newRefreshRaw = refreshTokenService.createRefreshToken(stored.getUser());

        String newJwt = jwtService.generateToken(stored.getUser());
        LoginResponse resp = new LoginResponse(newJwt, jwtService.getExpirationTime(), newRefreshRaw, refreshTokenService.refreshTokenServiceDurationMs());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody java.util.Map<String, String> body) {
        String refreshRaw = body.get("refreshToken");
        if (refreshRaw == null || refreshRaw.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "refreshToken missing"));
        }
        var opt = refreshTokenService.findValidByRaw(refreshRaw);
        opt.ifPresent(refreshTokenService::revoke);
        return ResponseEntity.ok(Map.of("message", "logged out"));
    }
}
