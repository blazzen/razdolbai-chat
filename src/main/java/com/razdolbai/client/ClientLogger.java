package com.razdolbai.client;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientLogger {
    static Logger createLogger() {
        Logger logger = Logger.getLogger("ClientLogger");
        logger.setLevel(Level.INFO);
        FileHandler handler = null;
        try {
            handler = new FileHandler("client.log", true);
            SimpleFormatter simple = new SimpleFormatter();
            handler.setFormatter(simple);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Exception is thrown", e);
        }
        return logger;
    }
}
