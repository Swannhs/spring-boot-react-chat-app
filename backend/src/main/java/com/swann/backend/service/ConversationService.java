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
import java.util.*;

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
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return modelMapper.map(message, MessageDTO.class);
    }

    public List<MessageDTO> getConversationMessages(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        Set<Message> sortedMessages = new TreeSet<>(Comparator.comparing(Message::getTimestamp));
        sortedMessages.addAll(conversation.getMessages());

        List<MessageDTO> messageDTOList = new ArrayList<>();
        for (Message message : sortedMessages) {
            messageDTOList.add(modelMapper.map(message, MessageDTO.class));
        }
        return messageDTOList;
    }


    public List<ConversationsDTO> getConversations() {
        return conversationRepository.findAll().stream().map(conversation -> modelMapper.map(conversation, ConversationsDTO.class)).toList();
    }
}
