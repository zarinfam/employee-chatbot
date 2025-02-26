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
        setSizeFull();
        setSpacing(false);

        messageList.setSizeFull();
        MessageInput messageInput = new MessageInput();
        messageInput.setWidthFull();

        messageInput.addSubmitListener(submitEvent -> {
            String userMessage = submitEvent.getValue();
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
        MessageListItem botMessage = new MessageListItem(response, Instant.now(), "Bot");
        botMessage.setUserColorIndex(2);
        messages.add(botMessage);
        messageList.setItems(messages);
    }
}