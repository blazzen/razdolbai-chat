package com.razdolbai.server;

import com.razdolbai.server.commands.Command;

import java.time.LocalDateTime;

public interface CommandFactory {
    Command createCommand(Session senderSession, String message, LocalDateTime timeStamp);
}
