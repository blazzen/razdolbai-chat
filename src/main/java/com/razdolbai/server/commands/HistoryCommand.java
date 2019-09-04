package com.razdolbai.server.commands;

import com.razdolbai.server.Session;

public class HistoryCommand implements Command {
    private final Session session;

    public HistoryCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {

    }
}
