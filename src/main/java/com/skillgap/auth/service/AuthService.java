package com.skillgap.auth.service;

import com.skillgap.auth.dto.LoginRequest;
import com.skillgap.auth.dto.LoginResponse;
import com.skillgap.auth.dto.RegisterRequest;
import com.skillgap.common.exception.EmailAlreadyExistsException;
import com.skillgap.user.model.User;
import com.skillgap.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

import javax.management.RuntimeErrorException;
import com.skillgap.common.exception.EmailAlreadyExistsException;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
            
        public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {

            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
        }

    public User register(RegisterRequest request){
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email already Registered");
        }
        String passwordHash=passwordEncoder.encode(request.getPassword());
        User user = new User(
            request.getName().trim(),
            email,
            passwordHash,
            "Student",
            Instant.now()
        );
        return userRepository.save(user);
    }
    public LoginResponse login(LoginRequest request) {

    String email = request.getEmail()
            .trim()
            .toLowerCase(Locale.ROOT);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash())) {

        throw new RuntimeException("Invalid email or password");
    }

    String token = jwtService.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole()
    );

    return new LoginResponse(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
    );
}
}
