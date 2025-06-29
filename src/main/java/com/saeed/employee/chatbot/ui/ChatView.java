package com.saeed.employee.chatbot.ui;

import com.saeed.employee.chatbot.service.ChatbotService;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Chat")
public class ChatView extends VerticalLayout {
    private final MessageList messageList = new MessageList();
    private final List<MessageListItem> messages = new ArrayList<>();
    private final String conversationId = UUID.randomUUID().toString();
    private final ProgressBar progressBar;
    private final MessageInput messageInput;
    private StringBuilder currentResponse = new StringBuilder();

    public ChatView(ChatbotService chatbotService) {
        setWidth("100%");
        setHeight(null);
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        
        // Configure message list
        messageList.setWidth("100%");
        messageList.setHeight(null);
        
        // Configure progress bar
        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setWidth("100%");
        progressBar.getStyle()
                .set("margin", "var(--lumo-space-s) 0")
                .set("max-width", "800px");
        
        // Configure message input
        messageInput = new MessageInput();
        messageInput.setWidth("100%");
        messageInput.getStyle()
                .set("margin-top", "var(--lumo-space-s)")
                .set("max-width", "800px");

        // Create container for chat components
        VerticalLayout chatContainer = new VerticalLayout(messageList, progressBar, messageInput);
        chatContainer.setWidth("100%");
        chatContainer.setHeight(null);
        chatContainer.setPadding(false);
        chatContainer.setSpacing(false);
        chatContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        chatContainer.setMaxWidth("800px");

        messageInput.addSubmitListener(submitEvent -> {
            String userMessage = submitEvent.getValue();
            currentResponse.setLength(0); // Reset current response
            addUserMessage(userMessage);
            
            // Set UI to loading state
            setUiLoadingState(true);
            
            // Get current UI instance for async updates
            UI ui = getUI().orElseThrow();
            
            // Use chatAsync for streaming response
            final var startTime = System.currentTimeMillis();
            final var totalTokens = new AtomicLong(0);
            chatbotService.chatAsync(conversationId, userMessage)
                    .doOnComplete(() -> {
                        ui.access(() -> {
                            final var endTime = System.currentTimeMillis();
                            double elapsedSeconds = (double) (endTime - startTime) / 1000;
                            double tokPerSec = totalTokens.get() / elapsedSeconds;

                            // Add response time to the last bot message
                            if (!messages.isEmpty()) {
                                MessageListItem lastMessage = messages.get(messages.size() - 1);
                                String updatedContent = String.format(
                                    "%s\n\n(%.2f tok/sec - %d tokens - Response time: %.2f seconds)"
                                    , lastMessage.getText(), tokPerSec, totalTokens.get(), elapsedSeconds);
                                lastMessage.setText(updatedContent);
                                messageList.setItems(messages);
                            }
                            setUiLoadingState(false);
                        });
                    })
                    .doOnError(error -> {
                        ui.access(() -> {
                            setUiLoadingState(false);
                            Notification.show("Error: " + error.getMessage(),
                                    5000,
                                    Notification.Position.TOP_CENTER)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        });
                    })
                    .subscribe(response -> {
                        ui.access(() -> {
                            // For streaming responses, update or add bot message
                            totalTokens.set(response.getMetadata().getUsage().getCompletionTokens());
                            response.getResults().forEach(result -> {
                                String content = result.getOutput().getText();
                                currentResponse.append(content);
                            });
                            
                            if (!messages.isEmpty() && messages.get(messages.size() - 1).getUserName().equals("Bot")) {
                                // Update existing bot message
                                MessageListItem lastMessage = messages.get(messages.size() - 1);
                                lastMessage.setText(currentResponse.toString());
                            } else {
                                // Add new bot message
                                MessageListItem botMessage = new MessageListItem(
                                    currentResponse.toString(), Instant.now(), "Bot");
                                botMessage.setUserColorIndex(2);
                                messages.add(botMessage);
                            }
                            messageList.setItems(messages);
                        });
                    });
        });

        add(chatContainer);
    }

    private void setUiLoadingState(boolean loading) {
        messageInput.setEnabled(!loading);
        progressBar.setVisible(loading);
    }

    private void addUserMessage(String text) {
        MessageListItem message = new MessageListItem(text, Instant.now(), "You");
        message.setUserColorIndex(1);
        messages.add(message);
        messageList.setItems(messages);
    }
}