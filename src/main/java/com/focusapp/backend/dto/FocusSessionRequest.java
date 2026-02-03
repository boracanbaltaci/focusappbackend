package com.focusapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusSessionRequest {

    private String title;
    
    private String description;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String category;
    
    private String tags;
}
