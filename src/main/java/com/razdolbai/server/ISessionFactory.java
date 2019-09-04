package com.razdolbai.server;

import java.net.Socket;

public interface ISessionFactory {
    Session createSession(Socket socket);
}
