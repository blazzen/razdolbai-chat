package com.razdolbai.server.history.reader;

import com.razdolbai.server.history.saver.SwitchingFileSaver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwitchingFileReader implements Reader {
    private static final int initialCapacity = 2097152 * 5;
    private String pathname = "./resources/History";
    //About half of a day

    public SwitchingFileReader(){}
    public SwitchingFileReader(String folder) {
        this.pathname = pathname + "/" + folder;
    }


    @Override
    public List<String> getHistory() {
        ArrayList<String> res  = new ArrayList<>(initialCapacity);
        readAllLinesDFS(new File(pathname), res);
        return res;
    }

    static void readAllLinesDFS(File folder, List<String> res) {
        if (folder == null) return;
        File[] files = folder.listFiles();
        if(files == null) return;
        Arrays.sort(files);
        for(File currentFile : files) {
            if(currentFile.isDirectory()) {
                readAllLinesDFS(currentFile, res);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
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
}
