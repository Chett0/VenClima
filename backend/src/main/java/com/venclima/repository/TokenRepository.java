package com.venclima.repository;

import com.venclima.model.Token;
import com.venclima.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByUser(User user);
    Optional<Token> findByToken(String token);
}
