package com.razdolbai.server;

import com.razdolbai.server.commands.*;

class Parser {
    private static final String FIELD_DELIMITER = "\0";
    private static final String HEAD_BODY_DELIMITER = ":";

    public Command parse(String message) {
        String[] fields = message.split(FIELD_DELIMITER);
        String type = getType(fields[0]);
        switch (type) {
            case "/hist": {
                return parseHistMessage(fields);
            }
            case "/snd": {
                return parseSendMessage(fields);
            }
            default: {
                throw new IllegalArgumentException("Unknown command type: " + type);
            }
        }
    }

    private SendCommand parseSendMessage(String[] fields) {
        String messageField = fields[1];
        String message = parseField(messageField)[1];
        return new SendCommand(message);
    }

    private HistoryCommand parseHistMessage(String[] fields) {
        return new HistoryCommand();
    }

    private String[] parseField(String message) {
        int delimiterIndex = message.indexOf(HEAD_BODY_DELIMITER);
        if (delimiterIndex == -1) {
            throw new IllegalArgumentException("There is no body in field");
        }
        return new String[] {
                message.substring(0, delimiterIndex),
                message.substring(delimiterIndex + 1)
        };
    }

    private String getType(String message) {
        String[] headAndBody = parseField(message);
        String head = headAndBody[0], body = headAndBody[1];
        if (!head.equals("type")) {
            throw new IllegalArgumentException("Type should be specified in first field");
        }
        return body;
    }
}
