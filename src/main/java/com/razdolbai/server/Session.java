package com.razdolbai.server;

public interface Session extends Runnable{
    String getUsername();
    void setUsername(String nickname);
    void run();
}
