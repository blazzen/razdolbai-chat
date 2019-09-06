package com.razdolbai.server.history;


import com.razdolbai.server.history.saver.SwitchingFileSaver;
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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PerformanceTest {

    private LocalDateTime messageDateTime = null;
    History sut;
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
        sut = new HistoryAccessObject();
    }


    @Test @Ignore
    public void perfomanceTest() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new MyThread(sut).start();
        }

        Thread.sleep(100000000);
    }


    class MyThread extends Thread {
        History sut;
        public MyThread(History sut) {
            super();
            this.sut = sut;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                int rand = random.nextInt(100);

                if (rand < 15) {
                    if (rand < 2) {
                        sut.getHistory();
                    } else {
                        try {
                            sut.save("sodjfnewofweiufn;wfiowefniwlefnewifuwn;ofwei pnwuipefw weiofsodjfnewofweiufn;wfiowefniwlefnewifuwn;ofwei pnwuipefw weiof", LocalDateTime.now());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
