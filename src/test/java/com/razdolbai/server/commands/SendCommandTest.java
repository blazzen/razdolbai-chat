package com.razdolbai.server.commands;
import com.razdolbai.server.Decorator;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommandTest {
    private Saver mockSaver;
    private Session mockSession;
    private SessionStore mockSessionStore;
    private LocalDateTime timestamp;
    private String username;
    @Before
    public void setup(){
        mockSaver = mock(Saver.class);
        mockSession = mock(Session.class);
        mockSessionStore= mock(SessionStore.class);
        timestamp = LocalDateTime.now();
        username = "James";

    }
    @Test
    public void shouldSendCommand() throws UnidentifiedUserException, IOException {
        final String message = "HI WORLD!";
        final String decoratedMessage = Decorator.decorate(message,timestamp,username);
        SendCommand testSendCommand = new SendCommand(mockSession,mockSessionStore,message,mockSaver,timestamp);
        when(mockSession.getUsername()).thenReturn(username);
        testSendCommand.execute();
        verify(mockSession,times(2)).getUsername();
        verify(mockSessionStore).sendToAll(decoratedMessage);
        verify(mockSaver).save(decoratedMessage,timestamp);
    }
    @Test(expected = UnidentifiedUserException.class)
    public void shouldThrowUnidentifiedException() throws UnidentifiedUserException, IOException{
        Saver mockSaver = mock(Saver.class);
        Session mockSession = mock(Session.class);
        SessionStore mockSessionStore= mock(SessionStore.class);
        final LocalDateTime timestamp = LocalDateTime.now();
        SendCommand testSendCommand = new SendCommand(mockSession,mockSessionStore,"",mockSaver,timestamp);
        when(mockSession.getUsername()).thenReturn(null);
        testSendCommand.execute();
    }
}
