package com.razdolbai.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Decorator {
    private static String leaveMessage = " has left the chat";
    private static String changeNameMessage = " has changed name to ";
    private static String joinMessage = " joined to chat";
    private static String chatHistoryMessage = "Chat history: ";
    private static String openBracket = "[";
    private static String closeBracket = "] ";
    private static String delim = ": ";
    public static String leftMessage(String message) {
        return message + leaveMessage;
    }
    public static String changeNameMessage(String oldnickname, String newnickname){
        return oldnickname + changeNameMessage + newnickname;
    }
    public static String joinMessage(String nickname){
        return nickname + joinMessage;
    }
    public static String formatTimestamp(LocalDateTime timestamp){
        return openBracket + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + closeBracket;
    }
    public static String decorate(String message, LocalDateTime timestamp){
        return formatTimestamp(timestamp) + message;
    }
    public static String decorate(String message,LocalDateTime timestamp, String nickname) {
        return formatTimestamp(timestamp) +
                nickname + delim +
                message;
    }

    public static String getChatHistoryMessage(){
        return chatHistoryMessage;
    }
}
