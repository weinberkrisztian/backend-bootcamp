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

        // flat map works asynchronously if another mapping is present before ?
        StepVerifier.create(namedFlux)
//                .expectNext("e","l","s","o","m","a","s","o","d","i","k")
                .expectNextCount(11)
                .verifyComplete();
    }

    @Test
    void nameFluxConcatMapLettersTest(){
        var namedFlux = unitUnderTest.nameFluxConcatMapLetters();

        // concat map keeps the order of the elements
        StepVerifier.create(namedFlux)
                .expectNext("e","l","s","o","m","a","s","o","d","i","k")
                .verifyComplete();
    }

    @Test
    void nameFluxTransformTest(){
        var namedFlux = unitUnderTest.nameFluxTransform(4);

        // concat map keeps the order of the elements
        StepVerifier.create(namedFlux)
                .expectNext("e","l","s","o","m","a","s","o","d","i","k")
                .verifyComplete();
    }

    @Test
    void nameMonoFlatMapTest(){
        var nameMono = unitUnderTest.nameMonoFlatMap(9);

        StepVerifier.create(nameMono)
                .expectNext(List.of("V","A","L","E","N","T","I","N","A"))
                .verifyComplete();
    }

    @Test
    void nameMonoFlatMapManyTest(){
        var nameMono = unitUnderTest.nameMonoFlatMapMany(9);

        StepVerifier.create(nameMono)
                .expectNext("V","A","L","E","N","T","I","N","A")
                .verifyComplete();
    }

    @Test
    void nameFluxDefaultEmptyTest(){
        var namedFlux = unitUnderTest.nameFluxDefEmpty(10);

        // input is filtered by length therefore default value is returned
        StepVerifier.create(namedFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void nameFluxZipTest(){
        var namedFlux = unitUnderTest.nameFluxZip();

        var namedFluxAlternative = unitUnderTest.nameFluxZipAlternative();

        // zip merges the values together
        StepVerifier.create(namedFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();

        StepVerifier.create(namedFluxAlternative)
                .expectNext("A1", "B2", "C3")
                .verifyComplete();
    }
}