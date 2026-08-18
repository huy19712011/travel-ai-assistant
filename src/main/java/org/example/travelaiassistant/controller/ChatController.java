package org.example.travelaiassistant.controller;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.service.TravelChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final TravelChatService travelChatService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {

        return travelChatService.chat(chatRequest);
    }

}
