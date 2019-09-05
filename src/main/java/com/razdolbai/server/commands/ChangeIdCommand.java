package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeIdCommand implements Command {
    private final Session session;
    private final Identificator identificator;
    private final String newNickname;
    private final LocalDateTime timestamp;
    private final SessionStore sessionStore;

    public ChangeIdCommand(Session session, Identificator identificator, String newNickname, LocalDateTime timestamp, SessionStore sessionStore) {
        this.session = session;
        this.identificator = identificator;
        this.newNickname = newNickname;
        this.timestamp = timestamp;
        this.sessionStore = sessionStore;
    }

    @Override
    public void execute() throws OccupiedNicknameException {
        String oldNickname = session.getUsername();
        identificator.changeNickname(oldNickname, newNickname);
        session.setUsername(newNickname);
        sendChangedNicknameMessage(oldNickname, newNickname);
    }

    private void sendChangedNicknameMessage(String oldNickname, String newNickname) {
        String message = "";
        if (oldNickname == null) {
            message = newNickname + " joined the chat";
        } else {
            message = oldNickname + " has changed name to " + newNickname;
        }
        String decoratedMessage = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" + message + "\n";
        sessionStore.sendToAll(decoratedMessage);
    }
}
