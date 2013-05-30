package com.alltheamiga.storage.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("database")
public class DiskDatabase {

    @XStreamAsAttribute
    private Date lastSaved;
    private List<DiskRegistration> diskRegistrations;
    
    public DiskDatabase() {
        this.diskRegistrations = new ArrayList<>();
    }
    
    public List<DiskRegistration> getDiskRegistrations() {
        return diskRegistrations;
    }
    
    public void setDiskRegistrations(List<DiskRegistration> diskRegistrations) {
        this.diskRegistrations = diskRegistrations;
    }
    
    public Date getLastSaved() {
        return lastSaved;
    }
    
    public void setLastSaved(Date lastSaved) {
        this.lastSaved = lastSaved;
    }
    
}
