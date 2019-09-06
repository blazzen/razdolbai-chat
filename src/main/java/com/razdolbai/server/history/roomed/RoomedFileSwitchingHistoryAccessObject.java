package com.razdolbai.server.history.roomed;

import com.razdolbai.server.history.history.History;
import com.razdolbai.server.history.history.HistoryAccessObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomedFileSwitchingHistoryAccessObject implements RoomedHistory{
    private Map<String, History> roomsAccessObjects;
    private final Object mutex = new Object();


    public RoomedFileSwitchingHistoryAccessObject() {
        roomsAccessObjects = new HashMap<>();
    }



    private void createDirectoryIfNotExist(String roomName) {
        File directory = new File("./resources/History/" + roomName);
        if(!directory.exists()) {
            directory.mkdir();
        }
    }


    @Override
    public List<String> getHistory(String roomName) {
        return null;
    }

    @Override
    public void save(String message, LocalDateTime dateTime, String roomName) throws IOException {
        History history;
        synchronized (mutex) {
            history = roomsAccessObjects.get(roomName);
            if (history == null) {
                history = new HistoryAccessObject(roomName);
                roomsAccessObjects.put(roomName, history);
            }
        }
        history.save(message, dateTime);

    }

    @Override
    public void close() throws Exception {
        for(History history : this.roomsAccessObjects.values()) {
            history.close();
        }
    }
}
