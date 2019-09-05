package com.razdolbai.client;

import com.razdolbai.common.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.fest.assertions.Assertions.assertThat;

public class InputParserTest {

    private static final String VERY_LONG_LINE = "ettesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    private static final String NOT_LONG_ENOUGH_LINE = "etesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private InputParser sut;

    @Before
    public void setUp() {
        sut = new InputParser();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void shouldWriteErrorMessageIfCommandIsNotInTheExistingCommands() {

        sut.parse("/send testtesttest");

        assertThat(outContent.toString()).contains("Unknown command");

    }

    @Test
    public void shouldReturnNullIfCommandIsNotInTheExistingCommands() {

        Command result = sut.parse("/send testtesttest");

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldWriteErrorMessageIfCommandIsTooLong() {

        sut.parse(CommandType.SEND.getValue() + " " + VERY_LONG_LINE);

        assertThat(outContent.toString()).contains("Message is too long, try again");

    }

    @Test
    public void shouldReturnNullIfCommandIsTooLong() {

        Command result = sut.parse(VERY_LONG_LINE);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldReturnCommandIfOnlyCommandTypeIsPassed() {

        Command result = sut.parse(CommandType.HIST.getValue());

        assertThat(result.getType()).isEqualTo(CommandType.HIST);
        assertThat(result.getMessage()).isEqualTo("");
    }

    @Test
    public void shouldReturnCommandIfMessageAndCommandTypeIsPassed() {

        Command result = sut.parse(CommandType.SEND.getValue() + " testMsg");

        assertThat(result.getType()).isEqualTo(CommandType.SEND);
        assertThat(result.getMessage()).isEqualTo("testMsg");
    }

    @Test
    public void shouldReturnCommandIfMessageIsMaxLengthAndCommandTypeIsPassed() {

        Command result = sut.parse(CommandType.SEND.getValue() + " " + NOT_LONG_ENOUGH_LINE);

        assertThat(result.getType()).isEqualTo(CommandType.SEND);
        assertThat(result.getMessage()).isEqualTo(NOT_LONG_ENOUGH_LINE);
    }


}
