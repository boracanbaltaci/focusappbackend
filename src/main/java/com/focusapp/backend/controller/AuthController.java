package com.focusapp.backend.controller;

import com.focusapp.backend.dto.JwtResponse;
import com.focusapp.backend.dto.LoginRequest;
import com.focusapp.backend.dto.MessageResponse;
import com.focusapp.backend.dto.SignupRequest;
import com.focusapp.backend.model.Subscription;
import com.focusapp.backend.model.User;
import com.focusapp.backend.repository.SubscriptionRepository;
import com.focusapp.backend.repository.UserRepository;
import com.focusapp.backend.security.JwtUtils;
import com.focusapp.backend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    
    private final UserRepository userRepository;
    
    private final SubscriptionRepository subscriptionRepository;
    
    private final PasswordEncoder encoder;
    
    private final JwtUtils jwtUtils;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(java.util.stream.Collectors.toSet())
        ));
    }

    @Operation(summary = "User registration", description = "Register a new user with FREE subscription")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Create FREE subscription for new user
        Subscription subscription = new Subscription();
        subscription.setUserId(savedUser.getId());
        subscription.setPlan(Subscription.SubscriptionPlan.FREE);
        subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setAutoRenew(true);
        subscriptionRepository.save(subscription);

        return ResponseEntity.ok(new MessageResponse("User registered successfully with FREE subscription!"));
    }
}
