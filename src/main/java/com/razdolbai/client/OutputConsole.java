package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class OutputConsole {
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
            while (true) {
                String socketInput = in.readLine();
                if (socketInput != null) {
                    System.out.println(socketInput);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

