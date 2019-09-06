package com.razdolbai.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatSessionFactory implements SessionFactory {
    private CommandFactory commandFactory;
    private final Logger logger;

    public ChatSessionFactory(CommandFactory commandFactory, Logger logger) {
        this.commandFactory = commandFactory;
        this.logger = logger;
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
        return new ChatSession(null, in, out, commandFactory, logger);
    }
}
