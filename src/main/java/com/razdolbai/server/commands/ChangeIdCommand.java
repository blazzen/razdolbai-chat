package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.history.saver.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeIdCommand implements Command {
    private final Session session;
    private final Identificator identificator;
    private final String newNickname;
    private final LocalDateTime timestamp;
    private final SessionStore sessionStore;
    private final Saver saver;

    public ChangeIdCommand(Session session, Identificator identificator, String newNickname,
                           LocalDateTime timestamp, SessionStore sessionStore, Saver saver) {
        this.session = session;
        this.identificator = identificator;
        this.newNickname = newNickname;
        this.timestamp = timestamp;
        this.sessionStore = sessionStore;
        this.saver = saver;
    }

    @Override
    public void execute() throws OccupiedNicknameException, IOException {
        String oldNickname = session.getUsername();
        identificator.changeNickname(oldNickname, newNickname);
        session.setUsername(newNickname);
        sendChangedNicknameMessage(oldNickname, newNickname);
    }

    private void sendChangedNicknameMessage(String oldNickname, String newNickname) throws IOException {
        String message = "";
        if (oldNickname == null) {
            message = newNickname + " joined the chat";
        } else {
            message = oldNickname + " has changed name to " + newNickname;
        }
        String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message;
        sessionStore.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }
}
