package com.razdolbai.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {

    private static final String EXCEPTION_MESSAGE = "Exception is thrown";

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
                final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                final ServerSocket connectionListener = new ServerSocket(666);
                final Socket server = connectionListener.accept();
                final PrintWriter consoleOutput = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(
                                        server.getOutputStream())))
        ) {

            Thread thread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        String inputData = in.readLine();
                        if (inputData != null) {
                            consoleOutput.println(inputData);
                            consoleOutput.flush();
                        }
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, EXCEPTION_MESSAGE, e);
                }
            });

            thread.start();

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("echo hello");
            CommandSender commandSender = new CommandSender(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(commandSender, reader, new InputParser(), logger);

            while (!Thread.currentThread().isInterrupted()) {
                inputConsole.readCommand();
            }
            consoleOutput.println("CLOSE");
            consoleOutput.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, EXCEPTION_MESSAGE, e);
        }

    }


}