package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.exceptions.OccupiedNicknameException;

public class ChangeIdCommand implements Command {
    private final Session session;
    private final Identificator identificator;
    private final String newNickname;

    public ChangeIdCommand(Session session, Identificator identificator, String newNickname) {
        this.session = session;
        this.identificator = identificator;
        this.newNickname = newNickname;
    }

    @Override
    public void execute() throws OccupiedNicknameException {
        String oldNickname = session.getUsername();
        identificator.changeNickname(oldNickname, newNickname);
    }
}
