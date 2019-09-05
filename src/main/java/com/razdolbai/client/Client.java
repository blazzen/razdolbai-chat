package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class Client {

    private static BufferedReader inFromSocket = null;

    private Client() {
    }

    public static void main(String[] args) {


        try (
                final Socket socket = new Socket("localhost", 666);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())))
        ) {

            inFromSocket = in;
            Proxy proxy = new Proxy(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(proxy);


            inputConsole.readCommand();

            registerShutdownHook(socket, out, in);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void registerShutdownHook(Socket socket, PrintWriter out, BufferedReader in) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public static void printToOther() throws IOException {
        System.out.println(inFromSocket.readLine());
    }

}


