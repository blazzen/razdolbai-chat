package com.razdolbai.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {

    private Client() {
    }

    public static void main(String[] args) {

        Logger logger = Logger.getLogger("ClientLogger");
        logger.setLevel(Level.SEVERE);

        try (
                final Socket socket = new Socket("localhost", 8081);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())));
                final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            new ShutdownHookCreator().registerShutdownHook(socket, out, in, reader, logger);
            CommandSender commandSender = new CommandSender(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(commandSender, reader, new InputParser(), logger);

            while (!Thread.currentThread().isInterrupted()) {
                inputConsole.readCommand();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception is thrown", e);
        }

    }


}