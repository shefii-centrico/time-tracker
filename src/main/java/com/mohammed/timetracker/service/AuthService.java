package com.mohammed.timetracker.service;

import com.mohammed.timetracker.dto.LoginRequest;
import com.mohammed.timetracker.dto.LoginResponse;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.repository.UserRepository;
import com.mohammed.timetracker.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authManager,
                       UserRepository userRepository,
                       JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user);
        return new LoginResponse(token, user.getUsername(), user.getRole().name(), user.getFullName());
    }
}
