package com.razdolbai.server;

public interface Session extends Runnable{
    String getUsername();
    void run();
    void send(String message);
}
