package com.razdolbai.client;

import com.razdolbai.common.CommandType;

class InputParser {

    Command parse(String input) {
        String message = "";
        String commandType;
        if (input.contains(" ")) {
            int separatorIndex = input.indexOf(' ');
            commandType = input.substring(0, separatorIndex);
            message = input.substring(separatorIndex + 1);
        } else {
            commandType = input;
        }

        CommandType command;
        try {
            command = CommandType.fromString(commandType);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (message.length() > 149) {
            System.out.println("Message is too long, try again");
            return null;
        }

        return new Command(command, message);
    }
}
