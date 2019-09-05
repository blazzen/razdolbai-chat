package com.razdolbai.server.history.saver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * File Saver which switches file on date changing or limit exceeding
 * File size limit - 2^21 messages in 2-byte encoding 150 symbols per each
 */
public class SwitchingFileSaver extends FileSaver {
    private final int sizeLimit;
    private int sizeCounter = 0;
    private int fileCounter = 0;
    private final static String pathname = Paths.get(".","resources", "History").toString();
    private final static String name = "history";
    private final static String format = ".txt";
    private final static int defaultSizeLimit = 2 * 150 * 2097152;


    static {
        File directory = new File(pathname);
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    private LocalDateTime dateTime;

    public SwitchingFileSaver(int sizeLimit) throws IOException {
        dateTime = LocalDateTime.now();
        this.open(dateTime);
        this.sizeLimit = sizeLimit;
    }


    public SwitchingFileSaver() throws IOException {
        dateTime = LocalDateTime.now();
        this.open(dateTime);
        this.sizeLimit = defaultSizeLimit;
    }

    public static String fileNameFormat(String name, LocalDateTime dateTime, int fileCounter) {
        return pathname + File.separator + name + "_"
                + dateTime.getDayOfMonth() + "_"
                + dateTime.getMonth() + "_"
                + dateTime.getYear() + "_" +
                fileCounter + format;
    }

    @Override
    public void save(String string, LocalDateTime dateTime) throws IOException {
        Boolean isDateChanged = !this.dateTime.toLocalDate().equals(dateTime.toLocalDate());
        if (sizeCounter > sizeLimit || isDateChanged) {
            sizeCounter = 0;
            switchFile(dateTime);
            fileCounter = isDateChanged ? 0 : fileCounter++;
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
        this.open(dateTime);
    }
}
