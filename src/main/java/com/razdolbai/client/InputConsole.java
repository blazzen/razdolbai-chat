package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class InputConsole {
    private CommandSender commandSender;
    private BufferedReader reader;
    private InputParser parser;
    private Logger logger;

    InputConsole(CommandSender commandSender, BufferedReader reader, InputParser parser, Logger logger) {
        this.commandSender = commandSender;
        this.reader = reader;
        this.parser = parser;
        this.logger = logger;
    }

    void readCommand() {
        try {

            String input = reader.readLine();

            Command command = parser.parse(input);
            if (command != null) {
                commandSender.send(command);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception is thrown", e);
        }
    }
}
