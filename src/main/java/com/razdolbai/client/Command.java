package com.razdolbai.client;


class Command {
    private String commandType;
    private String message;

    Command(String commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }

    String getCommandType() {
        return commandType;
    }

    String getMessage() {
        return message;
    }
}
