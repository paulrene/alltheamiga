package com.alltheamiga.indexer;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;

import com.alltheamiga.database.Storage;


public class IndexerMain {

    public static void main(String[] args) throws IOException, ZipException {
        Indexer indexer = new Indexer(new Storage(), new File("/Users/paulrene/alltheamiga/temp"));
        
        File f1 = new File("/Users/paulrene/alltheamiga/dropbox/AMS-One Macro Assembler v1.29 (1990)(Daten und Medien Verlag)[cr TFA].zip");
        File f2 = new File("/Users/paulrene/alltheamiga/dropbox/Deluxe Paint V v5.2 (1995)(Electronic Arts)(AGA)(Disk 1 of 4)(Program)[WB].zip");
        File f3 = new File("/Users/paulrene/alltheamiga/dropbox/Copylock Amiga (1989-05-03)(Rob Northen Computing)[cr QTX].zip");
        File f4 = new File("/Users/paulrene/alltheamiga/dropbox/Globaltr.adf");
        
        indexer.process(f1);
        indexer.process(f2);
        indexer.process(f3);
        indexer.process(f4);
    }

}
