package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter{

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler handler) {

        return route()
                .GET("/v1/hello", request -> ServerResponse.ok().bodyValue("hello functional web"))
                .GET("/v1/review", request -> handler.listReviews())
                .POST("/v1/review", request -> handler.addReview(request))
                .build();
    }
}
