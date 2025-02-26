package com.saeed.employee.chatbot.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Employee Chatbot")
public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 title = new H1("Employee Chatbot");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(title);
    }
}