package com.razdolbai.server.saver;

import org.junit.Before;
import org.junit.Ignore;
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
        String folder = "./resources/History";
        for(File f : new File(folder).listFiles()) {
            if(!f.isDirectory()) {
                f.delete();
            }
        }


        messageDateTime = LocalDateTime.now();
    }

    @Test @Ignore
    public void shouldSaveAndNotSwitchIfLimitWasNotReachedAndSameDate() throws IOException {
        File file = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        file.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver();

        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime);
        sut.close();



        String filename = SwitchingFileSaver.fileNameFormat("history", messageDateTime,0);

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test1")).isTrue();
        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test2")).isTrue();

    }

    @Test @Ignore
    public void shouldCreateNewFileIfFileAlreadyExists() throws IOException {
        File existedFile = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        existedFile.delete();
        existedFile.createNewFile();

        String filenameToBeCreated = SwitchingFileSaver.fileNameFormat("history", messageDateTime,1);
        File fileToBeCreated = new File(filenameToBeCreated);
        fileToBeCreated.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver();
        sut.save("test1", messageDateTime);
        sut.close();

        BufferedReader reader = new BufferedReader(new FileReader(filenameToBeCreated));
        assertThat(reader.readLine().equals("[" + messageDateTime.toString() + "]" + "test1")).isTrue();
    }

    @Test
    public void shouldSwitchToNewFileIfPreviousFileSizeIsBiggerThanLimit()  throws IOException {
        File firstFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        firstFileToWrite.delete();
        File secondFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 1));
        secondFileToWrite.delete();

        SwitchingFileSaver sut = new SwitchingFileSaver(1);

        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime);
        sut.close();

        BufferedReader reader1 = new BufferedReader(new FileReader(firstFileToWrite));
        BufferedReader reader2 = new BufferedReader(new FileReader(secondFileToWrite));

        assertThat(reader1.readLine().equals("[" + messageDateTime.toString() + "]" + "test1")).isTrue();
        assertThat(reader2.readLine().equals("[" + messageDateTime.toString() + "]" + "test2")).isTrue();
    }
}
