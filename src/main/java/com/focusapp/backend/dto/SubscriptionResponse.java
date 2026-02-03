package com.focusapp.backend.dto;

import com.focusapp.backend.model.Subscription.SubscriptionPlan;
import com.focusapp.backend.model.Subscription.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {

    private String id;
    
    private String userId;
    
    private SubscriptionPlan plan;
    
    private SubscriptionStatus status;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private boolean autoRenew;
    
    private String paymentMethod;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
