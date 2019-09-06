package com.razdolbai.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private ServerSocket connectionListener;
    private SessionFactory sessionFactory;
    private SessionStore sessionStore;
    private final Logger logger;

    public Server(SessionFactory sessionFactory, SessionStore sessionStore, Logger logger) {
        this.sessionFactory = sessionFactory;
        this.sessionStore = sessionStore;
        this.logger = logger;
    }

    void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            connectionListener = serverSocket;
            registerShutdownHook();
            while (true) {
                Socket socket = connectionListener.accept();
                Session session = sessionFactory.createSession(socket);
                sessionStore.register(session);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception is thrown", e);
        }
    }


    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (connectionListener != null) {
                try {
                    sessionStore.sendToAll("Server died ;<");
                    sessionStore.closeAll();
                    connectionListener.close();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Exception in Shutdown Hook", e);
                }
            }
            logger.log(Level.INFO, "Server closed");
        }));
    }
}
