package com.razdolbai.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputConsole {
    private static final String EXCEPTION_MESSAGE = "Exception is thrown";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("ClientLogger");
        logger.setLevel(Level.SEVERE);

        try (
                final Socket socket = new Socket("localhost", 666);
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())))
        ) {
            while (!Thread.currentThread().isInterrupted()) {
                String socketInput = in.readLine();
                if ("CLOSE".equals(socketInput)) {
                    Thread.currentThread().interrupt();
                }
                if (socketInput != null) {
                    System.out.println(socketInput);
                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, EXCEPTION_MESSAGE, e);
        }
    }
}
