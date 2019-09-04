package com.razdolbai.server.commands;

import com.razdolbai.server.Session;

public class CloseCommand implements Command {
    private final Session session;

    public CloseCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {

    }
}
