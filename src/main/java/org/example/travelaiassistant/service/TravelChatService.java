package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelChatService {

    private final ChatClient chatClient;

    public ChatResponse chat(ChatRequest chatRequest) {

        String aiResponse = chatClient.prompt()
                .user(chatRequest.getMessage())
                .call()
                .content();

        return new ChatResponse(aiResponse);
    }
}
