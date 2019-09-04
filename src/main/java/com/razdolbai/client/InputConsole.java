package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class InputConsole {
    private Proxy proxy;


    InputConsole(Proxy proxy) {
        this.proxy = proxy;
    }

    void readCommand(String[] existingCommands) throws IOException {

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            String input = reader.readLine();

            InputParser parser = new InputParser(existingCommands);
            Command command = parser.parse(input);
            if (command != null) {
                proxy.send(command);
            }


        }

    }

}
