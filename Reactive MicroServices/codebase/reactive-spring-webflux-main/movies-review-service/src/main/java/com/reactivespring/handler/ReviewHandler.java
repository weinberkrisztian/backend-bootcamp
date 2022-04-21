package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.exception.ReviewNotFoundException;
import com.reactivespring.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ReviewHandler {

    @Autowired
    private Validator validator;

    @Autowired
    private ReviewRepository reviewRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .doOnNext(this::validate)
                .flatMap(reviewRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(Review review) {
        Set<ConstraintViolation<Review>> constrains = validator.validate(review);
        log.info("constrainViolations {}:", constrains);
        if(constrains.size() > 0){
            String errorMessages = constrains.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(errorMessages);
        }
    }

    public Mono<ServerResponse> listReviews() {
        var reviewFlux = reviewRepository.findAll();
        return ServerResponse.ok().body(reviewFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        String reviewId = request.pathVariable("id");

        Mono<Review> review = reviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not found with the following id " + reviewId)));

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

    public Mono<ServerResponse> getReviewsByMovieInfoId(ServerRequest request){
        var movieInfoId = request.queryParam("movieInfoId");
        Flux<Review> movieReviews;
        if(movieInfoId.isPresent()){
            movieReviews = reviewRepository.findByMovieInfoId(Long.valueOf(movieInfoId.get()));
        } else {
            movieReviews = reviewRepository.findAll();
        }
        return ServerResponse.ok().body(movieReviews, Review.class);
    }
}
