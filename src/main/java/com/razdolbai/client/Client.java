package com.razdolbai.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Client {

    private static final String EXCEPTION_MESSAGE = "Exception is thrown";

    private Client() {
    }

    public static void main(String[] args) {

        Logger logger = Logger.getLogger("ClientLogger");
        logger.setLevel(Level.INFO);


        try (
                final Socket socket = new Socket("localhost", 8081);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())));
                final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                final ServerSocket connectionListener = new ServerSocket(Integer.parseInt(args[0]));
                final Socket server = connectionListener.accept();
                final PrintWriter consoleOutput = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(
                                        server.getOutputStream())))
        ) {

            FileHandler handler = new FileHandler("client.log", true);
            SimpleFormatter simple = new SimpleFormatter();
            handler.setFormatter(simple);
            logger.addHandler(handler);
            logger.log(Level.INFO, "Client started");

            Thread thread = new Thread(() -> new OutputConsoleWriter(consoleOutput, in, logger).run());

            thread.start();

            CommandSender commandSender = new CommandSender(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(commandSender, reader, new InputParser(), logger);

            while (!Thread.currentThread().isInterrupted()) {
                inputConsole.readCommand();
            }
            consoleOutput.println("CLOSE");
            consoleOutput.flush();
            thread.interrupt();

        } catch (IOException e) {
            logger.log(Level.SEVERE, EXCEPTION_MESSAGE, e);
        }

    }


}