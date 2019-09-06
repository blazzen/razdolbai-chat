package com.razdolbai.server.history.reader;

import com.razdolbai.server.history.saver.SwitchingFileSaver;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;


import static org.fest.assertions.Assertions.*;

public class SwitchingFileReaderTest {

    private LocalDateTime messageDateTime = null;
    private SwitchingFileReader sut;


    private static void cleanRecursivelyDFS(File directory) {
        for(File currentFile: directory.listFiles()) {
            if(!currentFile.exists()) return;
            if(currentFile.isDirectory()) {
                cleanRecursivelyDFS(currentFile);
            } else {
                currentFile.delete();
            }
        }
    }

    @Before
    public void beforeTest() throws IOException {

        String directoryName = Paths.get(".","resources", "History").toString();
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        else {
            cleanRecursivelyDFS(new File("./resources"));
        }

        messageDateTime = LocalDateTime.now();
        sut = new SwitchingFileReader();
    }

    @After
    public void afterTest() throws  IOException {
        cleanRecursivelyDFS(new File("./resources"));
    }


    @Test
    public void shouldReadFromOneFile() throws IOException {
        SwitchingFileSaver saver = new SwitchingFileSaver();
        String message = "test";
        String message1 = "test1";


        saver.save(message, messageDateTime);
        saver.save(message1, messageDateTime);
        saver.close();

        List<String> history = sut.getHistory();

        assertThat(history.size()).isEqualTo(2);

        assertThat(history.get(0)).isEqualTo(message);
        assertThat(history.get(1)).isEqualTo(message1);
    }


    @Test
    public void shouldReadFromMultipleFiles() throws IOException {
        SwitchingFileSaver saver = new SwitchingFileSaver(5);
        String message = "test";
        String message1 = "test1";

        saver.save(message, messageDateTime);
        saver.save(message1, messageDateTime);
        saver.close();


        List<String> history = sut.getHistory();
        assertThat(history.get(0)).isEqualTo(message);
        assertThat(history.get(1)).isEqualTo(message1);
    }



    @Test
    public void shouldReadRecursively() throws IOException{
        SwitchingFileSaver saver = new SwitchingFileSaver(5);
        String message = "test";
        String message1 = "test1";
        String message2 = "test1";

        saver.save(message, messageDateTime);
        saver.save(message1, messageDateTime);
        saver.save(message2, messageDateTime.plusDays(1));
        saver.close();

        File innerDirectory = new File("./resources/History/tmpdir");
        innerDirectory.mkdir();

        File lastFile = new File(SwitchingFileSaver.fileNameFormat("history", messageDateTime.plusDays(1), 0));
        lastFile.renameTo(new File("./resources/History/tmpdir/" + lastFile.getName()));

        List<String> history = sut.getHistory();
        assertThat(history.size()).isEqualTo(3);
        assertThat(history.get(0)).isEqualTo(message);
        assertThat(history.get(1)).isEqualTo(message1);
        assertThat(history.get(2)).isEqualTo(message2);

        new File("./resources/History/tmpdir/" + lastFile.getName()).delete();
        innerDirectory.delete();

    }



}
