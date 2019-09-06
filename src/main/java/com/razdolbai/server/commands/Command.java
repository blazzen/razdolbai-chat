package com.razdolbai.server.commands;

import com.razdolbai.server.exceptions.ChatException;

import java.io.IOException;

public interface Command {
    public void execute() throws ChatException, IOException;
}
