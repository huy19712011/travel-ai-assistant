package org.example.travelaiassistant.service;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.advisors.TravelRAGAdvisor;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.tools.ContactTools;
import org.example.travelaiassistant.tools.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravelChatService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/travel-system-prompt.st")
    private Resource systemPromptTemplate;

    private final ChatMemory chatMemory;

    private final WeatherTools weatherTools;

    private final ContactTools contactTools;

    private final VectorStore vectorStore;

    private final TravelRAGAdvisor travelRAGAdvisor;

    public ChatResponse chat(ChatRequest chatRequest) {

        PromptTemplate promptTemplate = new PromptTemplate(systemPromptTemplate);

        Message systemMessage = promptTemplate.createMessage(
                Map.of(
                        "attractionsPerDay", 2,
                        "foodsPerDay", 2,
                        "maxWords", 50
                )
        );

        Prompt prompt = new Prompt(
                systemMessage,
                new UserMessage(chatRequest.getMessage())
        );

        String conversationId = chatRequest.getConversationId() == null ? UUID.randomUUID().toString() : chatRequest.getConversationId();

        String aiResponse = chatClient
                .prompt(prompt)
                .advisors(advisorSpec ->
                        advisorSpec.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                                .param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(travelRAGAdvisor)
                .tools(weatherTools, contactTools)
                .call()
                .content();

        return new ChatResponse(conversationId, aiResponse);
    }
}
