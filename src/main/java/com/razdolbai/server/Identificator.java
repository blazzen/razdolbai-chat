package com.razdolbai.server;

import com.razdolbai.server.exceptions.OccupiedNicknameException;

import java.util.HashSet;
import java.util.Set;

public class Identificator {
    private Set<String> nicknames = new HashSet<String>(1500);

    public synchronized void changeNickname(String oldNickname, String newNickname) {
        if (nicknames.contains(newNickname)) {
            throw new OccupiedNicknameException();
        }
        nicknames.add(newNickname);
        if (oldNickname != null) {
            nicknames.remove(oldNickname);
        }
    }
}

