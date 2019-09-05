package com.razdolbai.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatSessionStore implements SessionStore {
    private final ReentrantReadWriteLock lock;
    private Collection<Session> sessions;
    private final ExecutorService executorService;

    public ChatSessionStore() {
        sessions = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void sendToAll(String message) {
        lock.readLock().lock();
        sessions.forEach(s -> s.send(message));
        lock.readLock().unlock();
    }

    @Override
    public void register(Session session) {
        lock.writeLock().lock();
        sessions.add(session);
        lock.writeLock().unlock();
        executorService.execute(session);
    }

    @Override
    public void remove(Session session) {
        lock.writeLock().lock();
        sessions.remove(session);
        lock.writeLock().unlock();
        session.close();
    }

    @Override
    public void closeAll() {
        lock.readLock().lock();
        sessions.forEach(Session::close);
        lock.readLock().unlock();
        executorService.shutdown();
    }
}
