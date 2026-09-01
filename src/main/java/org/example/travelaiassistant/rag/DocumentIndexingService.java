package org.example.travelaiassistant.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentIndexingService {

    private final PDFLoader pdfLoader;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public void indexDocuments() throws Exception {

        List<Document> documents = pdfLoader.loadDocuments();

        TokenTextSplitter splitter = TokenTextSplitter.builder().build();

        List<Document> chunks = splitter.apply(documents);

        vectorStore.add(chunks);

        log.info("Indexed {} documents into pgvector.", chunks.size());
    }

    public void deleteAllDocuments() {

        jdbcTemplate.execute("TRUNCATE TABLE vector_store RESTART IDENTITY");

        log.info("Deleted all indexed documents");
    }
}
