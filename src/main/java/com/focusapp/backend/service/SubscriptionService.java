package com.focusapp.backend.service;

import com.focusapp.backend.dto.SubscriptionRequest;
import com.focusapp.backend.dto.SubscriptionResponse;
import com.focusapp.backend.model.Subscription;
import com.focusapp.backend.model.Subscription.SubscriptionStatus;
import com.focusapp.backend.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createOrUpdateSubscription(String userId, SubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElse(new Subscription());
        
        subscription.setUserId(userId);
        subscription.setPlan(request.getPlan());
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setAutoRenew(request.isAutoRenew());
        subscription.setPaymentMethod(request.getPaymentMethod());
        
        if (subscription.getStartDate() == null) {
            subscription.setStartDate(LocalDateTime.now());
        }
        
        // Set end date based on plan (example: 30 days for all plans)
        subscription.setEndDate(LocalDateTime.now().plusDays(30));
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        log.info("Created/Updated subscription {} for user {} with plan {}", 
                savedSubscription.getId(), userId, request.getPlan());
        
        return mapToResponse(savedSubscription);
    }

    public SubscriptionResponse getUserSubscription(String userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user: " + userId));
        
        return mapToResponse(subscription);
    }

    public SubscriptionResponse cancelSubscription(String userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user: " + userId));
        
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);
        
        Subscription cancelledSubscription = subscriptionRepository.save(subscription);
        log.info("Cancelled subscription {} for user {}", cancelledSubscription.getId(), userId);
        
        return mapToResponse(cancelledSubscription);
    }

    public boolean hasActiveSubscription(String userId) {
        return subscriptionRepository.findByUserId(userId)
                .map(Subscription::isActive)
                .orElse(false);
    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setUserId(subscription.getUserId());
        response.setPlan(subscription.getPlan());
        response.setStatus(subscription.getStatus());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setAutoRenew(subscription.isAutoRenew());
        response.setPaymentMethod(subscription.getPaymentMethod());
        response.setCreatedAt(subscription.getCreatedAt());
        response.setUpdatedAt(subscription.getUpdatedAt());
        return response;
    }
}
