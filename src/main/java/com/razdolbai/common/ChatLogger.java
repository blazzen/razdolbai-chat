package com.razdolbai.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatLogger {
    public static Logger createLogger(String loggerName, String loggerFilename) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setLevel(Level.INFO);
        FileHandler handler = null;
        try {
            handler = new FileHandler(loggerFilename, true);
            SimpleFormatter simple = new SimpleFormatter();
            handler.setFormatter(simple);
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Exception is thrown", e);
        }
        return logger;
    }
}
