package com.focusapp.backend.dto;

import com.focusapp.backend.model.Subscription.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    private SubscriptionPlan plan;
    
    private boolean autoRenew = true;
    
    private String paymentMethod;
}
