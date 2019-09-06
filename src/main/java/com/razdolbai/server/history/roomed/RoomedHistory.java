package com.razdolbai.server.history.roomed;

import com.razdolbai.server.history.history.History;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomedHistory extends AutoCloseable{
    List<String> getHistory(String roomName);

    void save(String message, LocalDateTime dateTime, String roomName) throws IOException;

    @Override
    void close() throws Exception;

}
