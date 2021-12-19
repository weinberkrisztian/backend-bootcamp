package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    void shouldCallBasicFluxAndReturnOkAndThreeInteger_firstApproach() {
        client.get()
                .uri("/v1/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void shouldCallBasicFluxAndReturnOkAndThreeInteger_secondApproach() {
        var resp = client.get()
                .uri("/v1/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(resp)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    void shouldCallBasicFluxAndReturnOkAndThreeInteger_thirdApproach() {
        client.get()
                .uri("/v1/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    assert listEntityExchangeResult.getResponseBody().size() == 3;
                });
    }

    @Test
    void shouldCallBasicMonoAndReturnOkAndThreeInteger_thirdApproach() {
        client.get()
                .uri("/v1/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(entityExchangeResult -> {
                    assert entityExchangeResult.getResponseBody().equals("hello-world-mono");
                });
    }

    @Test
    void shouldCallBasicStreamAndReturnOkAndVerifyTheFirstFourResponseValue_secondApproach() {
        var resp = client.get()
                .uri("/v1/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(resp)
                .expectNext(0L,1L,2L,3L)
                .thenCancel()
                .verify();
    }
}