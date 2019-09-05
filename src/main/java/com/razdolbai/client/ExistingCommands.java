package com.razdolbai.client;

public enum ExistingCommands {
    SEND("/snd"),

    HIST("/hist"),

    CHID("/chid"),

    CLOSE("/close");

    private String command;

    ExistingCommands(String command) {
        this.command = command;
    }


    public static boolean contains(String commandType) {

        for (ExistingCommands type : ExistingCommands.values()) {
            if (type.command.equals(commandType)) {
                return true;
            }
        }

        return false;
    }
}
