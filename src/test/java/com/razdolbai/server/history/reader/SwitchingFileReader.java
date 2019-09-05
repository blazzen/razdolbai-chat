package com.razdolbai.server.history.reader;

import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class SwitchingFileReader {

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



}
