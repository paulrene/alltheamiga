package com.alltheamiga.storage.model;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("registration")
public class DiskRegistration {

    @XStreamAsAttribute
    private String source;
    @XStreamAsAttribute
    private String filename;
    @XStreamAsAttribute
    private Date ingestTime;
    private AmigaDisk disk;

    public DiskRegistration() {
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public AmigaDisk getDisk() {
        return disk;
    }
    
    public void setDisk(AmigaDisk disk) {
        this.disk = disk;
    }
    
    public Date getIngestTime() {
        return ingestTime;
    }
    
    public void setIngestTime(Date ingestTime) {
        this.ingestTime = ingestTime;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
}
