package com.razdolbai.server;

import java.io.*;
import java.net.Socket;

public class ChatSessionFactory implements SessionFactory {
    private CommandFactory commandFactory;

    public ChatSessionFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @Override
    public Session createSession(Socket socket) throws IOException {
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                socket.getInputStream())));
        final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                socket.getOutputStream())));
        return new ChatSession(null, socket, in, out, commandFactory);
    }
}
