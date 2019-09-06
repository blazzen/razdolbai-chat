package com.razdolbai.server;

import com.razdolbai.server.commands.ChangeIdCommand;
import com.razdolbai.server.commands.CloseCommand;
import com.razdolbai.server.exceptions.ChatException;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatSessionTest {
    private ChatSession session;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private Socket socket;
    private CommandFactory commandFactory;
    ByteArrayOutputStream ERR = new ByteArrayOutputStream();


    @Before
    public void setUp() {
        socket = mock(Socket.class);
        socketIn = mock(BufferedReader.class);
        socketOut = mock(PrintWriter.class);
        commandFactory = mock(ChatCommandFactory.class);
        session = new ChatSession("user", socket, socketIn, socketOut, commandFactory);
        resetErr();
        captureSysErr();
    }

    @Test
    public void shouldSendMessageToSocketOut() {
        String message = "message";

        doNothing().when(socketOut).println(message);
        doNothing().when(socketOut).flush();

        session.send(message);

        verify(socketOut).println(message);
        verify(socketOut).flush();
    }

    @Test
    public void shouldSetClosedFlagToTrueWhenCloseInvoked() throws IOException {
        session.close();
        assertTrue((boolean) Whitebox.getInternalState(session, "isClosed"));
    }

    @Test
    public void shouldSetUsername() {
        String username = "user";
        session.setUsername(username);
        assertEquals(session.getUsername(), username);
    }

    @Test
    public void shouldHandleIOExceptionFromSocketIn() throws IOException, OccupiedNicknameException {
        doThrow(IOException.class).when(socketIn).readLine();
        session.run();
        assertSysErrContains(IOException.class.getName());
    }

    @Test
    public void shouldProccessCommandAndCloseSession() throws IOException, OccupiedNicknameException {
        testSessionRun(null);
    }

    @Test
    public void shouldProccessUnidentifiedUserException() throws IOException, OccupiedNicknameException {
        testSessionRun(UnidentifiedUserException.class);
    }

    @Test
    public void shouldProccessOccupiedNicknameException() throws IOException, OccupiedNicknameException {
        testSessionRun(OccupiedNicknameException.class);
    }

    @Test
    public void shouldProccessChatException() throws IOException, OccupiedNicknameException {
        testSessionRun(ChatException.class);
    }

    private void testSessionRun(Class exceptionCLass) throws IOException, OccupiedNicknameException {
        String firstMessage = "type:/chid\0msg:user";
        String secondMessage = "type:/close\0msg:";
        ChangeIdCommand firstCommandMock = mock(ChangeIdCommand.class);
        SessionStore sessionStoreMock = new ChatSessionStore();
        SessionStore sessionStoreSpy = spy(sessionStoreMock);
        ReadWriteLock readWriteLockMock = mock(ReentrantReadWriteLock.class);
        Collection<Session> sessionsMock = mock(Collection.class);
        Whitebox.setInternalState(sessionStoreMock, "rwl", readWriteLockMock);
        Whitebox.setInternalState(sessionStoreMock, "sessions", sessionsMock);
        ReentrantReadWriteLock.WriteLock wLockMock = mock(ReentrantReadWriteLock.WriteLock.class);
        CloseCommand secondCommandMock = new CloseCommand(session, sessionStoreSpy);

        when(socketIn.readLine()).thenReturn(firstMessage)
                .thenReturn(secondMessage);
        when(commandFactory.createCommand(any(Session.class), anyString(), any(LocalDateTime.class)))
                .thenReturn(firstCommandMock)
                .thenReturn(secondCommandMock);
        doNothing().when(wLockMock).lock();
        doNothing().when(wLockMock).unlock();
        when(readWriteLockMock.writeLock()).thenReturn(wLockMock);
        when(sessionsMock.remove(any(Session.class))).thenReturn(true);
        doCallRealMethod().when(sessionStoreSpy).remove(session);
        doNothing().when(sessionStoreSpy).sendToAll(anyString());
        if (exceptionCLass == null) {
            doNothing().when(firstCommandMock).execute();
            session.run();
        } else {
            doThrow(exceptionCLass).when(firstCommandMock).execute();
            session.run();
            assertSysErrContains(exceptionCLass.getName());
        }
    }

    private void assertSysErrContains(String expected) {
        assertThat(ERR.toString()).contains(expected);
    }

    private void captureSysErr() {
        System.setErr(new PrintStream(ERR));
    }

    private void resetErr() {
        ERR.reset();
    }
}
