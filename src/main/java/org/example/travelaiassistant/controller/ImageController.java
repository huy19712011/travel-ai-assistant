package org.example.travelaiassistant.controller;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.service.ImageUnderstandingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUnderstandingService imageUnderstandingService;

    @PostMapping("/analyze")
    public ChatResponse analyze(@RequestBody ChatRequest chatRequest) {

        String answer = imageUnderstandingService.analyzeImage(chatRequest);

        return new ChatResponse(UUID.randomUUID().toString(), answer);
    }
}
