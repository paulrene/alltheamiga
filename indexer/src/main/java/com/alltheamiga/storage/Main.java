package com.alltheamiga.storage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

/**
 * 
 * Storage Main
 * 
 * @author paulrene
 * 
 */
public class Main {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        if(args.length!=4) {
            System.err.println("Usage: Storage <databaseFilename> <databaseFileStoragePath> <dropboxForDiskImagesPath> <deleteFilesFromDropboxAfterIngest?>");
            System.exit(0);
        }
        
        String databaseFilename = args[0];
        String databaseFileStoragePath = args[1];
        String dropboxForDiskImagesPath = args[2];
        Boolean deleteFilesFromDropboxAfterIngest = new Boolean(args[3]);
        
        Storage storage = new Storage(new File(databaseFilename), new File(databaseFileStoragePath));

        try {
            File dropbox = new File(dropboxForDiskImagesPath);
            for(Iterator<File> i = FileUtils.iterateFiles(dropbox, new String[] { "adf" }, true);i.hasNext();) {
                File file = i.next();
                try {
                    System.out.print("Disk "+file.getName());
                    storage.addFloppyDiskImage("internal", file);
                    System.out.println(" added to database.");
                    if(deleteFilesFromDropboxAfterIngest) {
                        file.delete();
                    }
                } catch (RuntimeException e) {
                    System.err.println(" caused an Excepton: "+e.getClass().getName()+" ("+e.getMessage()+")");
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        storage.persist();
    }
}
