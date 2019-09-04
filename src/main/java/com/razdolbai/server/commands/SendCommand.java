package com.razdolbai.server.commands;

public class SendCommand implements Command {
    private final String message;

    public SendCommand(String message) {
        this.message = message;
    }

    public String getMessage() {return message;}
}
