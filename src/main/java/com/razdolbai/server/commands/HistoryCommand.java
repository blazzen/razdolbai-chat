package com.razdolbai.server.commands;

import com.razdolbai.server.Session;
import com.razdolbai.server.history.HistoryAccessObject;

import java.util.List;

public class HistoryCommand implements Command {
    private final Session session;

    private final HistoryAccessObject history;


    public HistoryCommand(Session session, HistoryAccessObject history) {
        this.session = session;
        this.history = history;
    }

    @Override
    public void execute() {
        session.send("Chat history: ");
        List<String> historyList = history.getHistory();
        for (String mess : historyList) {
            session.send(mess);
        }
    }
}
