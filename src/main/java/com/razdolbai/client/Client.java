package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class Client {

    private Client() {
    }

    // todo is it ok that it throws ex
    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket("localhost", 8081);
        final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(socket.getOutputStream())));
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(socket.getInputStream())));
        registerShutdownHook(socket, out, in);

        try {
            Proxy proxy = new Proxy(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(proxy);
            inputConsole.readCommand();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void registerShutdownHook(Socket socket, PrintWriter out, BufferedReader in) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {

                System.out.println("SHUTDOWNHOOK IS WORKING");
                in.close();
                out.close();
                socket.close();

                System.out.println("Successfully closed client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}