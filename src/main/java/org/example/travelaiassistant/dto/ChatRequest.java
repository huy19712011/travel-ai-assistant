package org.example.travelaiassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatRequest {

    private String conversationId;
    private String message;
    private String imageName;
}
