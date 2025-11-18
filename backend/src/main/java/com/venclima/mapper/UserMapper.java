package com.venclima.mapper;

import com.venclima.dto.RegisterUserDTO;
import com.venclima.dto.UserDTO;
import com.venclima.model.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

//    private final PasswordEncoder passwordEncoder;
//
//    public UserMapper(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    public User toEntity(RegisterUserDTO user) {
        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        //userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        return userEntity;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

}
