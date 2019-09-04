package com.razdolbai.server;

import java.util.HashSet;
import java.util.Set;

public class Identificator {
    private Set<String> nicknames = new HashSet<String>(1500);

    public synchronized boolean takeNickname(String nickname) {
        if (nicknames.contains(nickname)) {
            return false;
        }
        nicknames.add(nickname);
        return true;
    }

    public synchronized void freeNickname(String nickname) {
        nicknames.remove(nickname);
    }
}

