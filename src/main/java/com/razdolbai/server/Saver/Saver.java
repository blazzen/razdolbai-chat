package com.razdolbai.server.Saver;

import java.io.IOException;
import java.time.LocalDateTime;

public interface Saver {
    void save(String string, LocalDateTime dateTime) throws IOException;

    void close();

}
