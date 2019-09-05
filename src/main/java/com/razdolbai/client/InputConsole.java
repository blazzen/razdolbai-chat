package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;

class InputConsole {
    private CommandSender commandSender;
    private BufferedReader reader;
    private InputParser parser;

    InputConsole(CommandSender commandSender, BufferedReader reader, InputParser parser) {
        this.commandSender = commandSender;
        this.reader = reader;
        this.parser = parser;
    }

    void readCommand() {
        try {

            String input = reader.readLine();

            Command command = parser.parse(input);
            if (command != null) {
                commandSender.send(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
