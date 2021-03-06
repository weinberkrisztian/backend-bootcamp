package com.learnkafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;
import com.learnkafka.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @Test
    @SneakyThrows
    void shouldTakeLibraryEventDataAndReturnWithCreatedStatus(){
        LibraryEvent libraryEventTestData = TestUtils.getLibraryEventTestData();

        String jsonTestData = om.writeValueAsString(libraryEventTestData);
        doNothing().when(libraryEventProducer).sendLibraryEventSpecificTopic(libraryEventTestData);

        mockMvc.perform(post("/v1/libraryevent")
                .content(jsonTestData)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void shouldTakeLibraryEventDataWithBookNullAndFail(){
        LibraryEvent libraryEventTestData = TestUtils.getLibraryEventWithBookNullTestData();

        String jsonTestData = om.writeValueAsString(libraryEventTestData);
        doNothing().when(libraryEventProducer).sendLibraryEventSpecificTopic(libraryEventTestData);

        mockMvc.perform(post("/v1/libraryevent")
                .content(jsonTestData)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void shouldTakeLibraryEventDataWithMissingBookIdAndNameAndFail(){
        LibraryEvent libraryEventTestData = TestUtils.getLibraryEventWithMissingBookIdAndNameTestData();
        String errorMessage= "book.id - must not be nullbook.name - must not be blank";
        String jsonTestData = om.writeValueAsString(libraryEventTestData);
        doNothing().when(libraryEventProducer).sendLibraryEventSpecificTopic(libraryEventTestData);

        mockMvc.perform(post("/v1/libraryevent")
                .content(jsonTestData)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError())
                .andExpect(content().string(errorMessage));
    }
}
