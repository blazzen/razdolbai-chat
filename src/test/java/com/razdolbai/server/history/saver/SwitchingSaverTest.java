package com.razdolbai.server.history.saver;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.*;


public class SwitchingSaverTest {

    private LocalDateTime messageDateTime = null;

    @Before
    public void beforeTest() throws IOException {

        String directoryName = Paths.get(".","resources", "History").toString();
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        else {
            for (File f : directory.listFiles()) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }
        }

        messageDateTime = LocalDateTime.now();
    }


    @Test
    public void shouldSaveAndNotSwitchIfLimitWasNotReachedAndSameDate() throws IOException {
        String filename = SwitchingFileSaver.fileNameFormat("history", messageDateTime,0);

        SwitchingFileSaver sut = new SwitchingFileSaver();
        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime);
        sut.close();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        assertThat(reader.readLine().equals("test1")).isTrue();
        assertThat(reader.readLine().equals("test2")).isTrue();
        reader.close();
    }

    @Test
    public void shouldCreateNewFileIfFileAlreadyExists() throws IOException {
        File existedFile = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        existedFile.delete();
        existedFile.createNewFile();

        File fileToBeCreated = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime,1));

        SwitchingFileSaver sut = new SwitchingFileSaver();
        sut.save("test1", messageDateTime);
        sut.close();

        BufferedReader reader = new BufferedReader(new FileReader(fileToBeCreated));
        assertThat(reader.readLine().equals("test1")).isTrue();
        reader.close();
    }

    @Test
    public void shouldSwitchToNewFileIfPreviousFileSizeIsBiggerThanLimit() throws IOException {
        File firstFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        File secondFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 1));

        SwitchingFileSaver sut = new SwitchingFileSaver(1);

        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime);
        sut.close();

        BufferedReader reader1 = new BufferedReader(new FileReader(firstFileToWrite));
        BufferedReader reader2 = new BufferedReader(new FileReader(secondFileToWrite));

        assertThat(reader1.readLine().equals("test1")).isTrue();
        assertThat(reader2.readLine().equals("test2")).isTrue();

        reader1.close();
        reader2.close();
    }

    @Test
    public void shouldSwitchToNewFileIfNewDayHasCome() throws IOException {
        File firstFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime, 0));
        File secondFileToWrite = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime.plusDays(1), 0));

        SwitchingFileSaver sut = new SwitchingFileSaver();

        sut.save("test1", messageDateTime);
        sut.save("test2", messageDateTime.plusDays(1));
        sut.close();

        BufferedReader reader1 = new BufferedReader(new FileReader(firstFileToWrite));
        BufferedReader reader2 = new BufferedReader(new FileReader(secondFileToWrite));

        assertThat(reader1.readLine().equals("test1")).isTrue();
        assertThat(reader2.readLine().equals("test2")).isTrue();

        reader1.close();
        reader2.close();
    }

    @After
    public void afterTest() {
        File directory = new File("./resources/History");
        for (File f : directory.listFiles()) {
            if (!f.isDirectory()) {
                f.delete();
            }
        }

    }
}
