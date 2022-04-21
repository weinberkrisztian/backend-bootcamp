package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler handler) {

        return route()
                .nest(path("/v1/review"), builder ->
                        builder.GET("", request -> request.queryParam("movieInfoId")
                                        .map(id -> handler.getReviewsByMovieInfoId(request))
                                        .orElse(handler.listReviews())
                                )
                                .POST("/add", request -> handler.addReview(request))
                                .PUT("/{id}", request -> handler.updateReview(request))
                                .DELETE("/{id}", request -> handler.deleteReview(request))
                                .GET("/{id}", request -> handler.getReviewById(request))
                )
                .GET("/v1/hello", request -> ServerResponse.ok().bodyValue("hello functional web"))
                .build();
    }
}
