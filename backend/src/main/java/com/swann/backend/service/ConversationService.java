package com.swann.backend.service;

import com.swann.backend.exception.ResourceNotFoundException;
import com.swann.backend.model.Conversation;
import com.swann.backend.model.Message;
import com.swann.backend.pojo.ConversationsDTO;
import com.swann.backend.pojo.MessageDTO;
import com.swann.backend.pojo.NewMessageDTO;
import com.swann.backend.repository.ConversationRepository;
import com.swann.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ModelMapper modelMapper;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public Conversation createConversation() {
        return conversationRepository.save(new Conversation());
    }

    public MessageDTO addMessage(UUID conversationId, NewMessageDTO newMessageDTO) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        Message message = modelMapper.map(newMessageDTO, Message.class);
        message.setConversation(conversation);
        messageRepository.save(message);
        return modelMapper.map(message, MessageDTO.class);
    }

    public List<MessageDTO> getConversationMessages(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        return conversation.getMessages().stream().map(message -> modelMapper.map(message, MessageDTO.class)).toList();
    }

    public List<ConversationsDTO> getConversations() {
        return conversationRepository.findAll().stream().map(conversation -> modelMapper.map(conversation, ConversationsDTO.class)).toList();
    }
}
