package com.saeed.employee.chatbot.service;

import static org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor.TOP_K;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatbotService {

    private final ChatClient chatClient;

    ChatbotService(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            RetrievalAugmentationAdvisor ragAdvisor,
            @Value("classpath:/prompts/system-prompt.st") Resource systemPromptResource) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), ragAdvisor)
                .defaultSystem(systemPromptResource)
                .build();
    }

    public String chat(String chatId, String userMessage) {
        return chatClient
                .prompt()
                .user(userMessage)
                .advisors(a -> a.param(CONVERSATION_ID, chatId)
                        .param(TOP_K, 50))
                .call()
                .content();
    }

    public Flux<ChatResponse> chatAsync(String chatId, String userMessage) {
        return chatClient
                .prompt()
                .user(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)
                        .param(TOP_K, 50))
                .stream()
                .chatResponse();
    }    
}
