package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class Client {

    private Client() {
    }

    public static void main(String[] args) {

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

            registerShutdownHook(socket, out, in, reader);
            CommandSender commandSender = new CommandSender(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(commandSender, reader, new InputParser());

            while (!Thread.currentThread().isInterrupted()) {
                inputConsole.readCommand();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void registerShutdownHook(Socket socket, PrintWriter out, BufferedReader in, BufferedReader reader) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {

                in.close();
                out.close();
                socket.close();
                reader.close();

                System.out.println("Successfully closed client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}