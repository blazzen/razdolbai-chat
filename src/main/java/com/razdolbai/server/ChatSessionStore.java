package com.razdolbai.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatSessionStore implements SessionStore {
    private Collection<Session> sessions;
    private final ExecutorService executorService;
    private final ReadWriteLock rwl;

    public ChatSessionStore() {
        sessions = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        rwl = new ReentrantReadWriteLock(false);
    }

    @Override
    public void sendToAll(String message) {
        try {
            rwl.readLock().lock();
            sessions.forEach(s -> s.send(message));
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public void register(Session session) {
        try {
            rwl.writeLock().lock();
            sessions.add(session);
        } finally {
            rwl.writeLock().unlock();
        }
        executorService.execute(session);
    }

    @Override
    public void remove(Session session) {
        try {
            rwl.writeLock().lock();
            sessions.remove(session);
        } finally {
            rwl.writeLock().unlock();
        }
        session.close();
    }

    @Override
    public void closeAll() {
        try {
            rwl.readLock().lock();
            sessions.forEach(Session::close);
        } finally {
            rwl.readLock().unlock();
        }
        executorService.shutdown();
    }
}
