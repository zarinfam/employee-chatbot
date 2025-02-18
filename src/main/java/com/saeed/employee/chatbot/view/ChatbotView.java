package com.saeed.employee.chatbot.view;

import com.saeed.employee.chatbot.service.ChatbotService;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.firitin.components.messagelist.MarkdownMessage;
import org.vaadin.firitin.components.messagelist.MarkdownMessage.Color;

import java.util.Optional;
import java.util.UUID;

@Route("")
public class ChatbotView extends VerticalLayout {

    public ChatbotView(ChatbotService chatService) {
        String chatId = UUID.randomUUID().toString();
        ;
        var messageList = new VerticalLayout();
        var messageInput = new MessageInput();
        messageInput.setWidthFull();
        messageInput.addSubmitListener(
                e -> submitPromptListener(chatId, e, chatService, messageList));
        addClassName("centered-content");
        add(messageList, messageInput);
    }

    private static void submitPromptListener(
            String chatId,
            MessageInput.SubmitEvent e,
            ChatbotService chatService,
            VerticalLayout messageList) {
        var question = e.getValue();
        var userMessage = new MarkdownMessage(question, "You", Color.AVATAR_PRESETS[6]);
        var assistantMessage = new MarkdownMessage("Assistant", Color.AVATAR_PRESETS[0]);
        messageList.add(userMessage, assistantMessage);
        chatService
                .chatAsync(chatId, question)
                .map(res -> Optional.ofNullable(res.getResult().getOutput().getText())
                        .orElse(""))
                .subscribe(assistantMessage::appendMarkdownAsync);
    }
}
