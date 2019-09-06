package com.razdolbai.server;

import com.razdolbai.server.exceptions.OccupiedNicknameException;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Identificator {
    private Collection<String> nicknames;
    private final Lock lock;

    Identificator() {
        nicknames = new HashSet<>(1500);
        lock = new ReentrantLock();
    }

    public synchronized void changeNickname(String oldNickname, String newNickname) throws OccupiedNicknameException {
        try {
            lock.lock();
            if (nicknames.contains(newNickname)) {
                throw new OccupiedNicknameException();
            }
            nicknames.add(newNickname);
            if (oldNickname != null) {
                nicknames.remove(oldNickname);
            }
        } finally {
            lock.unlock();
        }
    }
}

