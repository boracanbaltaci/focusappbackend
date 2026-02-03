package com.focusapp.backend.service;

import com.focusapp.backend.dto.FocusSessionRequest;
import com.focusapp.backend.dto.FocusSessionResponse;
import com.focusapp.backend.model.FocusSession;
import com.focusapp.backend.repository.FocusSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;

    public FocusSessionResponse createSession(String userId, FocusSessionRequest request) {
        FocusSession session = new FocusSession();
        session.setUserId(userId);
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now());
        session.setEndTime(request.getEndTime());
        session.setCategory(request.getCategory());
        session.setTags(request.getTags());
        session.setCompleted(false);
        
        if (request.getEndTime() != null && request.getStartTime() != null) {
            long duration = Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
            session.setDurationInSeconds(duration);
        }
        
        FocusSession savedSession = focusSessionRepository.save(session);
        log.info("Created focus session {} for user {}", savedSession.getId(), userId);
        return mapToResponse(savedSession);
    }

    public FocusSessionResponse updateSession(String sessionId, String userId, FocusSessionRequest request) {
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found with id: " + sessionId));
        
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this session");
        }
        
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setCategory(request.getCategory());
        session.setTags(request.getTags());
        
        if (request.getEndTime() != null && request.getStartTime() != null) {
            long duration = Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
            session.setDurationInSeconds(duration);
        }
        
        FocusSession updatedSession = focusSessionRepository.save(session);
        log.info("Updated focus session {}", sessionId);
        return mapToResponse(updatedSession);
    }

    public FocusSessionResponse completeSession(String sessionId, String userId) {
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found with id: " + sessionId));
        
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to complete this session");
        }
        
        session.setCompleted(true);
        if (session.getEndTime() == null) {
            session.setEndTime(LocalDateTime.now());
        }
        
        if (session.getStartTime() != null && session.getEndTime() != null) {
            long duration = Duration.between(session.getStartTime(), session.getEndTime()).getSeconds();
            session.setDurationInSeconds(duration);
        }
        
        FocusSession completedSession = focusSessionRepository.save(session);
        log.info("Completed focus session {}", sessionId);
        return mapToResponse(completedSession);
    }

    public List<FocusSessionResponse> getUserSessions(String userId) {
        return focusSessionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FocusSessionResponse> getUserSessionsInDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return focusSessionRepository.findByUserIdAndStartTimeBetween(userId, start, end)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FocusSessionResponse getSessionById(String sessionId, String userId) {
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found with id: " + sessionId));
        
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to view this session");
        }
        
        return mapToResponse(session);
    }

    public void deleteSession(String sessionId, String userId) {
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found with id: " + sessionId));
        
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to delete this session");
        }
        
        focusSessionRepository.delete(session);
        log.info("Deleted focus session {}", sessionId);
    }

    private FocusSessionResponse mapToResponse(FocusSession session) {
        FocusSessionResponse response = new FocusSessionResponse();
        response.setId(session.getId());
        response.setUserId(session.getUserId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setDurationInSeconds(session.getDurationInSeconds());
        response.setCategory(session.getCategory());
        response.setTags(session.getTags());
        response.setCompleted(session.isCompleted());
        response.setCreatedAt(session.getCreatedAt());
        response.setUpdatedAt(session.getUpdatedAt());
        return response;
    }
}
