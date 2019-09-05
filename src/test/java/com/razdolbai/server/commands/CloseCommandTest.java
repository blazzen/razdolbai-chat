package com.razdolbai.server.commands;

import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CloseCommandTest {
    private Session mockSession;
    private SessionStore mockSessionStore;
    private String nickname;
    private CloseCommand testCloseCommand;
    @Before
    public void setup(){
        mockSession = mock(Session.class);
        mockSessionStore = mock(SessionStore.class);
        nickname = "Alex";
        testCloseCommand = new CloseCommand(mockSession,mockSessionStore);
    }
    @Test
    public void shouldCloseWithMessage(){
        when(mockSession.getUsername()).thenReturn(nickname);
        String message = nickname + " has left the chat";
        testCloseCommand.execute();
        verify(mockSession).getUsername();
        verify(mockSessionStore).sendToAll(message);
    }
    @Test
    public void shouldCloseWithoutMessage(){
        when(mockSession.getUsername()).thenReturn(null);
        testCloseCommand.execute();
        verify(mockSession).getUsername();
    }
}
