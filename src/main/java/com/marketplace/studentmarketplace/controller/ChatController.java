package com.marketplace.studentmarketplace.controller;

import com.marketplace.studentmarketplace.dto.request.ChatMessageRequest;
import com.marketplace.studentmarketplace.dto.response.ChatMessageResponse;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * POST /api/chat
     * Send a message (text, image URL, or link) to the common chatroom.
     */
    @PostMapping
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChatMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.sendMessage(user, request));
    }

    /**
     * GET /api/chat/history
     * Full chat history — all messages, oldest first.
     */
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageResponse>> getChatHistory() {
        return ResponseEntity.ok(chatService.getChatHistory());
    }

    /**
     * GET /api/chat/recent
     * Last 50 messages in chronological order.
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ChatMessageResponse>> getRecentMessages() {
        return ResponseEntity.ok(chatService.getRecentMessages());
    }

    /**
     * DELETE /api/chat/{messageId}
     * Delete your own message.
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long messageId,
            @AuthenticationPrincipal User user) {
        chatService.deleteMessage(messageId, user);
        return ResponseEntity.noContent().build();
    }
}