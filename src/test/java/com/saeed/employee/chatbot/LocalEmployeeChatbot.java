package com.saeed.employee.chatbot;

import org.springframework.boot.SpringApplication;

public class LocalEmployeeChatbot {

    public static void main(String[] args) {
        SpringApplication.from(EmployeeChatbotApplication::main)
                .with(EmployeeChatbotTestConfig.class)
                .run(args);
    }
}
