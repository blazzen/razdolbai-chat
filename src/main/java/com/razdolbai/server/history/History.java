package com.razdolbai.server.history;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface History extends AutoCloseable {
    List<String> getHistory();

    void save(String message, LocalDateTime dateTime) throws IOException;

    @Override
    void close() throws Exception;
}
