package com.razdolbai.client;


import java.io.PrintWriter;

class Proxy {
    private static final String DELIMITER = "\0";
    private PrintWriter out;
    private SystemExit systemExit;

    private static final String CLOSE = "/close";

    Proxy(PrintWriter out, SystemExit systemExit) {
        this.out = out;
        this.systemExit = systemExit;

    }

    void send(Command command) {

        String result = "type:" + command.getCommandType();
        if (!command.getMessage().isEmpty()) {
            result = addMessage(command, result);
        }

        out.println(result);
        out.flush();

        if (command.getCommandType().equals(CLOSE)) {
            systemExit.exit(0);
        }
    }

    private String addMessage(Command command, String message) {
        String result = message;
        result += DELIMITER;
        result += "msg:" + command.getMessage();
        return result;
    }


}
