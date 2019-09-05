package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputConsoleWriter implements Runnable {
    private PrintWriter consoleOutput;
    private final BufferedReader in;
    private final Logger logger;
    private static final String EXCEPTION_MESSAGE = "Exception is thrown";

    OutputConsoleWriter(PrintWriter consoleOutput, BufferedReader in, Logger logger) {
        this.consoleOutput = consoleOutput;
        this.in = in;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                processInput();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, EXCEPTION_MESSAGE, e);
        }
    }

    void processInput() throws IOException {
        String inputData = in.readLine();
        if (inputData != null && !inputData.isEmpty()) {
            consoleOutput.println(inputData);
            consoleOutput.flush();
        }
    }
}
