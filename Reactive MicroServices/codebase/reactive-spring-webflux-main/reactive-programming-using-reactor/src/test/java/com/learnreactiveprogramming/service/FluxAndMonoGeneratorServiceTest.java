package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService unitUnderTest = new FluxAndMonoGeneratorService();

    @Test
    void nameFluxTest() {
        var names = unitUnderTest.namesFlux();

        StepVerifier.create(names)
                .expectNextCount(3)
                .verifyComplete();

        StepVerifier.create(names)
                .expectNext("Alex", "Ben", "Chloe")
                .verifyComplete();

        StepVerifier.create(names)
                .expectNext("Alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void nameFluxMapTest(){
        var names = unitUnderTest.namesFluxMap();

        StepVerifier.create(names)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void nameFluxFlatMapTest(){
        var namedFlux = unitUnderTest.namesFluxFlatMap();

        StepVerifier.create(namedFlux)
                .expectNext("elso", "masodik", "harmadik", "negyedik")
                .verifyComplete();
    }

    @Test
    void nameFluxFlatMapLettersTest(){
        var namedFlux = unitUnderTest.namesFluxFlatMapLetters();

        StepVerifier.create(namedFlux)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }
}