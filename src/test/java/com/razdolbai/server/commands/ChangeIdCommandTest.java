package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class ChangeIdCommandTest {
    private Identificator mockIdentificator;
    private String newnickname;
    private String oldnickname;
    private LocalDateTime timestamp;
    private SessionStore mockSessionStore;
    private Session mockSession;
    private ChangeIdCommand testChangeIdCommand;
    private Saver mockSaver;
    @Before
    public void setup(){
        mockIdentificator = mock(Identificator.class);
        newnickname = "Tom";
        oldnickname = "James";
        timestamp = LocalDateTime.now();
        mockSessionStore = mock(SessionStore.class);
        mockSession = mock(Session.class);
        mockSaver = mock(Saver.class);
        testChangeIdCommand = new ChangeIdCommand(mockSession,mockIdentificator,newnickname,timestamp,mockSessionStore,mockSaver);
    }

    @Test

    public void shouldChangeIdTest() throws OccupiedNicknameException, IOException {
        String decoratedMessage = "[" +timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] "
                + oldnickname + " has changed name to " + newnickname;
        when(mockSession.getUsername()).thenReturn(oldnickname);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
        verify(mockIdentificator).changeNickname(oldnickname,newnickname);
        verify(mockSession).setUsername(newnickname);
        verify(mockSessionStore).sendToAll(decoratedMessage);
        verify(mockSaver).save(decoratedMessage,timestamp);
    }
    @Test
    public void shouldCreateIdTest() throws OccupiedNicknameException, IOException {
        String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] "
                + newnickname + " joined the chat";
        when(mockSession.getUsername()).thenReturn(null);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
        verify(mockIdentificator).changeNickname(null,newnickname);
        verify(mockSession).setUsername(newnickname);
        verify(mockSessionStore).sendToAll(decoratedMessage);
        verify(mockSaver).save(decoratedMessage,timestamp);
    }
    @Test(expected = OccupiedNicknameException.class )
    public void shouldThrowException() throws OccupiedNicknameException, IOException{
        when(mockSession.getUsername()).thenReturn(oldnickname);
        doThrow(new OccupiedNicknameException()).when(mockIdentificator).changeNickname(oldnickname,newnickname);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
    }
}

