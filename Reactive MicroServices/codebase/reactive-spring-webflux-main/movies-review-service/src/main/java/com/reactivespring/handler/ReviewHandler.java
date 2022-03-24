package com.reactivespring.handler;

import com.mongodb.internal.connection.Server;
import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    @Autowired
    private ReviewRepository reviewRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .flatMap(reviewRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    public Mono<ServerResponse> listReviews() {
        var reviewFlux = reviewRepository.findAll();
        return ServerResponse.ok().body(reviewFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        String reviewId = request.pathVariable("id");

        Mono<Review> review = reviewRepository.findById(reviewId);

        return review.flatMap(rev -> request.bodyToMono(Review.class)
                .map(reqRev -> {
                    reqRev.setReviewId(reviewId);
                    return reqRev;
                })
                .flatMap(reviewRepository::save)
                .flatMap(updatedReview -> ServerResponse.status(HttpStatus.OK).bodyValue(updatedReview))
                .switchIfEmpty(notFound));
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        String reviewId = request.pathVariable("id");

        return reviewRepository.findById(reviewId)
                        .flatMap(review -> reviewRepository.delete(review))
                                .then(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    public Mono<ServerResponse> getReviewById(ServerRequest request) {
        String reviewId = request.pathVariable("id");

        return reviewRepository.findById(reviewId)
                .flatMap(ServerResponse.status(HttpStatus.OK)::bodyValue)
                .switchIfEmpty(notFound);
    }
}
