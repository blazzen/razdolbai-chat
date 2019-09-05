package com.razdolbai.server;

import com.razdolbai.server.commands.CloseCommand;
import com.razdolbai.server.commands.Command;
import com.razdolbai.server.exceptions.UnidentifiedUserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatSession implements Session {
    private String username;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;

    ChatSession(String username, Socket socket, BufferedReader socketIn, PrintWriter socketOut, CommandFactory commandFactory) {
        this.username = username;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            Command command = null;
            while (!(command instanceof CloseCommand)) {
                String message = socketIn.readLine();
                LocalDateTime timeStamp = LocalDateTime.now();
                command = commandFactory.createCommand(this, message, timeStamp);
                command.execute(); // command --> session.send(msg)
                System.out.printf("Debug: %s %s %s" + System.lineSeparator(), username, timeStamp, message);
            }
            close();
        } catch (UnidentifiedUserException e) {
            e.printStackTrace();
            send("First command should be /chid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) {
        socketOut.println(message);
        socketOut.flush();
    }

    @Override
    public void close() {
        try {
            if (socketIn != null) {
                socketIn.close();
            }
            if (socketOut != null) {
                socketOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.printf("Debug: error in closing session %s" + System.lineSeparator(), username);
            e.printStackTrace();
        }
        System.out.printf("Debug: %s session closed" + System.lineSeparator(), username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    // will be set by ChIdCommand
    public void setUsername(String username) {
        this.username = username;
    }
}
