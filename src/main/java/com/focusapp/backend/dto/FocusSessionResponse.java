package com.focusapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusSessionResponse {

    private String id;
    
    private String userId;
    
    private String title;
    
    private String description;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Long durationInSeconds;
    
    private String category;
    
    private String tags;
    
    private boolean completed;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
