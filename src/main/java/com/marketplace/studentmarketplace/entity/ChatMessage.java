package com.marketplace.studentmarketplace.entity;

import com.marketplace.studentmarketplace.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The student who sent this message
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // Text content, image URL, or link URL
    @Column(nullable = false, length = 2000)
    private String content;

    // Optional: caption for images or link title
    @Column(length = 300)
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Column(updatable = false, nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
        if (messageType == null) messageType = MessageType.TEXT;
    }
}