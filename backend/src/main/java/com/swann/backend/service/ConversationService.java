package com.swann.backend.service;

import com.swann.backend.exception.ResourceNotFoundException;
import com.swann.backend.model.Conversation;
import com.swann.backend.model.Message;
import com.swann.backend.repository.ConversationRepository;
import com.swann.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private ConversationRepository conversationRepository;
    private MessageRepository messageRepository;

    public Conversation createConversation() {
        return conversationRepository.save(new Conversation());
    }

    public Message addMessage(UUID conversationId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        Message message = new Message();
        message.setContent(content);
        message.setConversation(conversation);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public Set<Message> getConversationMessages(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        return conversation.getMessages();
    }
}
