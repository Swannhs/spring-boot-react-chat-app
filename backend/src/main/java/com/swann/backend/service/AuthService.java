package com.swann.backend.service;

import com.swann.backend.config.JwtUtils;
import com.swann.backend.pojo.AuthRequest;
import com.swann.backend.pojo.AuthResponse;
import com.swann.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse register(AuthRequest request) {
        UserDetails user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        com.swann.backend.model.User newUser = modelMapper.map(user, com.swann.backend.model.User.class);
        newUser.setEmail(user.getUsername());

        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token);
    }
}
