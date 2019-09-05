package com.razdolbai.client;

import java.util.Arrays;

class InputParser {

    Command parse(String input) {
        String message = "";
        String commandType;
        if (input.contains(" ")) {
            commandType = input.substring(0, input.indexOf(" "));
            message = input.substring(input.indexOf(" ") + 1);
        } else {
            commandType = input;
        }

        if (!ExistingCommands.contains(commandType)) {
            System.out.println("Unknown command, try again");
            return null;
        }

        if (message.length() > 149) {
            System.out.println("Message is too long, try again");
            return null;
        }

        return new Command(commandType, message);

    }


}
