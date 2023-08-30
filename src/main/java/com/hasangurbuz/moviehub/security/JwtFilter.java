package com.hasangurbuz.moviehub.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasangurbuz.moviehub.config.PublicEndpoint;
import com.hasangurbuz.moviehub.service.impl.UserDetailServiceImpl;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String JWT_HEADER = "Authorization";

    private final JwtService jwtService;

    private final UserDetailServiceImpl userDetailsService;

    private final ObjectMapper objectMapper;

    private final PublicEndpoint publicEndpoint;

    private String token;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean isPublic = publicEndpoint.getAll(true).contains(request.getRequestURI());
        if (isPublic) {
            return true;
        }

        boolean authenticated = SecurityContextHolder.getContext().getAuthentication() != null;
        if (authenticated) {
            return true;
        }

        token = request.getHeader(JWT_HEADER);
        if (StringUtils.isBlank(token)) {
            return true;
        }

        if (!token.startsWith("Bearer ")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        UserDetails userDetails = null;
        boolean isTokenValid = false;

        token = token.substring(7);

        try {
            String username = jwtService.extractUsername(token);
            userDetails = userDetailsService.loadUserByUsername(username);
            jwtService.validateToken(token, userDetails.getUsername());
        } catch (Exception ex) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
