package com.saeed.employee.chatbot.ui;

import com.saeed.employee.chatbot.service.ChatbotService;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.model.ChatResponse;

@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Chat")
public class ChatView extends VerticalLayout {
    private final ChatbotService chatbotService;
    private final MessageList messageList = new MessageList();
    private final List<MessageListItem> messages = new ArrayList<>();
    private final String conversationId = UUID.randomUUID().toString();
    private Instant lastMessageTime;

    public ChatView(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
        setWidth("100%");
        setHeight(null); // Allow height to be determined by content
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        
        // Configure message list
        messageList.setWidth("100%");
        messageList.setHeight(null); // Allow height to be determined by content
        
        // Configure message input
        MessageInput messageInput = new MessageInput();
        messageInput.setWidth("100%");
        messageInput.getStyle()
                .set("margin-top", "var(--lumo-space-s)")
                .set("max-width", "800px"); // Limit width for better readability

        // Create container for chat components
        VerticalLayout chatContainer = new VerticalLayout(messageList, messageInput);
        chatContainer.setWidth("100%");
        chatContainer.setHeight(null);
        chatContainer.setPadding(false);
        chatContainer.setSpacing(false);
        chatContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        chatContainer.setMaxWidth("800px"); // Limit width for better readability

        messageInput.addSubmitListener(submitEvent -> {
            String userMessage = submitEvent.getValue();
            lastMessageTime = Instant.now();
            addUserMessage(userMessage);
            getBotResponse(userMessage);
        });

        add(chatContainer);
    }

    private void addUserMessage(String text) {
        MessageListItem message = new MessageListItem(text, Instant.now(), "You");
        message.setUserColorIndex(1);
        messages.add(message);
        messageList.setItems(messages);
    }

    private void getBotResponse(String userMessage) {
        ChatResponse chatResponse = chatbotService.chat(conversationId, userMessage);

        String response = chatResponse.getResult().getOutput().getText();
        Instant now = Instant.now();
        double elapsedSeconds = Duration.between(lastMessageTime, now).toMillis() / 1000.0;
        long totalTokens = chatResponse.getMetadata().getUsage().getGenerationTokens();

        double tokPerSec = totalTokens / elapsedSeconds;
        
        String responseWithTime = String.format(
            "%s\n\n(%.2f tok/sec - %d tokens - Response time: %.2f seconds)"
            , response, tokPerSec, totalTokens, elapsedSeconds);
        MessageListItem botMessage = new MessageListItem(responseWithTime, now, "Bot");
        botMessage.setUserColorIndex(2);
        messages.add(botMessage);
        messageList.setItems(messages);
    }
}