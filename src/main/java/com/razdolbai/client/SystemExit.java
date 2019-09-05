package com.razdolbai.client;

class SystemExit {
    void exit(int status) {
        Thread.currentThread().interrupt();
    }
}