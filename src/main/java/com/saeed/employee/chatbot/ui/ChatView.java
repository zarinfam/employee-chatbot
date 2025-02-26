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
        setSizeFull();
        setSpacing(false);

        messageList.setSizeFull();
        MessageInput messageInput = new MessageInput();
        messageInput.setWidthFull();

        messageInput.addSubmitListener(submitEvent -> {
            String userMessage = submitEvent.getValue();
            lastMessageTime = Instant.now();
            addUserMessage(userMessage);
            getBotResponse(userMessage);
        });

        add(messageList, messageInput);
    }

    private void addUserMessage(String text) {
        MessageListItem message = new MessageListItem(text, Instant.now(), "You");
        message.setUserColorIndex(1);
        messages.add(message);
        messageList.setItems(messages);
    }

    private void getBotResponse(String userMessage) {
        String response = chatbotService.chat(conversationId, userMessage);
        Instant now = Instant.now();
        double elapsedSeconds = Duration.between(lastMessageTime, now).toMillis() / 1000.0;
        
        String responseWithTime = String.format("%s\n\n(Response time: %.2f seconds)", response, elapsedSeconds);
        MessageListItem botMessage = new MessageListItem(responseWithTime, now, "Bot");
        botMessage.setUserColorIndex(2);
        messages.add(botMessage);
        messageList.setItems(messages);
    }
}