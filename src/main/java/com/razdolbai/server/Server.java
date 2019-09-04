package com.razdolbai.server;

import com.razdolbai.server.commands.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService executorService;
    private final Parser parser;

    public Server() {
        this.executorService = Executors.newCachedThreadPool();
        parser = new Parser();
    }

    private void startServer() {
        try (
                final ServerSocket connectionListener = new ServerSocket(8081);
                PrintWriter historyWriter = new PrintWriter(
                        new OutputStreamWriter(
                        new BufferedOutputStream(new FileOutputStream("fin.txt"))))
        ) {
            registerShutdownHook(executorService);
            while (true) {
                startConnection(connectionListener, historyWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerShutdownHook(ExecutorService executorService) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            System.out.println("Server closed");
        }));
    }

    private void startConnection(ServerSocket connectionListener, PrintWriter historyWriter) throws IOException {
        Socket socket = connectionListener.accept();
        executorService.execute(() -> {
            try (final PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new BufferedOutputStream(
                                    socket.getOutputStream())));
                 final BufferedReader in = new BufferedReader(
                         new InputStreamReader(
                                 new BufferedInputStream(
                                         socket.getInputStream())))) {
                processClient(in, out, historyWriter);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void processClient(BufferedReader socketIn, PrintWriter socketOut, PrintWriter historyWriter) {
        try {
            String readLine = socketIn.readLine();
            Command command = parser.parse(readLine);
            while (!("type:close".equals(readLine))) {
                System.out.println("debug: " + readLine);
                socketOut.println(readLine);
                readLine = socketIn.readLine();
            }
            socketOut.println("Success");
            System.out.println("client closed");
        } catch (IOException e) {
            System.out.println("Error in processClient");
            e.printStackTrace();
            socketOut.println("Error: " + e.getMessage());
        } finally {
            socketOut.flush();
        }
    }

    private void saveToFile() {

    }

    public static void main(String[] args) {
        new Server().startServer();
    }
}
