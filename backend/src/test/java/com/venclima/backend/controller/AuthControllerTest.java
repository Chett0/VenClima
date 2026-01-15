package com.venclima.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venclima.config.SecurityConfig;
import com.venclima.controller.AuthController;
import com.venclima.dto.LoginUserDTO;
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
import org.springframework.security.authentication.BadCredentialsException;
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
    private String loginUri;

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
    private LoginUserDTO validLogin;
    private UserDTO createdUserDTO;
    private User persistedUser;

    private String expectedJwtToken;
    private String expectedRefreshToken;

    @BeforeEach
    void setUp() {

        String baseUri = "/api/auth";
        this.signUpUri = baseUri + "/signup";
        this.loginUri = baseUri + "/login";

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        validRegistration = new RegisterUserDTO("test.user@example.com", "Test User", "Test User", "password123", null);
        validLogin = new LoginUserDTO("test.user@example.com","password123",null);
        createdUserDTO = new UserDTO("test.user@example.com", "Test User", "Test User");
        persistedUser = new User();
        persistedUser.setEmail("test.user@example.com");
        persistedUser.setPassword("password123");
        persistedUser.setName("Test User");
        persistedUser.setSurname("Test User");

        expectedJwtToken = "mock_jwt_token";
        expectedRefreshToken = "mock_refresh_token";

        when(jwtService.getExpirationTime()).thenReturn(3600000L);
        when(refreshTokenService.refreshTokenServiceDurationMs()).thenReturn(2592000000L);
    }


    @Test
    void userRegistrationSuccessful () throws Exception {

        when(authService.registerUser(any(RegisterUserDTO.class))).thenReturn(persistedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(this.expectedJwtToken);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(this.expectedJwtToken);

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.token").value(this.expectedJwtToken))
                .andExpect(jsonPath("$.expiresIn").value(3600000L))
                .andExpect(jsonPath("$.refreshToken").value(this.expectedJwtToken))
                .andExpect(jsonPath("$.refreshExpiresIn").value(2592000000L));
    }

    @Test
    void registerUserFailureUserAlreadyExists() throws Exception {

        doThrow(new IllegalArgumentException()).when(authService)
                .registerUser(any());

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserFailureGenericError() throws Exception {
        doThrow(new RuntimeException()).when(authService)
                .registerUser(any());

        mockMvc.perform(post(this.signUpUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isBadRequest());
    }


    @Test
    void loginUserSuccessful() throws Exception {
        when(authService.authenticate(any(LoginUserDTO.class))).thenReturn(persistedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(this.expectedJwtToken);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(this.expectedRefreshToken);

        mockMvc.perform(post(this.loginUri)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLogin)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.token").value(this.expectedJwtToken))
                .andExpect(jsonPath("$.expiresIn").value(3600000L))
                .andExpect(jsonPath("$.refreshToken").value(this.expectedRefreshToken))
                .andExpect(jsonPath("$.refreshExpiresIn").value(2592000000L));
    }

    @Test
    void loginUserFailureBadCredentials() throws Exception {

        doThrow(new BadCredentialsException("")).when(authService)
                .authenticate(any());

        mockMvc.perform(post(this.loginUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLogin)))

                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUserFailureGenericError() throws Exception {
        doThrow(new RuntimeException()).when(authService)
                .authenticate(any());

        mockMvc.perform(post(this.loginUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistration)))

                .andExpect(status().isBadRequest());
    }

}