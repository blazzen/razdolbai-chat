package com.razdolbai.server;

public class ServerLauncher {
    public static void main(String[] args) {
        Parser parser = new Parser("\0", ":");
        Saver saver = null;
        Identificator identificator = new Identificator();
        SessionStore sessionStore = new ChatSessionStore();
        ChatCommandFactory commandFactory = new ChatCommandFactory(parser, sessionStore, saver, identificator);
        ChatSessionFactory sessionFactory = new ChatSessionFactory(commandFactory);
        new Server(sessionFactory, sessionStore).startServer();
    }
}
