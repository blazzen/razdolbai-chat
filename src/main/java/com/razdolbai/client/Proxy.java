package com.razdolbai.client;


import java.io.PrintWriter;

class Proxy {
    private static final String DELIMITER = "\0";
    private PrintWriter out;

    Proxy(PrintWriter out) {
        this.out = out;
    }

    void send(Command command){

        String result = "type:" + command.getCommandType();
        if (!command.getMessage().isEmpty()) {
            result = addMessage(command, result);
        }

        out.println(result);
        out.flush();
    }

    private String addMessage(Command command, String result) {
        result+= DELIMITER;
        result += "msg:" + command.getMessage();
        return result;
    }
}
