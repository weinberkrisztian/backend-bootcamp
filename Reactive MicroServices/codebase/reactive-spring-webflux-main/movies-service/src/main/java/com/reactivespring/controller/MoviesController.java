package com.reactivespring.controller;

import com.reactivespring.client.MovieInfoClient;
import com.reactivespring.client.MovieReviewClient;
import com.reactivespring.domain.Movie;
import com.reactivespring.domain.Review;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

    private MovieInfoClient movieInfoClient;
    private MovieReviewClient movieReviewClient;

    public MoviesController(MovieInfoClient movieInfoClient, MovieReviewClient movieReviewClient) {
        this.movieInfoClient = movieInfoClient;
        this.movieReviewClient = movieReviewClient;
    }

    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieByMovieInfoId(@PathVariable("id") String movieInfoId) {
        return movieInfoClient.retrieveMovieInfoById(movieInfoId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = movieReviewClient.retrieveReviewByMovieInfoId(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
