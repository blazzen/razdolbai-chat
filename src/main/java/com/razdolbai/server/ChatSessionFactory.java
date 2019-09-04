package com.razdolbai.server;

import java.io.*;
import java.net.Socket;

public class ChatSessionFactory implements ISessionFactory {
    private ICommandFactory commandFactory;

    public ChatSessionFactory(ICommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @Override
    public Session createSession(Socket socket) {
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                socket.getInputStream())));
        final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                socket.getOutputStream())));
        return new Session(null, socket, in, out, commandFactory);
    }
}
