package com.razdolbai.server;

import com.razdolbai.server.commands.Command;
import com.razdolbai.server.exceptions.ChatException;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.exceptions.UnidentifiedUserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

public class ChatSession implements Session {
    private String username;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;
    private boolean isClosed = false;

    ChatSession(String username, Socket socket, BufferedReader socketIn, PrintWriter socketOut, CommandFactory commandFactory) {
        this.username = username;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try (
                BufferedReader myIn = socketIn
        ) {
            while (!isClosed) {
                String message = myIn.readLine();
                processRequest(message);
            }
        } catch (IOException e) {
            //TODO add logger
        }
    }

    @Override
    public void send(String message) {
//            e.printStackTrace();
        socketOut.println(message);
        socketOut.flush();
    }

    @Override
    public void close() {
        isClosed = true;
        System.out.printf("Debug: %s session closed%n", username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    private void processRequest(String message) throws IOException {
        LocalDateTime timeStamp = LocalDateTime.now();
        Command command = commandFactory.createCommand(this, message, timeStamp);
        try {
            command.execute();
        } catch (UnidentifiedUserException e) {
            processException(e, "First command should be /chid");
        } catch (OccupiedNicknameException e) {
            processException(e, "This nickname is occupied, try another one");
        } catch (ChatException e) {
            processException(e, "Some error has occurred");
        }
        System.out.printf("Debug: %s %s %s%n", username, timeStamp, message);
    }

    private void processException(Exception e, String message) {
        e.printStackTrace();
        send(message);
    }
}
