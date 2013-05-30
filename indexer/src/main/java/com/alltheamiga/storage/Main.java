package com.alltheamiga.storage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Storage Main
 * 
 * @author paulrene
 * 
 */
public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Storage storage = new Storage(new File("database.xml"), new File("diskfiles"));

        try {
            File dropbox = new File("dropbox");
            File[] filesInDropbox = dropbox.listFiles();
            for (File file : filesInDropbox) {
                storage.addFloppyDiskImage("admin", file);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        storage.persist();
    }
}
