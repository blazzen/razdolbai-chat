package com.razdolbai.server;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatSessionFactoryTest {
    private ChatSessionFactory sessionFactory;
    private Socket socketMock;

    @Before
    public void setUp() {
        CommandFactory commandFactoryMock = mock(CommandFactory.class);
        socketMock = mock(Socket.class);
        sessionFactory = new ChatSessionFactory(commandFactoryMock);
    }

    @Test
    public void sessionReturnedWhenCreateSessionCalled() throws IOException {
        InputStream socketInMock = mock(InputStream.class);
        OutputStream socketOutMock = mock(OutputStream.class);
        when(socketMock.getInputStream()).thenReturn(socketInMock);
        when(socketMock.getOutputStream()).thenReturn(socketOutMock);

        Session session = sessionFactory.createSession(socketMock);
        System.out.println(session);
        verify(socketMock).getInputStream();
        verify(socketMock).getOutputStream();
        assertTrue(session instanceof ChatSession);
    }

    @Test(expected = IOException.class)
    public void throwExceptionWhenExceptionRoseInSocket() throws IOException {
        doThrow(IOException.class).when(socketMock).getInputStream();

        Session session = sessionFactory.createSession(socketMock);
        System.out.println(session);
        verify(socketMock).getInputStream();
        verify(socketMock, never()).getOutputStream();
    }
}
