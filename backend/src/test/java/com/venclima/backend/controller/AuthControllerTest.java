package com.venclima.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venclima.config.SecurityConfig;
import com.venclima.controller.AuthController;
import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.model.User;
import com.venclima.service.AuthService;
import com.venclima.service.JWTService;
import com.venclima.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    private String signUpUri;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    private RegisterUserDTO validRegistration;
    private UserDTO createdUserDTO;
    private User persistedUser;

    @BeforeEach
    void setUp() {

        this.signUpUri = "/api/auth/signup";

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        validRegistration = new RegisterUserDTO("test.user@example.com", "Test User", "Test User", "password123", null);
        createdUserDTO = new UserDTO("test.user@example.com", "Test User", "Test User");
        persistedUser = new User();
        persistedUser.setEmail("test.user@example.com");
        persistedUser.setPassword("password123");
        persistedUser.setName("Test User");
        persistedUser.setSurname("Test User");

        when(jwtService.getExpirationTime()).thenReturn(3600000L);
        when(refreshTokenService.refreshTokenServiceDurationMs()).thenReturn(2592000000L);
    }


    @Test
    void userRegistrationSuccessful () throws Exception {

        when(authService.registerUser(any(RegisterUserDTO.class))).thenReturn(persistedUser);

        String expectedJwt = "mock_jwt_token";
        String expectedRefresh = "mock_refresh_token";
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedJwt);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(expectedRefresh);

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.token").value(expectedJwt))
                .andExpect(jsonPath("$.expiresIn").value(3600000L))
                .andExpect(jsonPath("$.refreshToken").value(expectedRefresh))
                .andExpect(jsonPath("$.refreshExpiresIn").value(2592000000L));
    }

    @Test
    void registerUserFailureUserAlreadyExists() throws Exception {
        String expectedErrorMessage = "Email already in use";

        doThrow(new IllegalArgumentException(expectedErrorMessage)).when(authService)
                .registerUser(any());

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void registerUserFailureGenericErrorWithNullMessage() throws Exception {
        String expectedErrorMessage = "Errore nella registrazione";

        doThrow(new RuntimeException()).when(authService)
                .registerUser(any());

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));
    }
}