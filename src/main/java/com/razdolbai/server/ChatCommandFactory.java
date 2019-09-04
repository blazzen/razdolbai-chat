package com.razdolbai.server;

import com.razdolbai.server.commands.*;

import java.util.Map;

public class ChatCommandFactory {
    private final Parser parser;
    private final SessionStore sessionStore;
    private final Saver saver;
    private final Identificator identificator;

    public ChatCommandFactory(Parser parser,
                              SessionStore sessionStore,
                              Saver saver,
                              Identificator identificator) {
        this.parser = parser;
        this.sessionStore = sessionStore;
        this.saver = saver;
        this.identificator = identificator;
    }

    public Command createCommand(Session session, String message) {
        Map<String, String> fieldMap = parser.parse(message);
        String type = fieldMap.get("type");
        switch (type) {
            case "/hist": {
                return createHistCommand(session);
            }
            case "/snd": {
                return createSendCommand(session, fieldMap);
            }
            case "/chid": {
                return createChangeIdCommand(session, fieldMap);
            }
            case "/close": {
                return createCloseCommand(session);
            }
            default: {
                throw new IllegalArgumentException("Unknown command type: " + type);
            }
        }
    }

    private Command createHistCommand(Session session) {
        return new HistoryCommand(session);
    }

    private Command createCloseCommand(Session session) {
        return new CloseCommand(session);
    }

    private SendCommand createSendCommand(Session session, Map<String, String> fieldMap) {
        String message = fieldMap.get("msg");
        return new SendCommand(session, message);
    }

    private ChangeIdCommand createChangeIdCommand(Session session, Map<String, String> fieldMap) {
        String newNickname = fieldMap.get("msg");
        return new ChangeIdCommand(session, identificator, newNickname);
    }
}
