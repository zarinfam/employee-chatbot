package com.saeed.employee.chatbot.api;

import com.saeed.employee.chatbot.api.model.UserMessage;
import com.saeed.employee.chatbot.service.ChatbotService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class ChatbotController {

    private final ChatbotService chatbotService;

    ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat/{chatId}")
    public String chat(@PathVariable String chatId, @RequestBody UserMessage userMessage) {
        return chatbotService.chat(chatId, userMessage.text()).getResult().getOutput().getText();
    }
}
