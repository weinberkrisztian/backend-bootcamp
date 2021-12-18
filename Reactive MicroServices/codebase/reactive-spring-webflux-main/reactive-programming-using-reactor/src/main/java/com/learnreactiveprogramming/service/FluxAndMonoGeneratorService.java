package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); // db or remote service call
    }

    public Flux<String> namesFluxMap(){
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")).map(String::toUpperCase);
    }

    public Flux<String> namesFluxImmutable(){
        // the operator retunr a new value every time, so either chain them or reference the same object
        Flux<String> stringFlux = Flux.fromIterable(List.of("Alex", "Ben", "Chloe")).map(str -> str.concat(" Immutable"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFluxFlatMap(){
        Flux<String> stringFlux = Flux.fromIterable(List.of(List.of("elso", "masodik"), List.of("harmadik", "negyedik"))).flatMap(strings -> {
            var millis = new Random().nextInt(1000);
            System.out.println(millis);
            return Flux.fromIterable(strings)
                    .delayElements(Duration.ofMillis(millis));
        });
        return stringFlux.log();
    }

    public Flux<String> namesFluxFlatMapLetters(){
        return Flux.fromIterable(List.of("ALEX"))
                .flatMap(str -> {
                    var millis = new Random().nextInt(1000);
                    System.out.println(millis);
                    var split = str.split("");
                    return Flux.fromArray(split)
                            .delayElements(Duration.ofMillis(millis));
                } ).log();
    }

    public Mono<String> nameMono(){
        return Mono.just("Valentina").log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService self = new FluxAndMonoGeneratorService();
//        self.namesFlux().subscribe(name -> System.out.println("My flux name is: " + name));
//        self.nameMono().subscribe(name -> System.out.println("My mono name is: " + name));
//        self.namesFluxMap().subscribe(name -> System.out.println("My flux name is in uppercase: " + name));
//        self.namesFluxImmutable().subscribe(name -> System.out.println("My immutable flux name is: " + name));
//        self.namesFluxFlatMap().subscribe(name -> System.out.println("My flat delayed flux name is: " + name));
    }
}
