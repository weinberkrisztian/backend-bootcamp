package com.reactivespring.router;

import com.reactivespring.non_related.FunctionalTestRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NonRelatedTest {

    @Test
    void nonRelatedFunctionalPractice(){

        FunctionalTestRunner main = new FunctionalTestRunner();

        String customizedText = main.runFunctionalStringMapper("test");

        assertEquals("TEST_FUNCTIONAL", customizedText);
    }
}
