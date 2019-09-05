package com.razdolbai.server.commands;

import com.razdolbai.server.Session;
import com.razdolbai.server.history.HistoryAccessObject;

import java.io.IOException;
import java.util.List;

public class HistoryCommand implements Command {
    private final Session session;
    private HistoryAccessObject history = null;

    public HistoryCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        try {
            history = new HistoryAccessObject();
            List<String> list = history.getHistory();
            for ( String mess: list
                 ) {
                session.send(mess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
