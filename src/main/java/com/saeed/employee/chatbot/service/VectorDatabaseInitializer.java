package com.saeed.employee.chatbot.service;

import jakarta.annotation.PostConstruct;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
class VectorDatabaseInitializer {

    private final VectorStore vectorStore;

    @Value("classpath:rules/rules1.txt")
    Resource rules1;

    VectorDatabaseInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    void init() {
        List<Document> documents = new ArrayList<>();

        var textReader = new TextReader(rules1);
        textReader.getCustomMetadata().put("filename", "rules1");
        textReader.setCharset(Charset.defaultCharset());
        documents.addAll(textReader.get());

        var textSplitter = new TokenTextSplitter();
        var transformedDocuments = textSplitter.apply(documents);

        vectorStore.add(transformedDocuments);
    }
}
