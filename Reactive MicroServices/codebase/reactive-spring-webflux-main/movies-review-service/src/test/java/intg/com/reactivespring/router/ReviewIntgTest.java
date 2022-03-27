package com.reactivespring.router;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewIntgTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    ReviewRepository reviewRepository;

    static String REVIEWS_URL = "/v1/review";

    List<Review> reviewsList;
    @BeforeEach
    void setUp() {

        reviewsList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review(null, 2L, "Excellent Movie", 8.0));
        reviewRepository.saveAll(reviewsList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll()
                .block();
    }

    @Test
    @SneakyThrows
    void addReview() {

        Review testReview = new Review("10", 1L, "Awesome Movie", 9.0);

        client.post()
                .uri(REVIEWS_URL + "/add")
                .bodyValue(testReview)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    Review responseReview = reviewEntityExchangeResult.getResponseBody();
                    Mono<Review> byId = reviewRepository.findById(responseReview.getReviewId());
                    assert Objects.requireNonNull(byId).block() != null;
                });

    }

    @Test
    @SneakyThrows
    void getAllReviews() {
        client.get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class)
                .value(reviews -> assertEquals(3, reviews.size()));
    }

    @Test
    @SneakyThrows
    void updateReview() {

        Review review = new Review(null, 1L, "Awesome Movie", 9.0);
        Review savedReview = reviewRepository.save(review).block();
        Review reviewRequest = new Review(null, 1L, "Awesome Movie Update", 9.0);

        client.put()
                .uri(REVIEWS_URL + "/{id}", savedReview.getReviewId())
                .bodyValue(reviewRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    Review responseBody = reviewEntityExchangeResult.getResponseBody();
                    assertEquals(reviewRequest.getComment(), Objects.requireNonNull(responseBody).getComment());
                });
    }

    @Test
    @SneakyThrows
    void deleteReview(){
        Review review = new Review(null, 1L, "Awesome Movie", 9.0);
        Review savedReview = reviewRepository.save(review).block();

        client.delete()
                .uri(REVIEWS_URL + "/{id}", savedReview.getReviewId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @SneakyThrows
    void getReviewById(){
        Review review = reviewsList.get(0);
        String expectedReviewId = reviewRepository.findById(review.getReviewId()).map(rev -> rev.getReviewId()).block();
        client.get()
                .uri(REVIEWS_URL + "/{id}", expectedReviewId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    Review responseBody = reviewEntityExchangeResult.getResponseBody();
                    assertEquals(review.getComment(), Objects.requireNonNull(responseBody).getComment());
                });
    }

}
