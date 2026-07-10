package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.ChatMessageRequest;
import com.marketplace.studentmarketplace.dto.response.ChatMessageResponse;
import com.marketplace.studentmarketplace.entity.User;

import java.util.List;

public interface ChatService {

    // Send a message to the common chatroom
    ChatMessageResponse sendMessage(User sender, ChatMessageRequest request);

    // Get full chat history (oldest first)
    List<ChatMessageResponse> getChatHistory();

    // Get latest 50 messages (most recent first)
    List<ChatMessageResponse> getRecentMessages();

    // Delete own message
    void deleteMessage(Long messageId, User user);
}