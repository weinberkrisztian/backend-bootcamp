package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {

        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll(){
       var moveisInfoFlux =  movieInfoRepository.findAll().log();

        StepVerifier.create(moveisInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {

        var movieInfoMono = movieInfoRepository.findById("abc");

        StepVerifier.create(movieInfoMono)
                //.expectNextCount(1)
                .assertNext(movieInfo -> assertEquals("Dark Knight Rises", movieInfo.getName()))
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {

        var movieInfo = new MovieInfo(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var movieInfoMono = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInf -> assertNotNull(movieInf.getMovieInfoId()))
                .verifyComplete();
    }


    @Test
    void updateMovieInfo() {
        var movieInfoMono = movieInfoRepository.findById("abc").log().block();
        movieInfoMono.setName("updated name");

        Mono<MovieInfo> updatedMovieInfo = movieInfoRepository.save(movieInfoMono);

        StepVerifier.create(updatedMovieInfo)
                .assertNext(movieInfo -> assertEquals("updated name", movieInfo.getName()))
                .verifyComplete();
    }

    @Test
    void deleteById() {
        movieInfoRepository.deleteById("abc").log().block();
        var movieInfosFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(movieInfosFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}