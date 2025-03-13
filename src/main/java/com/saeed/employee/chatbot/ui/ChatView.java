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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Chat")
public class ChatView extends VerticalLayout {
    private final ChatbotService chatbotService;
    private final MessageList messageList = new MessageList();
    private final List<MessageListItem> messages = new ArrayList<>();
    private final String conversationId = UUID.randomUUID().toString();

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
        String response = chatbotService.chat(conversationId, userMessage);

        MessageListItem botMessage = new MessageListItem(response, Instant.now(), "Bot");
        botMessage.setUserColorIndex(2);
        messages.add(botMessage);
        messageList.setItems(messages);
    }
}