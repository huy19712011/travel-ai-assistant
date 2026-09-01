package org.example.travelaiassistant.rag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DocumentIndexingServiceTest {

    @Autowired
    private DocumentIndexingService documentIndexingService;

    @Test
    void  shouldIndexDocuments() throws Exception {

        documentIndexingService.deleteAllDocuments();

        documentIndexingService.indexDocuments();
    }
}
