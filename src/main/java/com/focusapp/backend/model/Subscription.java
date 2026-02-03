package com.focusapp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    @Indexed
    private String userId;

    private SubscriptionPlan plan;

    private SubscriptionStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean autoRenew = true;

    private String paymentMethod;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum SubscriptionPlan {
        FREE,
        BASIC,
        PREMIUM,
        ENTERPRISE
    }

    public enum SubscriptionStatus {
        ACTIVE,
        INACTIVE,
        CANCELLED,
        EXPIRED,
        TRIAL
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && 
               (endDate == null || endDate.isAfter(LocalDateTime.now()));
    }
}
