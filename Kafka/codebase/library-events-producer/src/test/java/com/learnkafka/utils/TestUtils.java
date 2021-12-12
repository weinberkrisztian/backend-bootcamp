package com.learnkafka.utils;

import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;

public class TestUtils {
    public static LibraryEvent getLibraryEventTestData(){
        return LibraryEvent.builder()
                .id(1)
                .book(Book.builder().id(1).name("testBook").author("testAuthor").build())
                .build();
    }
}
