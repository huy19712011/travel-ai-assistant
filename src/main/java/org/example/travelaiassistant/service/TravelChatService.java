package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

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
                - Recommend exactly 2 attractions per day.
                - Recommend 1 local food for each day.
                - Keep each day's description under 50 words.
            """;

    public ChatResponse chat(ChatRequest chatRequest) {


        Prompt prompt = new Prompt(
                new SystemMessage(SYSTEM_PROMPT),
                new UserMessage("Show me a 2-day itinerary to Hanoi City"),
                new AssistantMessage("""
                        Day 1
                        Attractions:
                        - Place 1
                        - Place 2
                        Food:
                        - Food 1
                        
                        Day 2
                        Attractions:
                        - Place 1
                        - Place 2
                        Food:
                        - Food 2
                        
                        
                        """)
        );

        String aiResponse = chatClient
                .prompt(prompt)
                .call()
                .content();

        return new ChatResponse(aiResponse);
    }
}
