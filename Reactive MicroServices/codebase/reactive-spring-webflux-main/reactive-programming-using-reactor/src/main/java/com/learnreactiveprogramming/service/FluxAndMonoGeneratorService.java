package com.learnreactiveprogramming.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); // db or remote service call
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")).map(String::toUpperCase);
    }

    public Flux<String> namesFluxImmutable() {
        // the operator retunr a new value every time, so either chain them or reference the same object
        Flux<String> stringFlux = Flux.fromIterable(List.of("Alex", "Ben", "Chloe")).map(str -> str.concat(" Immutable"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFluxFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(List.of(List.of("elso", "masodik"), List.of("harmadik", "negyedik"))).flatMap(strings -> {
            var millis = new Random().nextInt(1000);
            System.out.println(millis);
            return Flux.fromIterable(strings)
                    .delayElements(Duration.ofMillis(millis));
        });
        return stringFlux.log();
    }

    public Flux<String> namesFluxFlatMapLetters() {
        return Flux.fromIterable(List.of("ELSO", "MASODIK"))
                .map(String::toLowerCase)
                .flatMap(str -> {
                    var millis = new Random().nextInt(1000);
                    System.out.println(millis);
                    var split = str.split("");
                    return Flux.fromArray(split)
                            .delayElements(Duration.ofMillis(millis));
                }).log();
    }

    public Flux<String> nameFluxConcatMapLetters() {
        return Flux.fromIterable(List.of("ELSO", "MASODIK"))
                .map(String::toLowerCase)
                .concatMap(str -> {
                    var millis = new Random().nextInt(1000);
                    System.out.println(millis);
                    var split = str.split("");
                    return Flux.fromArray(split)
                            .delayElements(Duration.ofMillis(millis));
                }).log();
    }

    public Flux<String> nameFluxTransform(int reqLength) {
        Function<Flux<String>, Flux<String>> transformFunction = nameFlux -> {
            return nameFlux
                    .map(String::toLowerCase)
                    .filter(str -> str.length() >= reqLength);
        };

        return Flux.fromIterable(List.of("ELSO", "MASODIK"))
                .transform(transformFunction)
                .flatMap(this::splitStrings)
                .log();
    }

    public Flux<String> nameFluxDefEmpty(int requiredLength) {
        return Flux.fromIterable(List.of("ELSO", "MASODIK"))
                .map(String::toUpperCase)
                .filter(str -> str.length() >= requiredLength)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> nameFluxZip(){
        var firstFlux= Flux.just("A", "B", "C");
        var secondFlux= Flux.just("D", "E", "F");
        var thirdFlux= Flux.just("1", "2", "3");
        var fourthFlux= Flux.just("4", "5", "6");

        return Flux.zip(firstFlux, secondFlux, thirdFlux, fourthFlux)
                .map(t4 -> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4()).log();
    }

    public Flux<String> nameFluxZipAlternative(){
        var firstFlux= Flux.just("A", "B", "C");
        var secondFlux= Flux.just("1", "2", "3");

        return Flux.zip(firstFlux, secondFlux, ((f1, f2) -> f1 + f2)).log();
    }

    public Mono<String> nameMonoFilter(int requiredLength) {
        return Mono.just("Valentina")
                .map(String::toUpperCase)
                .filter(str -> str.length() >= requiredLength)
                .log();
    }

    public Mono<List<String>> nameMonoFlatMap(int requiredLength) {
        return Mono.just("Valentina")
                .map(String::toUpperCase)
                .filter(str -> str.length() >= requiredLength)
                .flatMap(this::splitMonoInput)
                .log();
    }

    public Flux<String> nameMonoFlatMapMany(int requiredLength) {
        return Mono.just("Valentina")
                .map(String::toUpperCase)
                .filter(str -> str.length() >= requiredLength)
                .flatMapMany(this::splitStrings)
                .log();
    }

    private Flux<String> splitStrings(String s) {
        var chars = s.split("");
        return Flux.fromArray(chars);
    }

    private Mono<List<String>> splitMonoInput(String s) {
        var chars = s.split("");
        var charList = List.of(chars);
        return Mono.just(charList);
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService self = new FluxAndMonoGeneratorService();
//        self.namesFlux().subscribe(name -> System.out.println("My flux name is: " + name));
//        self.nameMono().subscribe(name -> System.out.println("My mono name is: " + name));
//        self.namesFluxMap().subscribe(name -> System.out.println("My flux name is in uppercase: " + name));
//        self.namesFluxImmutable().subscribe(name -> System.out.println("My immutable flux name is: " + name));
//        self.namesFluxFlatMap().subscribe(name -> System.out.println("My flat delayed flux name is: " + name));
//        self.nameFluxConcatMapLetters().subscribe(name -> System.out.println("My concat map delayed flux letters is: " + name));
        self.nameMonoFilter(9).subscribe(name -> System.out.println("My filtered mono name is: " + name));
    }
}
