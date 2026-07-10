package com.marketplace.studentmarketplace.dto.request;

import com.marketplace.studentmarketplace.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatMessageRequest {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    // Optional caption (used for IMAGE and LINK types)
    private String caption;

    @NotNull(message = "Message type is required")
    private MessageType messageType;
}