package com.razdolbai.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatSessionStore implements SessionStore {
    private Collection<Session> sessions;
    private final ExecutorService executorService;

    public ChatSessionStore() {
        sessions = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void sendToAll(String message) {
        sessions.forEach(s -> s.send(message));
    }

    @Override
    public void register(Session session) {
        sessions.add(session);
        executorService.execute(session);
    }

    @Override
    public void remove(Session session) {
        sessions.remove(session);
    }

    @Override
    public void closeAll() {
        sessions.forEach(Session::close);
        executorService.shutdown();
    }
}
