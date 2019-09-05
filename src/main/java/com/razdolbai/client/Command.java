package com.razdolbai.client;


import com.razdolbai.common.CommandType;

class Command {
    private CommandType commandType;
    private String message;

    Command(CommandType commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }

    CommandType getType() {
        return commandType;
    }

    String getMessage() {
        return message;
    }
}
