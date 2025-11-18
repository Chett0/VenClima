package com.venclima.service;

import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.mapper.UserMapper;
import com.venclima.model.User;
import com.venclima.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        User user = userMapper.toEntity(registerUserDTO);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

}
