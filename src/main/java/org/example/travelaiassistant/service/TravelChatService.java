package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TravelChatService {

    private final ChatClient chatClient;

    private final String SYSTEM_PROMPT = """
            You are a travel assistant.
            
            Your responsibilities include:
                - Help users plan trips and vacations.
                - Keep responses friendly and concise.
            
            When creating itineraries:
                - Recommend exactly {attractionsPerDay} attractions per day.
                - Recommend {foodsPerDay} local food for each day.
                - Keep each day's description under {maxWords} words.
            """;

    public ChatResponse chat(ChatRequest chatRequest) {

        PromptTemplate promptTemplate = PromptTemplate
                .builder()
                .template(SYSTEM_PROMPT)
                .build();

        Message systemMessage = promptTemplate.createMessage(
                Map.of(
                        "attractionsPerDay", 2,
                        "foodsPerDay", 2,
                        "maxWords", 50
                )
        );

        Prompt prompt = new Prompt(
                systemMessage,
                new UserMessage(chatRequest.getMessage())
        );

        String aiResponse = chatClient
                .prompt(prompt)
                .call()
                .content();

        return new ChatResponse(aiResponse);
    }
}
