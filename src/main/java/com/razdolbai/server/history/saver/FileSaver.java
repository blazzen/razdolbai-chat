package com.razdolbai.server.history.saver;


import java.io.*;
import java.time.LocalDateTime;

public class FileSaver implements Saver {
    class FileExistsException extends RuntimeException {
        FileExistsException() {
            super();
        }
    }

    private PrintWriter out;
    private volatile boolean isClosed = false;


    public FileSaver () {

    }


    protected void open(String filename) throws  FileExistsException, IOException {
        this.isClosed = false;
        File file = new File(filename);
        if(file.exists()) {
            throw new FileExistsException();
        }

        out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                new FileOutputStream(filename, true))));
    }

    @Override
    public void save(String string, LocalDateTime dateTime) throws IOException{
        out.println("[" + dateTime.toString() + "]" + string);
    }

    @Override
    public void close() {
        if (isClosed) return;

        isClosed = true;

        out.flush();
        out.close();
    }
}
