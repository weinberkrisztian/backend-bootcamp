package com.reactivespring.controller;

import com.reactivespring.client.MovieInfoClient;
import com.reactivespring.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movieinfos")
public class MovieInfosController {

    @Autowired
    private MovieInfoClient movieInfoClient;

    @GetMapping("/{id}")
    public Mono<MovieInfo> retrieveMovieInfoById(@PathVariable("id") String movieInfoId){
        Mono<MovieInfo> movieInfoMono = movieInfoClient.retrieveMovieInfoById(movieInfoId);
        return movieInfoMono;
    }
}
