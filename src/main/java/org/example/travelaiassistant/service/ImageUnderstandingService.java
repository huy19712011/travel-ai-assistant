package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@RequiredArgsConstructor
public class ImageUnderstandingService {

    private final ChatClient chatClient;

    public String analyzeImage(ChatRequest request) {

        Resource image = new FileSystemResource("data/images/" + request.getImageName());

        return chatClient.prompt()
                .user(spec ->
                        spec
                                .text(request.getMessage())
                                .media(MimeTypeUtils.IMAGE_JPEG, image))
                .call()
                .content();
    }
}
