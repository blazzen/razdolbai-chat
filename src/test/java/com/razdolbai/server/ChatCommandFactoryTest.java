package com.razdolbai.server;

import com.razdolbai.server.commands.*;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.history.saver.SwitchingFileSaver;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatCommandFactoryTest {
    private Parser parser;
    private SessionStore sessionStore;
    private Saver saver;
    private Identificator identificator;
    private Session session;
    private ChatCommandFactory commandFactory;

    @Before
    public void setUp() {
        parser = mock(Parser.class);
        sessionStore = mock(ChatSessionStore.class);
        saver = mock(SwitchingFileSaver.class);
        identificator = mock(Identificator.class);
        session = mock(ChatSession.class);
        commandFactory = new ChatCommandFactory(parser, sessionStore, saver, identificator);
    }

    @Test
    public void shouldReturnHistoryCommandWhenReceivedHist() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String message = "/hist";

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("type", "/hist");
        fieldMap.put("msg", "");

        when(parser.parse(message)).thenReturn(fieldMap);

        Command command = commandFactory.createCommand(session, message, timeStamp);

        verify(parser).parse(message);
        assertTrue(command instanceof HistoryCommand);
    }

    @Test
    public void shouldReturnSendCommandWhenReceivedSnd() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String message = "/snd";

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("type", "/snd");
        fieldMap.put("msg", "msg");

        when(parser.parse(message)).thenReturn(fieldMap);

        Command command = commandFactory.createCommand(session, message, timeStamp);

        verify(parser).parse(message);
        assertTrue(command instanceof SendCommand);
    }

    @Test
    public void shouldReturnChangeIdCommandWhenReceivedChid() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String message = "/chid";

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("type", "/chid");
        fieldMap.put("msg", "msg");

        when(parser.parse(message)).thenReturn(fieldMap);

        Command command = commandFactory.createCommand(session, message, timeStamp);

        verify(parser).parse(message);
        assertTrue(command instanceof ChangeIdCommand);
    }

    @Test
    public void shouldReturnCloseCommandWhenReceivedClose() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String message = "/close";

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("type", "/close");
        fieldMap.put("msg", "");

        when(parser.parse(message)).thenReturn(fieldMap);

        Command command = commandFactory.createCommand(session, message, timeStamp);

        verify(parser).parse(message);
        assertTrue(command instanceof CloseCommand);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenReceivedUnknownCommand() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String message = "/unknown";

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("type", "/unknown");

        when(parser.parse(message)).thenReturn(fieldMap);

        Command command = commandFactory.createCommand(session, message, timeStamp);
        System.out.println(command);

        verify(parser).parse(message);
    }
}
