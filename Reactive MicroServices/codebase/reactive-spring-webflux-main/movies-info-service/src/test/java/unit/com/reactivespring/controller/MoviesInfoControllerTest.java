package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.reactivespring.controller.MoviesInfoControllerTest.MOVIE_INFO_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    MovieInfoService movieInfoService;

    @Test
    @SneakyThrows
    void callFindAllMovieInfosAndReturnAll(){
        Flux<MovieInfo> movieInfoFlux = Flux.fromIterable(getMovieInfos());
        when(movieInfoService.getMovieInfos()).thenReturn(movieInfoFlux);

        client.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    @SneakyThrows
    void callGetFindByIdAndReturnMovieInfo(){
        MovieInfo movieInfo = getMovieInfos().get(0);
        String movieInfoId = movieInfo.getMovieInfoId();
        Mono<MovieInfo> monoMovieInfo = Mono.just(movieInfo);
        when(movieInfoService.getMovieInfoById(movieInfoId)).thenReturn(monoMovieInfo);

        client.get()
                .uri(MOVIE_INFO_URL + "/" + movieInfoId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals(movieInfoId, responseBody.get(0).getMovieInfoId());
                })
                .hasSize(1);
    }

    @Test
    @SneakyThrows
    void callPostCreateMovieInfoAndAssertResponseBodyAndReturnCREATED(){
        MovieInfo movieInfo = new MovieInfo("123", "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        Mono<MovieInfo> monoMovieInfo = Mono.just(movieInfo);
        when(movieInfoService.createMovieInfo(movieInfo)).thenReturn(monoMovieInfo);

        client.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult ->{
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody.getMovieInfoId());
                });
    }

    @Test
    @SneakyThrows
    void callPostCreateMovieInfoWithNegativeYearValueAndReturnValidationMessage(){
        MovieInfo movieInfo = new MovieInfo("123", "Batman Begins",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));
        Mono<MovieInfo> monoMovieInfo = Mono.just(movieInfo);
        when(movieInfoService.createMovieInfo(movieInfo)).thenReturn(monoMovieInfo);

        client.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(movieInfoEntityExchangeResult ->{
                    String responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(responseBody).contains("movieInfo.year must be positive!");
                    assert Objects.requireNonNull(responseBody).contains("movieInfo.cast can not be blank!");
                });
    }

    @Test
    @SneakyThrows
    void callPutUpdateMovieInfoAndAssertUpdatedValuesAndReturnOK(){
        MovieInfo movieInfo = getMovieInfos().get(0);
        String updatedName = "UPDATED_NAME";
        movieInfo.setName(updatedName);
        Mono<MovieInfo> monoMovieInfo = Mono.just(movieInfo);

        when(movieInfoService.updateMovieInfo(movieInfo)).thenReturn(monoMovieInfo);

        client.put()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody!= null;
                    assertEquals(updatedName, responseBody.get(0).getName());
                })
                .hasSize(1);
    }

    List<MovieInfo> getMovieInfos() {
        return List.of(new MovieInfo("123", "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo("456", "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
    }
}