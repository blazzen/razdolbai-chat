package com.razdolbai.server.history;

import com.razdolbai.server.history.reader.Reader;
import com.razdolbai.server.history.reader.SwitchingFileReader;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.history.saver.SwitchingFileSaver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HistoryAccessObject implements History {
    private Saver saver;
    private Reader reader;
    private ReadWriteLock readWriteLock;

    public HistoryAccessObject() throws IOException {
        saver = new SwitchingFileSaver();
        reader = new SwitchingFileReader();
        readWriteLock = new ReentrantReadWriteLock();
    }


    @Override
    public List<String> getHistory() {
        List<String> history = null;
        try {
            readWriteLock.readLock();
            history = reader.getHistory();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return history;
    }

    @Override
    public void save(String message, LocalDateTime dateTime) throws IOException {
        try {
            readWriteLock.writeLock().lock();
            saver.save(message, dateTime);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void close() throws Exception {
        saver.close();
    }
}
