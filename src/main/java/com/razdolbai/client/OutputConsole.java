package com.razdolbai.client;

import java.io.*;
import java.net.Socket;

public class OutputConsole {
    public static void main(String[] args) throws IOException {

        while (true) {
            try {
                Client.printToOther();
            } catch (NullPointerException ex) {
                continue;
            }

        }
    }
}

/*
    public static void printToOther() throws IOException {
        System.out.println(inFromSocket.readLine());
    }
 */