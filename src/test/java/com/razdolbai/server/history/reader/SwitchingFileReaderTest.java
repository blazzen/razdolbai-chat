package com.razdolbai.server.history.reader;

import com.razdolbai.server.history.saver.SwitchingFileSaver;
import org.junit.Before;
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
        sut = new SwitchingFileReader();
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

        assertThat(history.size() == 2).isTrue();

        assertThat(history.get(0)).isEqualTo("[" + messageDateTime + "]" + message);
        assertThat(history.get(1)).isEqualTo("[" + messageDateTime + "]" + message1);
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
        assertThat(history.get(0)).isEqualTo("[" + messageDateTime + "]" + message);
        assertThat(history.get(1)).isEqualTo("[" + messageDateTime + "]" + message1);
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
        System.out.println(lastFile.getName());
        System.out.println("./resources/History/tmpdir/" + lastFile.getName());
        lastFile.renameTo(new File("./resources/History/tmpdir/" + lastFile.getName()));

        List<String> history = sut.getHistory();
        assertThat(history.size()).isEqualTo(3);
        assertThat(history.get(0)).isEqualTo("[" + messageDateTime + "]" + message);
        assertThat(history.get(1)).isEqualTo("[" + messageDateTime + "]" + message1);
        assertThat(history.get(2)).isEqualTo("[" + messageDateTime.plusDays(1) + "]" + message1);

    }



}
