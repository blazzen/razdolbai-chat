package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ShutdownHookCreator {
    void registerShutdownHook(Socket socket, PrintWriter out, BufferedReader in,
                              BufferedReader reader, Logger logger) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {

                in.close();
                out.close();
                socket.close();
                reader.close();

                System.out.println("Successfully closed client");
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Exception is thrown", e);
            }
        }));
    }
}
