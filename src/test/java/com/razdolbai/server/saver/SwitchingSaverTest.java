package com.razdolbai.server.saver;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.*;


public class SwitchingSaverTest {

    LocalDateTime messageDateTime = null;

    @Before
    public void beforeTest() {
        messageDateTime = LocalDateTime.now();
    }

    @Test
    public void shouldSaveAndNotSwitchIfLimitWasntReachedAndSameDate() throws IOException {
        File file = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        file.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver();

        sut.save("hi", messageDateTime);
        sut.save("hello", messageDateTime);
        sut.close();

        String filename = SwitchingFileSaver.fileNameFormat("history", messageDateTime,0);


        BufferedReader reader = new BufferedReader(new FileReader(filename));

        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "hi")).isTrue();
        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "hello")).isTrue();

    }


}
