package com.razdolbai.server.commands;

import com.razdolbai.server.Decorator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;

public class CloseCommand implements Command {
    private final Session session;
    private final SessionStore sessionStore;
    public CloseCommand(Session session, SessionStore sessionStore) {
        this.session = session;
        this.sessionStore = sessionStore;
    }

    @Override
    public void execute() {
        sessionStore.remove(session);
        sendLeaveMessage();
    }

    private void sendLeaveMessage() {
        String nickname = session.getUsername();
        if (nickname != null) {
            String message = Decorator.leftMessage(nickname);
            sessionStore.sendToAll(message);
        }
    }
}
