package com.swann.backend.controller;

import com.swann.backend.exception.ErrorResponse;
import com.swann.backend.exception.ResourceNotFoundException;
import com.swann.backend.pojo.MessageDTO;
import com.swann.backend.pojo.NewMessageDTO;
import com.swann.backend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ConversationService conversationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<?> getConversations() {
        return ResponseEntity.ok(conversationService.getConversations());
    }

    @PostMapping
    public ResponseEntity<?> createConversation() {
        return ResponseEntity.ok(conversationService.createConversation());
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<?> addMessage(@PathVariable UUID conversationId, @RequestBody NewMessageDTO newMessageDTO) {
        log.info("Received new message: {}", newMessageDTO);
        MessageDTO savedMessageDTO = conversationService.addMessage(conversationId, newMessageDTO);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + conversationId, savedMessageDTO);
        return ResponseEntity.ok(savedMessageDTO);
    }

    @MessageMapping("/chat/{conversationId}/sendMessage")
    public void sendMessage(@DestinationVariable UUID conversationId, @Payload NewMessageDTO newMessageDTO) {
        MessageDTO savedMessageDTO = conversationService.addMessage(conversationId, newMessageDTO);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + conversationId, savedMessageDTO);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> getConversationMessages(@PathVariable UUID conversationId) {
        return ResponseEntity.ok(conversationService.getConversationMessages(conversationId));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorResponse("Resource Not Found", ex.getMessage());
    }
}
