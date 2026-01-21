package com.venclima.service;

import com.venclima.model.RefreshToken;
import com.venclima.model.User;
import com.venclima.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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

    /**
     * Creates a new refresh token for a user.
     * <p>
     * Generates a secure random token, hashes it, stores it with an expiration date,
     * and returns the raw token to the client.
     *
     * @param user the user for whom the refresh token is created
     * @return the raw refresh token string
     */
    public String createRefreshToken(User user) {
        String raw = generateRandomToken();
        String hash = hashToken(raw);
        Date expiresAt = new Date(System.currentTimeMillis() + refreshTokenExpirationMs);

        RefreshToken entity = new RefreshToken(hash, expiresAt, user);
        refreshTokenRepository.save(entity);
        return raw;
    }

    /**
     * Finds a valid refresh token by its raw value.
     * <p>
     * Checks that the token exists, is not revoked, and has not expired.
     *
     * @param rawToken the raw refresh token string
     * @return an {@link Optional} containing the valid {@link RefreshToken}, or empty if invalid
     */
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

    /**
     * Revokes a refresh token, marking it as invalid.
     *
     * @param token the {@link RefreshToken} to revoke
     */
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    /**
     * Generates a secure random token string using Base64 encoding.
     *
     * @return a secure random token
     */
    private String generateRandomToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Hashes a token using SHA-256 and encodes it in Base64 URL-safe format.
     *
     * @param token the raw token string
     * @return the hashed token string
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Unable to hash token", e);
        }
    }

    /**
     * Returns the configured refresh token duration in milliseconds.
     * <p>
     * Defaults to 30 days if the configured value is unavailable.
     *
     * @return the refresh token expiration duration in milliseconds
     */
    public long refreshTokenServiceDurationMs() {
        // try to read configured duration; fallback to 30 days in ms
        try {
            Field f = this.getClass().getDeclaredField("refreshTokenExpirationMs");
            f.setAccessible(true);
            Object val = f.get(this);
            if (val instanceof Long) return (Long) val;
        } catch (Exception ignored) {}
        return 2592000000L;
    }

}
