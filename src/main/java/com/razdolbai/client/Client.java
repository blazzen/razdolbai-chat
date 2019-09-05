package com.razdolbai.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        String[] existingCommands = {"/snd", "/hist", "/chid", "/close"};

        try (
                final Socket socket = new Socket("localhost", 8081);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())))
        ) {
            final ServerSocket connectionListener = new ServerSocket(666);
            final Socket server = connectionListener.accept();
            System.out.println("Assepted");
            try (final PrintWriter consoleOutput = new PrintWriter(
                    new OutputStreamWriter(
                            new BufferedOutputStream(
                                    server.getOutputStream())))) {
                Thread thread = new Thread(() -> {
                    try {
                        while (true) {
                            String inputData = in.readLine();
                            if (inputData != null) {
                                consoleOutput.println(inputData);
                                consoleOutput.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                thread.start();
                Proxy proxy = new Proxy(out);
                InputConsole inputConsole = new InputConsole(proxy);
                inputConsole.readCommand(existingCommands);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}