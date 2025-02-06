package com.saeed.employee.chatbot;

import static org.assertj.core.api.Assertions.assertThat;

import com.saeed.employee.chatbot.api.model.UserMessage;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30s")
@Import(EmployeeChatbotTestConfig.class)
@TestMethodOrder(OrderAnnotation.class)
class EmployeeChatbotApplicationTests {

    @Autowired
    private WebTestClient webTestClient;



    private static final String EMPLOYEE_UNKNOWN = "employee_unknown";
    private static final String EMPLOYEE_KNOWN = "employee_known";
    private static final String ANOTHER_UNKNOWN_EMPLOYEE_ID = "employee_unknown2";


    @Test
    public void chatWithUnknownEmployee_ShouldReturnUnknownMessage() {
        // Given:
        String query = "what is my name? Just answer me with I don't know if you don't know and just my name if know";

        // When: we request a chat for an unknown employee
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/" + EMPLOYEE_UNKNOWN).build())
                .body(Mono.just(new UserMessage(query)), UserMessage.class)
                .exchange()
                // Then: the response should indicate the name is unknown
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(result -> assertThat(result.toLowerCase()).contains("i don't know"));
    }

    @Test
    public void chatWithKnownEmployee_ShouldReturnEmployeeName() {
        // Given:
        String setNameQuery = "My name is Deli";
        String query = "what is my name? Just answer me with I don't know if you don't know and just my name if know";

        // Setup:
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/" + EMPLOYEE_KNOWN).build())
                .body(Mono.just(new UserMessage(setNameQuery)), UserMessage.class)
                .exchange()
                .expectStatus().isOk();

        // When:
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/" + EMPLOYEE_KNOWN).build())
                .body(Mono.just(new UserMessage(query)), UserMessage.class)
                .exchange()
                // Then:
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(result -> assertThat(result).contains("Deli"));
    }

//
//    @Test
//    void chatWithUnknownEmployee_whenKnownEmployeeExists_shouldReturnDontKnow() {
//        // Given: A known employee exists (with a different ID).
//        webTestClient
//                .post()
//                .uri(uriBuilder -> uriBuilder.path("/employee/chat/" + EMPLOYEE_KNOWN).build())
//                .body(Mono.just(new UserMessage("My name is Deli")), UserMessage.class)
//                .exchange()
//                .expectStatus().isOk();
//
//        // And: Another employee (with a separate ID) has not been set up.
//        webTestClient
//                .post()
//                .uri(uriBuilder -> uriBuilder.path("/employee/chat/" + ANOTHER_UNKNOWN_EMPLOYEE_ID).build())
//                .body(Mono.just(new UserMessage("what is my name? Just answer me with I don't know if you don't know and just my name if know")), UserMessage.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class)
//                .value(result -> assertThat(result.toLowerCase()).contains("i don't know"));
//    }





    @Test
    @Order(1)
    void chatWithUnknownEmployee_success() {
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/1").build())
                .body(
                        Mono.just(
                                new UserMessage(
                                        "what is my name? Just answer me with I don't know if you don't know and just my name if know")),
                        UserMessage.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(result -> {
                    assertThat(result.toLowerCase()).contains("i don't know");
                });
    }



    @Test
    @Order(2)
    void chatWithKnownEmployee_success() {
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/1").build())
                .body(Mono.just(new UserMessage("My name is Deli")), UserMessage.class)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/1").build())
                .body(
                        Mono.just(
                                new UserMessage(
                                        "what is my name? Just answer me with I don't know if you don't know and just my name if know")),
                        UserMessage.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(result -> {
                    assertThat(result).contains("Deli");
                });
    }

    @Test
    @Order(3)
    void chatWithUnknownEmployee_knownEmployeeExist_success() {
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/employee/chat/2").build())
                .body(
                        Mono.just(
                                new UserMessage(
                                        "what is my name? Just answer me with I don't know if you don't know and just my name if know")),
                        UserMessage.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(result -> {
                    assertThat(result.toLowerCase()).contains("i don't know");
                });
    }
}
