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
    public void shouldSaveAndNotSwitchIfLimitWasNotReachedAndSameDate() throws IOException {

        LocalDateTime messagesDateTime = LocalDateTime.now();
        File file = new File(SwitchingFileSaver.fileNameFormat("history", messagesDateTime, 0));

        file.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver();

        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime);
        sut.close();

        String filename = SwitchingFileSaver.fileNameFormat("history", messagesDateTime,0);


        BufferedReader reader = new BufferedReader(new FileReader(filename));

        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test1")).isTrue();
        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test2")).isTrue();

    }

    @Test
    public void shouldCreateNewFileIfFileAlreadyExists() throws IOException {
        File existedFile = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        existedFile.createNewFile();

        SwitchingFileSaver sut = new SwitchingFileSaver();
        sut.save("test1", messageDateTime);
        sut.close();

        String createdFileName = SwitchingFileSaver.fileNameFormat("history", messageDateTime,1);
        BufferedReader reader = new BufferedReader(new FileReader(createdFileName));

        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test1")).isTrue();
    }
}
