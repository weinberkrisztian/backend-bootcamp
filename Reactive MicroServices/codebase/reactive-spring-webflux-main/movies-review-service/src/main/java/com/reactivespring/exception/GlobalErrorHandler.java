package com.reactivespring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception message is {}", ex.getMessage(), ex);
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        if(ex instanceof ReviewDataException){
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof ReviewNotFoundException){
            response.setStatusCode(HttpStatus.NOT_FOUND);
        }
        return response.writeWith(Mono.just(errorMessage));
    }
}
