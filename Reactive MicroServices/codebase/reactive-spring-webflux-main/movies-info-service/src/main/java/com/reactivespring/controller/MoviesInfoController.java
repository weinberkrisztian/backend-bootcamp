package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@Slf4j
public class MoviesInfoController {

    public static final String MOVIEINFOS_BASE_URL = "/movieinfos";

    @Autowired
    private MovieInfoService movieInfoService;

    @PostMapping(MOVIEINFOS_BASE_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> createMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
        return movieInfoService.createMovieInfo(movieInfo);
    }

    @GetMapping(MOVIEINFOS_BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getMovieInfos(@RequestParam(value = "year", required = false) Integer year){
        return Optional.ofNullable(year).map(movieInfoService::getMovieInfosByYear)
                .orElseGet(movieInfoService::getMovieInfos);
    }

    @GetMapping(MOVIEINFOS_BASE_URL +"/{movieId}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable("movieId") String movieInfoId){
        log.debug("Received request for find movie info by ID: {}", movieInfoId);
        return movieInfoService.getMovieInfoById(movieInfoId)
                .map(ResponseEntity.status(HttpStatus.OK)::body)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PutMapping(MOVIEINFOS_BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> updateMovieInfoById(@RequestBody @Valid MovieInfo movieInfo){
        log.debug("Received put request for find movie info update: {}", movieInfo);
        return movieInfoService.updateMovieInfo(movieInfo);
    }

    @DeleteMapping(MOVIEINFOS_BASE_URL +"/{movieInfoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfoById(@PathVariable String movieInfoId){
        return movieInfoService.deleteMovieInfoById(movieInfoId);
    }
}
