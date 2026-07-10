package com.marketplace.studentmarketplace.dto.response;

import com.marketplace.studentmarketplace.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Data @Builder
public class ChatMessageResponse {
    private Long id;
    private String senderName;
    private String senderCollege;
    private Long senderId;
    private String content;
    private String caption;
    private MessageType messageType;
    private LocalDateTime sentAt;
}