package com.saeed.employee.chatbot;

import static org.assertj.core.api.Assertions.assertThat;

import com.saeed.employee.chatbot.api.model.UserMessage;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "300s")
@Import(EmployeeChatbotTestConfig.class)
@TestMethodOrder(OrderAnnotation.class)
class EmployeeChatbotApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void askAboutRemoteWork_success() {
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/1").build())
                .body(
                        Mono.just(new UserMessage("Is remote work allowed in the company?")),
                        UserMessage.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(result ->
                        assertThat(result).contains("Remote work requires manager approval"));
    }
}
