package com.venclima.service;

import com.venclima.dto.LoginUserDTO;
import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.mapper.UserMapper;
import com.venclima.model.Token;
import com.venclima.model.User;
import com.venclima.repository.TokenRepository;
import com.venclima.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthService(
            UserRepository userRepository,
            UserMapper userMapper,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        if(userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = userMapper.toEntity(registerUserDTO);
        userRepository.save(user);
        saveToken(registerUserDTO.getFcmToken(), user);

        return userMapper.toDTO(user);
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();
        saveToken(input.getFcmToken(), user);

        return user;
    }

    private void saveToken(String token, User user) {
        if (token == null || token.isBlank()) {
            return;
        }
        tokenRepository.save(new Token(token, user));
    }

    public UserDTO getUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }


    public User getUserForToken(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getAuthenticatedUser() {
        String userEmail = this.getUserEmail();
        return userRepository.findByEmail(userEmail).orElseThrow();
    }

}
