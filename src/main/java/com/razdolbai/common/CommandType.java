package com.razdolbai.common;


public enum CommandType {
    SEND("/snd"),
    HIST("/hist"),
    CHID("/chid"),
    CLOSE("/close"),
    UNKNOWN("");

    private String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getValue() {
        return command;
    }

    public static CommandType fromString(String command) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.command.equalsIgnoreCase(command)) {
                return commandType;
            }
        }
        return UNKNOWN;
    }
}
