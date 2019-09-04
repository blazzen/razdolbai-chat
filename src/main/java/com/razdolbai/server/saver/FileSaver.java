package com.razdolbai.server.saver;

import com.sun.xml.internal.bind.annotation.XmlLocation;

import java.io.*;
import java.time.LocalDateTime;

public class FileSaver implements Saver {
    class FileExistsException extends RuntimeException {
        FileExistsException() {
            super();
        }
    }


    String filename;

    private PrintWriter out;
    private boolean isClosed = false;


    public FileSaver (String filename) throws IOException {
        open(filename);
    }


    protected void open(String filename) throws  FileExistsException, IOException {
        this.filename = filename;
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
    public synchronized void save(String string, LocalDateTime dateTime) throws IOException{
        out.println("[" + dateTime.toString() + "]" + string);
        out.flush();
    }

    @Override
    public synchronized void close() {
        if (isClosed) return;
        isClosed = true;

        out.close();
    }
}
