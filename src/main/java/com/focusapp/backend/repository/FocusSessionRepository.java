package com.focusapp.backend.repository;

import com.focusapp.backend.model.FocusSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FocusSessionRepository extends MongoRepository<FocusSession, String> {
    
    List<FocusSession> findByUserId(String userId);
    
    List<FocusSession> findByUserIdAndStartTimeBetween(String userId, LocalDateTime start, LocalDateTime end);
    
    List<FocusSession> findByUserIdAndCompleted(String userId, boolean completed);
    
    List<FocusSession> findByUserIdAndCategory(String userId, String category);
}
