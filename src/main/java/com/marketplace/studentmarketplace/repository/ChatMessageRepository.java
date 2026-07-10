package com.marketplace.studentmarketplace.repository;

import com.marketplace.studentmarketplace.entity.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // All messages ordered oldest-first (for displaying a chat history)
    List<ChatMessage> findAllByOrderBySentAtAsc();

    // Latest N messages (for pagination / "load more")
    List<ChatMessage> findTop50ByOrderBySentAtDesc();
}