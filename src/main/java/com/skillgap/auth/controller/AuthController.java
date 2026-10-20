package com.skillgap.auth.controller;

import com.skillgap.auth.dto.LoginRequest;
import com.skillgap.auth.dto.LoginResponse;
import com.skillgap.auth.dto.RegisterRequest;
import com.skillgap.auth.dto.RegisterResponse;
import com.skillgap.auth.service.AuthService;
import com.skillgap.user.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        User user = authService.register(request);
        RegisterResponse response = new RegisterResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
