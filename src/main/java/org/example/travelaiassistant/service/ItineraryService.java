package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ItineraryRequest;
import org.example.travelaiassistant.dto.ItineraryResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/travel-system-prompt.st")
    private Resource systemPromptTemplate;

    public ItineraryResponse prepareItinerary(ItineraryRequest itineraryRequest) {

        PromptTemplate promptTemplate = new PromptTemplate(systemPromptTemplate);

        Message systemMessage = promptTemplate.createMessage(
                Map.of(
                        "attractionsPerDay", 3,
                        "foodsPerDay", 2,
                        "maxWords", 50
                )
        );

        String userMessage = "Create a {days}-day itinerary for {destination}";

        Message input = new PromptTemplate(userMessage).createMessage(
                Map.of(
                        "days", itineraryRequest.getDays(),
                        "destination", itineraryRequest.getDestination()
                )
        );

        Prompt prompt = new Prompt(systemMessage, input);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(ItineraryResponse.class);
    }
}
