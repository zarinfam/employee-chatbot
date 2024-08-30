package com.saeed.employee.chatbot.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saeed.employee.chatbot.service.ChatbotService;

@RestController
@RequestMapping("/employee")
public class ChatbotController {

    private final ChatbotService chatbotService;

    ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/chat/{chatId}/{userMessage}")
    public String chat(@PathVariable String chatId, @PathVariable String userMessage) {
        return chatbotService.chat(chatId, userMessage);
    }

}
