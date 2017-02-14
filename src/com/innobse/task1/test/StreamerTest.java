package com.innobse.task1.test;

import com.innobse.task1.Exceptions.GetStreamException;
import com.innobse.task1.Streamer;
import static com.innobse.task1.test.ConstTest.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing Streamer
 *
 */

class StreamerTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getStreamBadScenary() throws GetStreamException {
        //  Проверка на выброс исключения при неверных путях до файла
        for(String str : unknownFiles){
            assertThrows(GetStreamException.class, () -> Streamer.getStream(str));
        }
    }

    @Test
    void getStreamGoodScenary() throws GetStreamException {
        //  Проверка на возврат не null значений при корректных путях
        for(String str : goodFiles){
            assertNotNull(Streamer.getStream(str));
        }
        for(String str : badFiles){
            assertNotNull(Streamer.getStream(str));
        }
    }

}