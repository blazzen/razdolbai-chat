package com.razdolbai.client;

import com.razdolbai.common.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.mockito.Mockito.*;

public class ProxyTest {

    private Proxy sut;

    private SystemExit systemExitMock = mock(SystemExit.class);
    private PrintWriter printWriterMock = mock(PrintWriter.class);

    @Before
    public void setUp() {
        sut = new Proxy(printWriterMock, systemExitMock);

    }

    @Test
    public void shouldAddMessageIfMessageIsPresent() {
        doNothing().when(printWriterMock).println("type:" + CommandType.SEND.getValue() + "\0msg:testtest");
        doNothing().when(printWriterMock).flush();

        Command command = new Command(CommandType.SEND, "testtest");

        sut.send(command);

        verify(printWriterMock).println("type:" + CommandType.SEND.getValue() + "\0msg:testtest");
        verify(printWriterMock).flush();

    }

    @Test
    public void shouldNotAddMessageIfCommandDoesNotHaveMessage() {

        doNothing().when(printWriterMock).println("type:" + CommandType.HIST.getValue());
        doNothing().when(printWriterMock).flush();

        Command command = new Command(CommandType.HIST, "");

        sut.send(command);

        verify(printWriterMock).println("type:" + CommandType.HIST.getValue());
        verify(printWriterMock).flush();

    }

    @Test
    public void shouldExitIfCloseCommandIsPassed() {
        SystemExit systemExitMock = spy(SystemExit.class);
        Proxy sut = new Proxy(printWriterMock, systemExitMock);
        doNothing().when(systemExitMock).exit(0);

        Command command = new Command(CommandType.CLOSE, "");

        sut.send(command);

        verify(systemExitMock).exit(0);

    }

}
