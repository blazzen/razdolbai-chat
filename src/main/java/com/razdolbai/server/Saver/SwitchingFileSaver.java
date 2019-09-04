package com.razdolbai.server.Saver;

import java.io.IOException;
import java.time.LocalDateTime;

public class SwitchingFileSaver extends FileSaver {
    private final int sizeLimit;
    private int sizeCounter = 0;
    private int fileCounter = 0;
    private String name;
    private final static String format = ".txt";

    private LocalDateTime dateTime;


    public SwitchingFileSaver(String name, LocalDateTime dateTime, int sizeLimit) throws IOException {
        super(fileNameFormat(name, dateTime, 0));
        this.sizeLimit = sizeLimit;
        this.name = name;
        this.dateTime = dateTime;
    }

    public static String fileNameFormat(String name, LocalDateTime dateTime, int fileCounter) {
        return name + "_"
                + dateTime.getDayOfMonth() + "_"
                + dateTime.getMonth() + "_"
                + dateTime.getYear() + "_" +
                fileCounter + format;
    }

    @Override
    public synchronized void save(String string, LocalDateTime dateTime) throws IOException {
        sizeCounter += string.length();

        if (sizeCounter > sizeLimit || this.dateTime.getDayOfYear() != dateTime.getDayOfYear()) {
            sizeCounter = 0;
            switchFile();
        }

        super.save(string, dateTime);

        this.dateTime = dateTime;
    }

    private void switchFile() throws IOException {
        super.close();
        fileCounter++;
        super.filename = fileNameFormat(name, dateTime, fileCounter);
        super.open();
    }
}
