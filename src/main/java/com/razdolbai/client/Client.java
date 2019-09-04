package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class Client{
    public static void main(String[] args) {

        String[] existingCommands = {"/snd", "/hist", "/chid"};
        try {

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

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(System.in));


                while (true) {

                    String input = reader.readLine();

                    InputParser parser = new InputParser(existingCommands);
                    Command command = parser.parse(input);
                    if (command!=null) {
                        proxy.send(command);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        catch (Exception e){
            // todo add logger
            e.printStackTrace();
        }

        System.out.println("Test");
    }

    private static void createNewConnection() {

    }
}