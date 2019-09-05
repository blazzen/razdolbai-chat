package com.razdolbai.server;

import java.io.IOException;
import java.net.Socket;

public interface SessionFactory {
    Session createSession(Socket socket) throws IOException;
}
