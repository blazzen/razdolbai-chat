package com.razdolbai.client;

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


        return new Command(commandType, message);

    }
}
