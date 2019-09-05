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
            List<String> historyList = new HistoryAccessObject().getHistory();
            for (String mess : historyList) {
                session.send(mess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
