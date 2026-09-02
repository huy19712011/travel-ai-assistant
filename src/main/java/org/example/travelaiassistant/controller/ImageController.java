package org.example.travelaiassistant.controller;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.service.ImageGenerationService;
import org.example.travelaiassistant.service.ImageUnderstandingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUnderstandingService imageUnderstandingService;
    private final ImageGenerationService imageGenerationService;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateImage(@RequestParam String message) {

        byte[] imageBytes = imageGenerationService.generate(message);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }

    @PostMapping("/analyze")
    public ChatResponse analyze(@RequestBody ChatRequest chatRequest) {

        String answer = imageUnderstandingService.analyzeImage(chatRequest);

        return new ChatResponse(UUID.randomUUID().toString(), answer);
    }
}
