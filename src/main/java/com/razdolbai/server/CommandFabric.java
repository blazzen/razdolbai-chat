package com.razdolbai.server;

import com.razdolbai.server.commands.*;

import java.util.Map;

public class CommandFabric {
    private final Parser parser = new Parser("\0", ":");
    private final Server server;

    public CommandFabric(Server server) {
        this.server = server;
    }

    public Command createCommand(String message) {
        Map<String, String> fieldMap = parser.parse(message);
        String type = fieldMap.get("type");
        switch (type) {
            case "/hist": {
                return createHistCommand(fieldMap);
            }
            case "/snd": {
                return createSendCommand(fieldMap);
            }
            case "/chid": {
                return createIdentificationCommand(fieldMap);
            }
            case "/close": {
                return createCloseCommand(fieldMap);
            }
            default: {
                throw new IllegalArgumentException("Unknown command type: " + type);
            }
        }
    }

    private Command createHistCommand(Map<String, String> fieldMap) {
        return new HistoryCommand();
    }

    private Command createCloseCommand(Map<String, String> fieldMap) {
        return new CloseCommand();
    }

    private SendCommand createSendCommand(Map<String, String> fieldMap) {
        String message = fieldMap.get("msg");
        return new SendCommand(message);
    }

    private IdentificationCommand createIdentificationCommand(Map<String, String> fieldMap) {
        String nickname = fieldMap.get("msg");
        return new IdentificationCommand(server.getIdentificator(), nickname);
    }
}
