package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.OccupiedNicknameException;

public class IdentificationCommand implements Command {
    private final Identificator identificator;
    private final String nickname;

    public IdentificationCommand(Identificator identificator, String nickname) {
        this.identificator = identificator;
        this.nickname = nickname;
    }

    @Override
    public void execute() {
        boolean isSucceed = identificator.takeNickname(nickname);
        if (!isSucceed) {
            throw new OccupiedNicknameException();
        }
    }
}
