package com.alltheamiga.indexer.amitools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XdfMeta {

    private String volumeName;
    private String dosType;
    private List<String> rootTimestamps;
    private List<MetaFile> metaFileList;
    
    private XdfMeta() {
        rootTimestamps = new ArrayList<>();
        metaFileList = new ArrayList<>();
    }
    
    public static XdfMeta fromFile(File file) {
        try (Scanner scanner = new Scanner(file, "latin1")) {
            XdfMeta meta = new XdfMeta();
            String headerStr = scanner.nextLine();
            String[] headerComponents = headerStr.split(",");
            String[] volumeInfo = headerComponents[0].split(":");
            meta.volumeName = volumeInfo[0];
            meta.dosType = volumeInfo[1];
            for(int n=1;n<headerComponents.length;n++) {
                meta.rootTimestamps.add(headerComponents[n]);
            }
            while(scanner.hasNextLine()) {
                String fileInfoStr = scanner.nextLine();
                String[] fileInfo = fileInfoStr.split(",");
                String[] fileInfoComponents = fileInfo[0].split(":");
                MetaFile metaFile = new MetaFile();
                metaFile.filePath = fileInfoComponents[0];
                metaFile.attributes = fileInfoComponents[1];
                metaFile.timestamp = fileInfo[1];
                if(fileInfo.length>2) {
                    metaFile.comment = fileInfo[2];
                }
                meta.metaFileList.add(metaFile);
            }
            return meta;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String toString() {
        StringBuilder o = new StringBuilder();
        o.append(volumeName+"("+dosType+")\n");
        for(MetaFile f : metaFileList) {
            o.append(f.toString()+"\n");
        }
        return o.toString();        
    }
    
    public String getDosType() {
        return dosType;
    }
    
    public List<String> getRootTimestamps() {
        return rootTimestamps;
    }
    
    public String getVolumeName() {
        return volumeName;
    }
    
    public List<MetaFile> getMetaFileList() {
        return metaFileList;
    }
    
    public static class MetaFile {
        private String filePath;
        private String attributes;
        private String timestamp;
        private String comment;
        
        public String toString() {
            return "FILE:"+filePath+", ATTRS:"+attributes+", TIME:"+timestamp+", COMMENT:"+comment;
        }
        
        public boolean isProtected() {
            return attributes.indexOf('p')!=-1;
        }
        
        public boolean isReadable() {
            return attributes.indexOf('r')!=-1;
        }

        public boolean isWriteable() {
            return attributes.indexOf('r')!=-1;
        }

        public boolean isDeletable() {
            return attributes.indexOf('r')!=-1;
        }
    }

}
