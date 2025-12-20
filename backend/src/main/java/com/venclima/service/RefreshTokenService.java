package com.venclima.service;

import com.venclima.model.RefreshToken;
import com.venclima.model.User;
import com.venclima.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${security.refresh-token.expiration-ms:2592000000}") // default 30 days
    private long refreshTokenExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    //create refresh token
    public String createRefreshToken(User user) {
        String raw = generateRandomToken();
        String hash = hashToken(raw);
        Date expiresAt = new Date(System.currentTimeMillis() + refreshTokenExpirationMs);

        RefreshToken entity = new RefreshToken(hash, expiresAt, user);
        refreshTokenRepository.save(entity);
        return raw;
    }

    //check refresh token's validity
    public Optional<RefreshToken> findValidByRaw(String rawToken) {
        try {
            String hash = hashToken(rawToken);
            Optional<RefreshToken> opt = refreshTokenRepository.findByTokenHash(hash);
            if (opt.isEmpty()) return Optional.empty();
            RefreshToken rt = opt.get();
            if (rt.isRevoked()) return Optional.empty();
            if (rt.getExpiresAt() == null || rt.getExpiresAt().before(new Date())) return Optional.empty();
            return Optional.of(rt);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    private String generateRandomToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Unable to hash token", e);
        }
    }

}
