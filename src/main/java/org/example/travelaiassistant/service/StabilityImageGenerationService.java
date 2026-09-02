package org.example.travelaiassistant.service;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StyleEnum;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class StabilityImageGenerationService {

    private final ImageModel imageModel;

    public StabilityImageGenerationService(@Qualifier("stabilityAiImageModel") ImageModel imageModel) {

        this.imageModel = imageModel;
    }

    public byte[] generate(String message) {

        // many options can set to image here!!!
        StabilityAiImageOptions options = StabilityAiImageOptions.builder()
                .stylePreset(StyleEnum.THREE_D_MODEL)
                .build();

        ImagePrompt prompt = new ImagePrompt(message, options);
        ImageResponse response = imageModel.call(prompt);
        Image image = response.getResult().getOutput();

        String base64Image = image.getB64Json();
        return Base64.getDecoder().decode(base64Image);
    }
}
