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

    /**
     * Registers a new user in the system.
     * <p>
     * Validates email uniqueness, persists the user entity,
     * and optionally stores the provided device/FCM token.
     *
     * @param registerUserDTO DTO containing user registration data
     * @return the persisted {@link User} entity
     * @throws RuntimeException if registration fails or email is already in use
     */
    public User registerUser(RegisterUserDTO registerUserDTO) {
        try {
            if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
                throw new IllegalArgumentException("Email già utilizzata");
            }
            User user = userMapper.toEntity(registerUserDTO);
            userRepository.save(user);
            saveToken(registerUserDTO.getFcmToken(), user);

            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Authenticates a user using email and password credentials.
     * <p>
     * Delegates authentication to Spring Security and persists
     * the provided device/FCM token if present.
     *
     * @param input DTO containing login credentials
     * @return the authenticated {@link User}
     * @throws IllegalArgumentException if the user does not exist
     * @throws org.springframework.security.core.AuthenticationException
     *         if authentication fails
     */
    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        saveToken(input.getFcmToken(), user);

        return user;
    }

    /**
     * Persists a device or FCM token associated with a user if it does not already exist.
     *
     * @param token the token to persist
     * @param user the user associated with the token
     */
    private void saveToken(String token, User user) {
        if (token == null || token.isBlank()) {
            return;
        }
        if(tokenRepository.findByToken(token).isEmpty())
            tokenRepository.save(new Token(token, user));
    }

    /**
     * Retrieves a {@link UserDTO} by email address.
     *
     * @param email the user's email
     * @return the corresponding {@link UserDTO}
     * @throws RuntimeException if the user is not found
     */
    public UserDTO getUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }


    /**
     * Retrieves a {@link User} entity by email.
     *
     * @param email the user's email
     * @return the corresponding {@link User}
     * @throws RuntimeException if the user is not found
     */
    public User getUserForToken(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Returns the email of the currently authenticated user
     * from the Spring Security context.
     *
     * @return authenticated user's email
     */
    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Retrieves the currently authenticated {@link User}.
     *
     * @return the authenticated user entity
     * @throws java.util.NoSuchElementException if the user is not found
     */
    public User getAuthenticatedUser() {
        String userEmail = this.getUserEmail();
        return userRepository.findByEmail(userEmail).orElseThrow();
    }

}
