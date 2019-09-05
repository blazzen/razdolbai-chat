package com.razdolbai.server;

import com.razdolbai.server.commands.Command;

public interface CommandFactory {
    Command createCommand(Session senderSession, String message, String timeStamp);
}
