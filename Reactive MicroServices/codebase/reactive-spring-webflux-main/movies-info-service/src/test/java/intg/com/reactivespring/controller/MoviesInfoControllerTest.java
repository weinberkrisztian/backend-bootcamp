package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    @Autowired
    WebTestClient client;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    List<MovieInfo> movieInfos;

    public static final String MOVIE_INFO_URL = "/v1/movieinfos";

    @BeforeEach
    void setUp() {


        movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo("efg", "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2008, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    @SneakyThrows
    void callCreateMovieInfoEndpointAndReturnTheNexEntity() {
        client.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfos.get(0))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody.getMovieInfoId());
                });
    }

    @Test
    @SneakyThrows
    void callGetMovieInfosEndpointAndReturnAllTestData() {
        client.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    @SneakyThrows
    void callGetMovieInfosEndpointByYearAndReturnOneRelatedTestData() {
        Integer year = 2005;

        client.get()
                .uri(uriBuilder ->
                        uriBuilder.path(MOVIE_INFO_URL)
                                .queryParam("year", year).build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    assert responseBody.size() == 1;
                    MovieInfo movieInfo = responseBody.get(0);
                    assertEquals(movieInfos.get(0).getMovieInfoId(), movieInfo.getMovieInfoId());
                })
                .hasSize(1);
    }

    @Test
    @SneakyThrows
    void callGetMovieInfosEndpointByYearAndReturnTwoRelatedTestData() {
        Integer year = 2008;

        URI uri = UriComponentsBuilder.fromUriString(MOVIE_INFO_URL)
                .queryParam("year", year).buildAndExpand().toUri();

        client.get()
                .uri(uri)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    assert responseBody.size() == 2;
                    MovieInfo movieInfoFirst = responseBody.get(0);
                    MovieInfo movieInfoSecond = responseBody.get(1);
                    assertEquals(movieInfos.get(1).getMovieInfoId(), movieInfoFirst.getMovieInfoId());
                    assertEquals(movieInfos.get(2).getMovieInfoId(), movieInfoSecond.getMovieInfoId());
                })
                .hasSize(2);
    }

    @Test
    @SneakyThrows
    void callGetMovieInfosByIdEndpointAndReturnTestDataById() {
        String requestedMovieInfoId = movieInfos.get(0).getMovieInfoId();
        client.get()
                .uri(MOVIE_INFO_URL + "/" + requestedMovieInfoId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals(requestedMovieInfoId, responseBody.get(0).getMovieInfoId());
                })
                .hasSize(1);
    }

    @Test
    @SneakyThrows
    void callGetMovieInfosByIdEndpointAndReturnNotFound() {
        String requestedMovieInfoId = "not_found_id";
        client.get()
                .uri(MOVIE_INFO_URL + "/" + requestedMovieInfoId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @SneakyThrows
    void callPutMovieInfoEndPointAndUpdateValueAndResponseOK() {
        MovieInfo movieInfo = movieInfos.get(0);
        String updatedName = "UPDATED_NAME";
        movieInfo.setName(updatedName);
        client.put()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals(updatedName, responseBody.get(0).getName());
                })
                .hasSize(1);
    }

    @Test
    @SneakyThrows
    void deleteMovieInfoByIdAndAssertDeletion() {
        String movieInfoId = movieInfos.get(0).getMovieInfoId();
        client.delete()
                .uri(MOVIE_INFO_URL + "/" + movieInfoId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }
}