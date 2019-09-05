package com.razdolbai.client;

import com.razdolbai.common.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class InputConsoleTest {

    private static final String TEST_MESSAGE = "testtest";
    private CommandSender commandSenderMock = mock(CommandSender.class);
    private BufferedReader readerMock = mock(BufferedReader.class);
    private InputParser inputParserMock = mock(InputParser.class);
    private Logger loggerSpy = spy(Logger.class);
    private InputConsole sut;

    @Before
    public void setUp() {

        sut = new InputConsole(commandSenderMock, readerMock, inputParserMock, loggerSpy);

    }

    @Test
    public void shouldParseTestAndSendCommandWhenIsInvoked() throws IOException {

        when(readerMock.readLine()).thenReturn(CommandType.SEND.getValue() + " " +TEST_MESSAGE);
        Command command = new Command(CommandType.SEND, TEST_MESSAGE);
        when(inputParserMock.parse(CommandType.SEND.getValue() + " " + TEST_MESSAGE)).thenReturn(command);
        doNothing().when(commandSenderMock).send(command);

        sut.readCommand();

        verify(commandSenderMock).send(command);
        verify(inputParserMock).parse(CommandType.SEND.getValue() + " " + TEST_MESSAGE);


    }

    @Test
    public void shouldParseTestButDoNotSendCommandWhenIsInvokedWithEmptyInput() throws IOException {

        when(readerMock.readLine()).thenReturn("");
        when(inputParserMock.parse("")).thenReturn(null);

        sut.readCommand();

        verifyZeroInteractions(commandSenderMock);
        verify(inputParserMock).parse("");

    }

    @Test()
    public void shouldNotThrowExceptionWhenReadLineThrowsException() throws IOException {
        when(readerMock.readLine()).thenThrow(new IOException());

        sut.readCommand();
    }
}
