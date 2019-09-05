package com.razdolbai.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private static BufferedReader inFromServer;

    public Client() {
        inFromServer = null;
    }


    public static void main(String[] args) throws IOException {

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
            PrintWriter consoleOutput = createPrinter();

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String inputData = in.readLine();
                        if (inputData != null & consoleOutput!=null)  {
                            consoleOutput.println(inputData);
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
    }

    private static PrintWriter createPrinter() {
        try (final ServerSocket connectionListener = new ServerSocket(666)) {
            final Socket socket = connectionListener.accept();
            System.out.println("Assepted");
            try (final PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new BufferedOutputStream(
                                    socket.getOutputStream())))) {
                System.out.println("Started");
                return out;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}