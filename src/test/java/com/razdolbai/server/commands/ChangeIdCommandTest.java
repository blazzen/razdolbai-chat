package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.history.saver.SwitchingFileSaver;
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
    private Saver mockSaver;
    private ChangeIdCommand testChangeIdCommand;

    @Before
    public void setup() {
        mockIdentificator = mock(Identificator.class);
        newnickname = "Tom";
        oldnickname = "James";
        timestamp = LocalDateTime.now();
        mockSessionStore = mock(SessionStore.class);
        mockSession = mock(Session.class);
        mockSaver = mock(SwitchingFileSaver.class);
        testChangeIdCommand = new ChangeIdCommand(mockSession, mockIdentificator, newnickname,
                timestamp, mockSessionStore, mockSaver);
    }

    @Test
    public void shouldChangeIdTest() throws OccupiedNicknameException, IOException {
        String decoratedMessage = "[" +timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] "
                + oldnickname + " has changed name to " + newnickname;
        when(mockSession.getUsername()).thenReturn(oldnickname);
        doNothing().when(mockSaver).save(decoratedMessage, timestamp);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
        verify(mockIdentificator).changeNickname(oldnickname, newnickname);
        verify(mockSession).setUsername(newnickname);
        verify(mockSessionStore).sendToAll(decoratedMessage);
    }

    @Test
    public void shouldCreateIdTest() throws OccupiedNicknameException, IOException {
        String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] "
                + newnickname + " joined the chat";
        when(mockSession.getUsername()).thenReturn(null);
        doNothing().when(mockSaver).save(decoratedMessage, timestamp);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
        verify(mockIdentificator).changeNickname(null, newnickname);
        verify(mockSession).setUsername(newnickname);
        verify(mockSessionStore).sendToAll(decoratedMessage);
    }

    @Test(expected = OccupiedNicknameException.class)
    public void shouldThrowException() throws OccupiedNicknameException, IOException {
        when(mockSession.getUsername()).thenReturn(oldnickname);
        doThrow(new OccupiedNicknameException()).when(mockIdentificator).changeNickname(oldnickname, newnickname);
        testChangeIdCommand.execute();
        verify(mockSession).getUsername();
    }
}
