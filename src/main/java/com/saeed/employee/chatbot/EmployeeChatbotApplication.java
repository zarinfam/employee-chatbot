package com.saeed.employee.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;

@Push
@PWA(name = "Employee Assistant Chatbot", shortName = "Employee Chatbot")
@SpringBootApplication
public class EmployeeChatbotApplication  implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeChatbotApplication.class, args);
    }
}
