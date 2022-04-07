package com.reactivespring.router;

import com.reactivespring.domain.Review;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
class ReviewUnitTest {

    @MockBean
    ReviewRepository reviewRepository;

    @Autowired
    private WebTestClient client;

    private List<Review> reviewsList;

    static String REVIEWS_URL = "/v1/review";

    @Test
    @SneakyThrows
    void getAllReview(){
        reviewsList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review(null, 2L, "Excellent Movie", 8.0));
        Flux<Review> reviewFlux = Flux.fromIterable(reviewsList);

        when(reviewRepository.findAll()).thenReturn(reviewFlux);

        client.get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class)
                .value(reviews -> assertEquals(reviewsList.size(),reviews.size()));
    }

    @Test
    @SneakyThrows
    void deleteReview(){
        String deleteReviewId = "delete_review_test";
        Review review = new Review(deleteReviewId, 1L, "Awesome Movie", 9.0);

        when(reviewRepository.findById(eq(deleteReviewId))).thenReturn(Mono.just(review));
        when(reviewRepository.delete(eq(review))).thenReturn(Mono.empty());

        client.delete()
                .uri(REVIEWS_URL + "/{id}", deleteReviewId)
                .exchange()
                .expectStatus().isNoContent();

        verify(reviewRepository, times(1)).findById(deleteReviewId);
        verify(reviewRepository, times(1)).delete(review);
        verifyNoMoreInteractions(reviewRepository);
    }
}