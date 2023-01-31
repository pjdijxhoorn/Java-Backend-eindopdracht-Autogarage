package com.example.garage.Filter;

import com.example.garage.Services.CustomUserDetailsService;
import com.example.garage.Utilities.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;
    @Test
    void doFilterInternal() throws ServletException, IOException {
        // Arrange
        String jwt = "validJwt";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtil.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(jwt, userDetails)).thenReturn(true);
        // Act
        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(authentication.getPrincipal(), userDetails);
    }}
