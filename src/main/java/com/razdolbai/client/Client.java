package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class Client {

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
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String a = in.readLine();
                        if (a != null) {
                            System.out.println(a);
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
}