package com.razdolbai.server.commands;

import com.razdolbai.server.exceptions.ChatException;

public interface Command {
    public void execute() throws ChatException;
}
