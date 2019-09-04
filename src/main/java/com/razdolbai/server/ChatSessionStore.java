package com.razdolbai.server;

import java.util.ArrayList;
import java.util.Collection;

public class ChatSessionStore implements SessionStore {
    private Collection<Session> sessions;

    public ChatSessionStore() {
        sessions = new ArrayList<>();
    }

    @Override
    public void sendToAll(String message) {
        sessions.forEach(s -> s.send(message));
    }

    @Override
    public void register(Session session) {
        sessions.add(session);
    }

    @Override
    public void remove(Session session) {
        sessions.remove(session);
    }
}
