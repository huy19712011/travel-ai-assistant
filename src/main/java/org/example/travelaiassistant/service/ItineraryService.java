package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travelaiassistant.dto.ItineraryRequest;
import org.example.travelaiassistant.dto.ItineraryResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ChatClient chatClient;

    private final ObjectMapper objectMapper;

    @Value("classpath:prompts/travel-system-prompt.st")
    private Resource systemPromptTemplate;

    public ItineraryResponse prepareItinerary(ItineraryRequest itineraryRequest) {

        Message systemMessage = new PromptTemplate(systemPromptTemplate).createMessage(
                Map.of(
                        "attractionsPerDay", 3,
                        "foodsPerDay", 2,
                        "maxWords", 50
                )
        );

        String userMessageTemplate = "Create a {days}-day itinerary for {destination}";

        Message userMessage = new PromptTemplate(userMessageTemplate).createMessage(
                Map.of(
                        "days", itineraryRequest.getDays(),
                        "destination", itineraryRequest.getDestination()
                )
        );

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(ItineraryResponse.class);
    }

    public ItineraryResponse improveItinerary(ItineraryResponse itineraryResponse) {

        String itineraryString = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(itineraryResponse);
        log.info("Draft Itinerary: {}", itineraryString);

        String userMessageTemplate = """
                Review the following travel itinerary.
                
                Check:
                - Logical city progression
                - Travel efficiency
                - Nearby attractions
                - Remove unnecessary travel
                
                Return an improved itinerary using the same JSON structure.
                
                Itinerary:
                {itinerary}
                """;

        Message userMessage = new PromptTemplate(userMessageTemplate).createMessage(
                Map.of(
                        "itinerary", itineraryString
                )
        );

        Prompt prompt = new Prompt(userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(ItineraryResponse.class);

    }
}
