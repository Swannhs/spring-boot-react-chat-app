package com.swann.backend.controller;

import com.swann.backend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private ConversationService conversationService;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> getConversationMessages(UUID conversationId) {
        return ResponseEntity.ok(conversationService.getConversationMessages(conversationId));
    }

    @PostMapping
    public ResponseEntity<?> createConversation() {
        return ResponseEntity.ok(conversationService.createConversation());
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<?> addMessage(UUID conversationId, String content) {
        return ResponseEntity.ok(conversationService.addMessage(conversationId, content));
    }
}
