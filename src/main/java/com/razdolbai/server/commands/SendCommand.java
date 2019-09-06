package com.razdolbai.server.commands;

import com.razdolbai.server.Decorator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import com.razdolbai.server.history.saver.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Session session;
    private final SessionStore sessionStore;
    private final String message;
    private final Saver saver;
    private final LocalDateTime timestamp;
    private static Decorator stringMessage = new Decorator();
    public SendCommand(Session session, SessionStore sessionStore, String message, Saver saver, LocalDateTime timestamp) {
        this.session = session;
        this.sessionStore = sessionStore;
        this.message = message;
        this.saver = saver;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() throws UnidentifiedUserException, IOException {
        checkUsername();
        String decoratedMessage = Decorator.decorate(message,timestamp,session.getUsername());
        sessionStore.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }

    private void checkUsername() throws UnidentifiedUserException {
        String nickname = session.getUsername();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
    }
}
