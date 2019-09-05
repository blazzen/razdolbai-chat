package com.razdolbai.server.history.reader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SwitchingFileReader implements Reader {

    @Override
    public List<String> getHistory() {
        ArrayList<String> res  = new ArrayList<>(2097152 * 5);
        readAllLinesDFS(new File("./resources/History"), res);
        return res;
    }

    static void readAllLinesDFS(File folder, List<String> res) {
        for(File f : folder.listFiles()) {
            if(f.isDirectory()) {
                readAllLinesDFS(f, res);
            }

            if (f.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ( (line = reader.readLine()) != null ) {
                        res.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        /*
        for(String s : res) {
            System.out.println(s);
        }
        */

    }
}
