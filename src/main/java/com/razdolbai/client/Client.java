package com.razdolbai.client;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        String[] existingCommands = {"/snd", "/hist", "/chid", "/close"};

        try (
                final Socket socket = new Socket("localhost", 666);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())))
        ) {
            Proxy proxy = new Proxy(out);
            InputConsole inputConsole = new InputConsole(proxy);


            inputConsole.readCommand(existingCommands);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


