package com.razdolbai.server.commands;

import com.razdolbai.server.Session;

public class SendCommand implements Command {
    private final Session session;
    private final String message;

    public SendCommand(Session session, String message) {
        this.session = session;
        this.message = message;
    }

    @Override
    public void execute() {

    }
}
