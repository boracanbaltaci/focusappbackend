package com.focusapp.backend.controller;

import com.focusapp.backend.dto.SubscriptionRequest;
import com.focusapp.backend.dto.SubscriptionResponse;
import com.focusapp.backend.security.UserDetailsImpl;
import com.focusapp.backend.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subscriptions", description = "Subscription management APIs")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Get current user's subscription")
    @GetMapping
    public ResponseEntity<SubscriptionResponse> getUserSubscription(Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        SubscriptionResponse subscription = subscriptionService.getUserSubscription(userId);
        return ResponseEntity.ok(subscription);
    }

    @Operation(summary = "Create or upgrade subscription")
    @PostMapping
    public ResponseEntity<SubscriptionResponse> createOrUpdateSubscription(
            @RequestBody SubscriptionRequest request,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        SubscriptionResponse subscription = subscriptionService.createOrUpdateSubscription(userId, request);
        return ResponseEntity.ok(subscription);
    }

    @Operation(summary = "Cancel subscription")
    @PostMapping("/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        SubscriptionResponse subscription = subscriptionService.cancelSubscription(userId);
        return ResponseEntity.ok(subscription);
    }

    private String getUserIdFromAuth(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}
