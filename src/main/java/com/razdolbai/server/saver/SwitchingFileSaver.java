package com.razdolbai.server.saver;

import java.io.IOException;
import java.time.LocalDateTime;


/**
 * File Saver which switches file on date changing or limit exceeding
 * File size limit - 2^21 messages in 2-byte encoding 150 symbols per each
 */
public class SwitchingFileSaver extends FileSaver {
    private final int sizeLimit;
    private int sizeCounter = 0;
    private int fileCounter = 0;
    private final static String name = "history";
    private final static String format = ".txt";

    private LocalDateTime dateTime;

    public SwitchingFileSaver() throws IOException {
        dateTime = LocalDateTime.now();
        this.open(dateTime);
        this.sizeLimit = 2 * 150 * 2097152;
    }

    public static String fileNameFormat(String name, LocalDateTime dateTime, int fileCounter) {
        return "./resources/History/" + name + "_"
                + dateTime.getDayOfMonth() + "_"
                + dateTime.getMonth() + "_"
                + dateTime.getYear() + "_" +
                fileCounter + format;
    }

    @Override
    public synchronized void save(String string, LocalDateTime dateTime) throws IOException {
        if (sizeCounter > sizeLimit ||
                this.dateTime.getDayOfYear() != dateTime.getDayOfYear() ||
                this.dateTime.getYear() != dateTime.getYear()) {
            sizeCounter = 0;
            switchFile(dateTime);
        }

        sizeCounter += string.getBytes().length;

        super.save(string, dateTime);
        this.dateTime = dateTime;
    }


    private void open(LocalDateTime dateTime) throws IOException {
        boolean opened = false;
        while(!opened) {
            try {
                super.open(fileNameFormat(name, dateTime, fileCounter));
            } catch (FileExistsException e) {
                fileCounter++;
                continue;
            }
            opened = true;
        }
    }

    private void switchFile(LocalDateTime dateTime) throws IOException {
        super.close();
        fileCounter++;
        this.open(dateTime);
    }


}
