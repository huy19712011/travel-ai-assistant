package org.example.travelaiassistant.controller;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.dto.ItineraryRequest;
import org.example.travelaiassistant.dto.ItineraryResponse;
import org.example.travelaiassistant.service.ItineraryService;
import org.example.travelaiassistant.service.TravelChatService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final TravelChatService travelChatService;

    private final ItineraryService itineraryService;

    private final ChatMemory chatMemory;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {

        return travelChatService.chat(chatRequest);
    }

    @PostMapping("/itinerary")
    public ItineraryResponse generateItinerary(@RequestBody ItineraryRequest itineraryRequest) {

        ItineraryResponse draftItinerary = itineraryService.prepareItinerary(itineraryRequest); // 1st response

        return itineraryService.improveItinerary(draftItinerary); // 2nd response
    }

    @GetMapping("/memory")
    public List<Message> fetchMemory() {

        return chatMemory.get("1234");
    }

}
