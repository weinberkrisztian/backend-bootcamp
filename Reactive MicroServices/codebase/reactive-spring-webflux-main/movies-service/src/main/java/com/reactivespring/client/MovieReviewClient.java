package com.reactivespring.client;

import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
public class MovieReviewClient {

    private WebClient webClient;

    @Value("${moviereviews.client.url}")
    private String movieReviewsBaseUrl;

    public MovieReviewClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Review> retrieveReviewByMovieInfoId(String movieInfoId){
        String url = UriComponentsBuilder.fromUriString(movieReviewsBaseUrl)
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand().toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class);
    }
}
