package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MovieInfoService {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> createMovieInfo(MovieInfo movieInfo){
        return movieInfoRepository.save(movieInfo).log();
    }
    public Flux<MovieInfo> getMovieInfos(){
        return movieInfoRepository.findAll().log();
    }

    public Mono<MovieInfo> getMovieInfoById(String movieInfoId) {
        return movieInfoRepository.findById(movieInfoId);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo movieInfo){
        return movieInfoRepository.save(movieInfo);
    }

    public Mono<Void> deleteMovieInfoById(String movieInfoId) {
        log.debug("Received delete request for find movie info by id: {}", movieInfoId);
        return movieInfoRepository.deleteById(movieInfoId).log();
    }

    public Flux<MovieInfo> getMovieInfosByYear(Integer year){
        log.debug("Received get request for find movie infos by year: {}", year);
        return movieInfoRepository.findByYear(year);
    }
}
