package com.venclima.config;

import com.venclima.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(
            JWTService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Intercepts each HTTP request to check for a valid JWT token in the "Authorization" header.
     * <p>
     * If a valid JWT is present and corresponds to a user, authentication is set in the
     * {@link SecurityContextHolder}. If the token is invalid or missing, the filter passes
     * control to the next filter without authentication.
     * <p>
     * Any exceptions during token processing are handled via {@link HandlerExceptionResolver}.
     *
     * @param request the incoming {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @param filterChain the filter chain to pass control to the next filter
     * @throws ServletException if an error occurs during request filtering
     * @throws IOException if an I/O error occurs during request filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        logger.debug("Incoming request {} {} - Authorization header present: {}", request.getMethod(), request.getRequestURI(), authHeader != null);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            logger.debug("JWT present (len={}): [redacted]", jwt != null ? jwt.length() : 0);

            final String userEmail = jwtService.extractUsername(jwt);
            logger.debug("Extracted username from JWT: {}", userEmail);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.debug("Token valid for user {}", userEmail);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("Invalid token for user {}", userEmail);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            logger.error("Error while processing JWT: {}", exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
