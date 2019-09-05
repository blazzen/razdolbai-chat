package com.razdolbai.client;


import com.razdolbai.common.CommandType;

import java.io.PrintWriter;

class CommandSender {
    private static final String DELIMITER = "\0";

    private PrintWriter out;
    private SystemExit systemExit;

    CommandSender(PrintWriter out, SystemExit systemExit) {
        this.out = out;
        this.systemExit = systemExit;
    }

    void send(Command command) {
        String result = "type:" + command.getType().getValue();
        if (!command.getMessage().isEmpty()) {
            result = addMessage(command, result);
        }

        out.println(result);
        out.flush();

        if (command.getType() == CommandType.CLOSE) {
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
