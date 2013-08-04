package com.alltheamiga.indexer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.exception.ZipException;

import com.alltheamiga.database.Storage;

public class IndexerMain {

    private File scanDir;
    private Indexer indexer;

    public IndexerMain(Indexer indexer, File scanDir) {
        this.scanDir = scanDir;
        this.indexer = indexer;
    }

    public static void main(String[] args) throws IOException, ZipException {
        if (args == null || args.length == 0) {
            System.out.println("Usage: java -DamitoolsHome={} -DworkDir={} -DscanDir={} -jar indexer.jar start");
            return;
        }

        File workDir = new File(System.getProperty("workDir", "/Users/paulrene/alltheamiga/temp"));
        if (!workDir.exists()) {
            throw new IllegalArgumentException("Work directory does not exist!");
        }
        Indexer indexer = new Indexer(new Storage(), workDir);


        new IndexerMain(indexer, new File(System.getProperty("scanDir", "/Users/paulrene/alltheamiga/dropbox"))).run();
    }

    private void run() {
        while (true) {
            try {
                doScan(scanDir);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
            }
        }
    }

    private void doScan(File dir) {
        if (!dir.canRead()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(file);
            } else {
                try {
                    if (!file.isHidden()) {
                        indexer.process(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ZipException e) {
                    e.printStackTrace();
                }
            }
            FileUtils.deleteQuietly(file);
        }
    }

}
