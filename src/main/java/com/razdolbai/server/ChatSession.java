package com.razdolbai.server;

import com.razdolbai.server.commands.Command;
import com.razdolbai.server.exceptions.ChatException;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.exceptions.UnidentifiedUserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatSession implements Session {
    private String username;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;
    private final Logger logger;
    private boolean isClosed = false;

    ChatSession(String username, BufferedReader socketIn, PrintWriter socketOut,
                CommandFactory commandFactory, Logger logger) {
        this.username = username;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
        this.logger = logger;
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
            logger.log(Level.SEVERE, "Exception in session", e);
        }
    }

    @Override
    public void send(String message) {
        socketOut.println(message);
        socketOut.flush();
    }

    @Override
    public void close() {
        isClosed = true;
        logger.log(Level.INFO, String.format("Debug: %s session closed%n", username));
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
        String logLine = String.format("%s %s %s%n", username, timeStamp, message);
        System.out.println(logLine);
        logger.log(Level.INFO, "Debug: " + logLine);
    }

    private void processException(Exception e, String message) {
        logger.log(Level.SEVERE, "Exception in session", e);
        send(message);
    }
}
