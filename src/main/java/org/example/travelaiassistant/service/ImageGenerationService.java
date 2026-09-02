package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageGenerationService {

    private final ImageModel imageModel;

    public byte[] generate(String message) {

        ImagePrompt prompt = new ImagePrompt(message);

        ImageResponse response = imageModel.call(prompt);
        Image image = response.getResult().getOutput();

        String base64Image = image.getB64Json();

        return Base64.getDecoder().decode(base64Image);
    }
}
