package com.razdolbai.server.history;

import com.razdolbai.server.history.reader.Reader;
import com.razdolbai.server.history.reader.SwitchingFileReader;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.history.saver.SwitchingFileSaver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryAccessObject implements History {
    Saver saver;
    Reader reader;

    public HistoryAccessObject() throws IOException {
       saver = new SwitchingFileSaver();
       reader = new SwitchingFileReader();
    }



    @Override
    public List<String> getHistory() {
        return null;
    }

    @Override
    public void save(String message, LocalDateTime dateTime) {

    }

    @Override
    public void close() throws Exception {

    }
}
