package com.venclima.repository;

import com.venclima.model.Token;
import com.venclima.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByUser(User user);
}
