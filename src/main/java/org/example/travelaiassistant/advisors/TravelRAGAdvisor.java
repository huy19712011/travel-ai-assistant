package org.example.travelaiassistant.advisors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TravelRAGAdvisor implements BaseAdvisor {

    private final VectorStore vectorStore;

    @Value("classpath:prompts/travel-rag-prompt.st")
    private Resource promptTemplateResource;

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {

        log.info("==========================================");
        log.info("Travel RAG Advisor");
        log.info("==========================================");

        // 1. get user's question
        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();
        String question = userMessage.getText();

        // 2. search vector store
        log.info("Searching vector store...");
        List<Document> documents = vectorStore.similaritySearch(question);

        // 3. prepare context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));

        // 4. augment prompt
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);
        String augmentedPrompt = promptTemplate.render(Map.of(
                "question", question,
                "context", context
        ));

        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedPrompt))
                .build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
