package com.saeed.employee.chatbot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@PWA(name = "Employee Assistant Chatbot", shortName = "Employee Chatbot")
@Theme(value = "chatbot", variant = Lumo.LIGHT)
@Push
@SpringBootApplication
public class EmployeeChatbotApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeChatbotApplication.class, args);
    }
}
