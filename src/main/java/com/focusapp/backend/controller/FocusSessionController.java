package com.focusapp.backend.controller;

import com.focusapp.backend.dto.FocusSessionRequest;
import com.focusapp.backend.dto.FocusSessionResponse;
import com.focusapp.backend.security.UserDetailsImpl;
import com.focusapp.backend.service.FocusSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Focus Sessions", description = "Focus session management APIs")
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    @Operation(summary = "Create a new focus session")
    @PostMapping
    public ResponseEntity<FocusSessionResponse> createSession(
            @RequestBody FocusSessionRequest request,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        FocusSessionResponse response = focusSessionService.createSession(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all sessions for current user")
    @GetMapping
    public ResponseEntity<List<FocusSessionResponse>> getUserSessions(Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        List<FocusSessionResponse> sessions = focusSessionService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Get sessions in date range")
    @GetMapping("/range")
    public ResponseEntity<List<FocusSessionResponse>> getSessionsInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        List<FocusSessionResponse> sessions = focusSessionService.getUserSessionsInDateRange(userId, start, end);
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Get session by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FocusSessionResponse> getSessionById(
            @PathVariable String id,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        FocusSessionResponse session = focusSessionService.getSessionById(id, userId);
        return ResponseEntity.ok(session);
    }

    @Operation(summary = "Update a focus session")
    @PutMapping("/{id}")
    public ResponseEntity<FocusSessionResponse> updateSession(
            @PathVariable String id,
            @RequestBody FocusSessionRequest request,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        FocusSessionResponse response = focusSessionService.updateSession(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Complete a focus session")
    @PostMapping("/{id}/complete")
    public ResponseEntity<FocusSessionResponse> completeSession(
            @PathVariable String id,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        FocusSessionResponse response = focusSessionService.completeSession(id, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a focus session")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable String id,
            Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        focusSessionService.deleteSession(id, userId);
        return ResponseEntity.noContent().build();
    }

    private String getUserIdFromAuth(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}
