package com.razdolbai.server;

import com.razdolbai.common.CommandType;
import com.razdolbai.server.commands.*;

import java.util.Map;

public class ChatCommandFactory implements CommandFactory {
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

    @Override
    public Command createCommand(Session session, String message, String timestamp) {
        Map<String, String> fieldMap = parser.parse(message);
        String type = fieldMap.get("type");
        CommandType commandType = CommandType.fromString(type);
        switch (commandType) {
            case HIST:
                return createHistCommand(session);
            case SEND:
                return createSendCommand(session, fieldMap, timestamp);
            case CHID:
                return createChangeIdCommand(session, fieldMap, timestamp);
            case CLOSE:
                return createCloseCommand(session);
            default:
                throw new IllegalArgumentException("Unknown command type: " + type);
        }
    }

    private Command createHistCommand(Session session) {
        return new HistoryCommand(session);
    }

    private Command createCloseCommand(Session session) {
        return new CloseCommand(session, sessionStore);
    }

    private SendCommand createSendCommand(Session session, Map<String, String> fieldMap, String timestamp) {
        String message = fieldMap.get("msg");
        return new SendCommand(session, sessionStore, message, saver, timestamp);
    }

    private ChangeIdCommand createChangeIdCommand(Session session, Map<String, String> fieldMap, String timestamp) {
        String newNickname = fieldMap.get("msg");
        return new ChangeIdCommand(session, identificator, newNickname, timestamp, sessionStore);
    }
}
