package com.razdolbai.client;

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

        assertThat(outContent.toString()).contains("Unknown command, try again");

    }

    @Test
    public void shouldReturnNullIfCommandIsNotInTheExistingCommands() {

        Command result = sut.parse("/send testtesttest");

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldWriteErrorMessageIfCommandIsTooLong() {

        sut.parse("/snd " + VERY_LONG_LINE);

        assertThat(outContent.toString()).contains("Message is too long, try again");

    }

    @Test
    public void shouldReturnNullIfCommandIsTooLong() {

        Command result = sut.parse(VERY_LONG_LINE);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldReturnCommandIfOnlyCommandTypeIsPassed() {

        Command result = sut.parse("/hist");

        assertThat(result.getCommandType()).isEqualTo("/hist");
        assertThat(result.getMessage()).isEqualTo("");
    }

    @Test
    public void shouldReturnCommandIfMessageAndCommandTypeIsPassed() {

        Command result = sut.parse("/snd testMsg");

        assertThat(result.getCommandType()).isEqualTo("/snd");
        assertThat(result.getMessage()).isEqualTo("testMsg");
    }

    @Test
    public void shouldReturnCommandIfMessageIsMaxLengthAndCommandTypeIsPassed() {

        Command result = sut.parse("/snd " + NOT_LONG_ENOUGH_LINE);

        assertThat(result.getCommandType()).isEqualTo("/snd");
        assertThat(result.getMessage()).isEqualTo(NOT_LONG_ENOUGH_LINE);
    }


}
