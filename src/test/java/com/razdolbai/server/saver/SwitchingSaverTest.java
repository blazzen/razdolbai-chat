package com.razdolbai.server.saver;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.*;


public class SwitchingSaverTest {

    @Test
    public void shouldSaveAndNotSwitchIfLimitWasntReachedAndSameDate() throws IOException {


        LocalDateTime messagesDateTime = LocalDateTime.now();
        File file = new File(SwitchingFileSaver.fileNameFormat("test", messagesDateTime, 0));
        file.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver();




        sut.save("hi", messagesDateTime);
        sut.save("hello", messagesDateTime);
        sut.close();

        String filename = SwitchingFileSaver.fileNameFormat("test", messagesDateTime,0);

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        assertThat(reader.readLine().equals("[" + messagesDateTime.toString() + "]" + "hi")).isTrue();
        assertThat(reader.readLine().equals("[" + messagesDateTime.toString() + "]" + "hello")).isTrue();

    }


}
