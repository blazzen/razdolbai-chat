package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;

public class ChangeIdCommand implements Command {
    private final Session session;
    private final Identificator identificator;
    private final String newNickname;
    private final String timestamp;
    private final SessionStore sessionStore;

    public ChangeIdCommand(Session session, Identificator identificator, String newNickname, String timestamp, SessionStore sessionStore) {
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
        String decoratedMessage = timestamp + "\n" + message + "\n";
        sessionStore.sendToAll(decoratedMessage);
    }
}
