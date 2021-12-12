package com.learnkafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.utils.TestUtils;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;
    @InjectMocks
    LibraryEventProducer libraryEventProducer;
    @Spy
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void libraryEventProducerSendLibraryEventSpecificTopicFailure(){
        LibraryEvent libraryEventTestData = TestUtils.getLibraryEventTestData();
        // mock listenableFuture
        SettableListenableFuture futureTest = new SettableListenableFuture();

        // setup failure
        futureTest.setException(new RuntimeException("Exception Calling Kafka"));
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(futureTest);

        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEventSpecificTopic(libraryEventTestData).get());
    }
}
