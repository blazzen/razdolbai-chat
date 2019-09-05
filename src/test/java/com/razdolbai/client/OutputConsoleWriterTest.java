package com.razdolbai.client;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class OutputConsoleWriterTest {

    private PrintWriter printWriterMock = mock(PrintWriter.class);
    private BufferedReader readerMock = mock(BufferedReader.class);
    private Logger loggerSpy = spy(Logger.getLogger("Test"));
    private OutputConsoleWriter sut;

    @Before
    public void setUp() {
        sut = new OutputConsoleWriter(printWriterMock, readerMock, loggerSpy);
    }

    @Test
    public void shouldPrintAndFlushWhenInputIsNotNull() throws IOException {

        when(readerMock.readLine()).thenReturn("input str");
        doNothing().when(printWriterMock).println("input str");

        sut.processInput();

        verify(readerMock).readLine();
        verify(printWriterMock).println("input str");
        verify(printWriterMock).flush();
    }

    @Test
    public void shouldNotPrintAndFlushWhenInputIsEmpty() throws IOException {

        when(readerMock.readLine()).thenReturn("");

        sut.processInput();

        verify(readerMock).readLine();
        verifyZeroInteractions(printWriterMock);
    }

    @Test
    public void shouldNotPrintAndFlushWhenInputIsNull() throws IOException {
        when(readerMock.readLine()).thenReturn(null);

        sut.processInput();

        verify(readerMock).readLine();
        verifyZeroInteractions(printWriterMock);
    }
}
