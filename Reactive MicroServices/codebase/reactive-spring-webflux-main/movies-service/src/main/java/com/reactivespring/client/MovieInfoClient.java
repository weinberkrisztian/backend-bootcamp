package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class MovieInfoClient {

    private WebClient webClient;

    @Value("${movieinfos.client.url}")
    private String movieInfoBaseUrl;

    public MovieInfoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> retrieveMovieInfoById(String movieInfoId){
        var url = movieInfoBaseUrl + "/{id}";

        return webClient.get()
                .uri(url, movieInfoId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.info("Status code is: {}", clientResponse.statusCode().value());
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
                        return Mono.error(new MoviesInfoClientException("There is no movie info with the given ID: " + movieInfoId, clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new MoviesInfoClientException(responseMessage, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.info("Status code is: {}", clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new MoviesInfoClientException("Server exception in MovieInfoService " + responseMessage, clientResponse.statusCode().value())));
                })
                .bodyToMono(MovieInfo.class)
                .retryWhen(Retry.fixedDelay(4, Duration.ofSeconds(2))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));
    }
}
