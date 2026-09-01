package org.example.travelaiassistant.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class PDFLoader {

    @Value("${app.data.pdfs.path}")
    private String pdfFolder;

    public List<Document> loadDocuments() throws Exception{

        List<Document> documents = new ArrayList<>();

        Files.list(Path.of(pdfFolder))
                .filter(path -> path.toString().endsWith(".pdf"))
                .forEach(path -> {
                    PagePdfDocumentReader reader = new PagePdfDocumentReader(new FileSystemResource(path));
                    List<Document> pdfDocuments = reader.get();
                    pdfDocuments.forEach(document -> {
                        document.getMetadata().put("source", path.getFileName().toString());
                    });
                    documents.addAll(pdfDocuments);
                });

        return documents;
    }
}
