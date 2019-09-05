package com.razdolbai.client;

class SystemExit {
    void exit() {
        Thread.currentThread().interrupt();
    }
}