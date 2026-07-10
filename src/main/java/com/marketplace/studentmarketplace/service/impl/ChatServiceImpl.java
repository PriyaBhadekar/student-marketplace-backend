package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.ChatMessageRequest;
import com.marketplace.studentmarketplace.dto.response.ChatMessageResponse;
import com.marketplace.studentmarketplace.entity.ChatMessage;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.exception.BadRequestException;
import com.marketplace.studentmarketplace.exception.ResourceNotFoundException;
import com.marketplace.studentmarketplace.repository.ChatMessageRepository;
import com.marketplace.studentmarketplace.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessageResponse sendMessage(User sender, ChatMessageRequest request) {
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .content(request.getContent())
                .caption(request.getCaption())
                .messageType(request.getMessageType())
                .build();

        return mapToResponse(chatMessageRepository.save(message));
    }

    @Override
    public List<ChatMessageResponse> getChatHistory() {
        return chatMessageRepository.findAllByOrderBySentAtAsc()
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ChatMessageResponse> getRecentMessages() {

        List<ChatMessageResponse> recent = chatMessageRepository
                .findTop50ByOrderBySentAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();

        // Convert to mutable list (IMPORTANT)
        List<ChatMessageResponse> reversedList = new java.util.ArrayList<>(recent);

        // Reverse it
        java.util.Collections.reverse(reversedList);

        return reversedList;
    }

    @Override
    public void deleteMessage(Long messageId, User user) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSender().getId().equals(user.getId())) {
            throw new BadRequestException("You can only delete your own messages");
        }
        chatMessageRepository.delete(message);
    }

    private ChatMessageResponse mapToResponse(ChatMessage msg) {
        return ChatMessageResponse.builder()
                .id(msg.getId())
                .senderId(msg.getSender().getId())
                .senderName(msg.getSender().getFullName())
                .senderCollege(msg.getSender().getCollegeName())
                .content(msg.getContent())
                .caption(msg.getCaption())
                .messageType(msg.getMessageType())
                .sentAt(msg.getSentAt())
                .build();
    }
}