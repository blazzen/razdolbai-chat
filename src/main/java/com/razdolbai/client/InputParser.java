package com.razdolbai.client;

import java.util.Arrays;

class InputParser {
    private String[] existingCommands;

    InputParser(String[] existingCommands) {
        this.existingCommands = existingCommands;
    }

    Command parse(String input) {
        String message = "";
        String commandType;
        if (input.contains(" ")) {
            commandType = input.substring(0, input.indexOf(" "));
            message = input.substring(input.indexOf(" ") + 1);
        } else {
            commandType = input;
        }

        if (!Arrays.asList(existingCommands).contains(commandType)){
            System.out.println("Unknown command, try again");
            return null;
        }




        return new Command(commandType, message);

    }
}
