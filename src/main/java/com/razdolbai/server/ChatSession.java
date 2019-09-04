package com.razdolbai.server;

import com.razdolbai.server.commands.CloseCommand;
import com.razdolbai.server.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatSession implements Session{
    private String username;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;

    public ChatSession(String username, Socket socket, BufferedReader socketIn, PrintWriter socketOut, CommandFactory commandFactory) {
        this.username = username;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try (Socket mySocket = socket;
             final PrintWriter mySocketOut = socketOut;
             final BufferedReader mySocketIn = socketIn) {
            Command command = null;
            while (!(command instanceof CloseCommand)) {
                String message = mySocketIn.readLine();
                String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                command = commandFactory.getCommand(this, message, timeStamp);
                command.execute(); // command --> session.send(msg)
                System.out.printf("Debug: %s %s %s" + System.lineSeparator(), username, timeStamp, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {// TODO: 2019-09-04 add catch for NotAuthorizedException
            e.printStackTrace();
        }
    }

    void send(String message) {
        socketOut.println(message);
        socketOut.flush();
    }

    public String getUsername() {
        return username;
    }

    // will be set by ChIdCommand
    public void setUsername(String username) {
        this.username = username;
    }

}
