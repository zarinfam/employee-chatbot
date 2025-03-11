package com.saeed.employee.chatbot.config;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatbotConfig {

    @Bean
    ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    // @Bean
    // VectorStore vectorStore(EmbeddingModel embeddingModel) {
    //     return SimpleVectorStore.builder(embeddingModel).build();
    // }

    // @Bean
    // RetrievalAugmentationAdvisor RagAdvisor(VectorStore vectorStore) {
    //     return RetrievalAugmentationAdvisor.builder()
    //             .documentRetriever(VectorStoreDocumentRetriever.builder()
    //                     .similarityThreshold(0.50)
    //                     .vectorStore(vectorStore)
    //                     .build())
    //             .build();
    // }
}
