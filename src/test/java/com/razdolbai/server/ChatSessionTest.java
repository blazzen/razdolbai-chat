package com.razdolbai.server;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatSessionTest {
    private ChatSession session;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private Socket socket;

    @Before
    public void setUp() {
        socket = mock(Socket.class);
        socketIn = mock(BufferedReader.class);
        socketOut = mock(PrintWriter.class);
        CommandFactory commandFactory = mock(ChatCommandFactory.class);
        session = new ChatSession("user", socket, socketIn, socketOut, commandFactory);
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
}
