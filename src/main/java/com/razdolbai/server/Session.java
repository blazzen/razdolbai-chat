package com.razdolbai.server;

public interface Session {
    String getUsername();
    void run();
    void stop();
}
