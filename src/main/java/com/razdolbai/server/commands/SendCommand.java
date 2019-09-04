package com.razdolbai.server.commands;

import com.razdolbai.server.Saver;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.UnidentifiedUserException;

import java.time.LocalDateTime;

public class SendCommand implements Command {
    private final Session session;
    private final SessionStore sessionStore;
    private final String message;
    private final Saver saver;
    private final String timestamp;

    public SendCommand(Session session, SessionStore sessionStore, String message, Saver saver, String timestamp) {
        this.session = session;
        this.sessionStore = sessionStore;
        this.message = message;
        this.saver = saver;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() throws UnidentifiedUserException {
        checkUsername();
        String decoratedMessage = decorate(message);
        sessionStore.sendToAll(decoratedMessage);
        saver.save(decoratedMessage);
    }

    private String decorate(String message) {
        return timestamp + "\n" +
                session.getUsername() + "\n" +
                message + "\n";
    }

    private void checkUsername() throws UnidentifiedUserException {
        String nickname = session.getUsername();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
    }
}
