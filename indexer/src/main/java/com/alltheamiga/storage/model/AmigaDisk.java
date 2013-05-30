package com.alltheamiga.storage.model;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("disk")
public class AmigaDisk {

    @XStreamAsAttribute
    private String volumeName;
    @XStreamAsAttribute
    private String hashCode;
    @XStreamAsAttribute
    private Integer usedBytes;
    @XStreamAsAttribute
    private Integer availableBytes;
    @XStreamAsAttribute
    private String fileSystemType;
    @XStreamAsAttribute
    private Boolean isFloppy;
    @XStreamAsAttribute
    private Boolean valid;
    @XStreamAsAttribute
    private Date created;
    @XStreamAsAttribute
    private Date modified;
    private AmigaDirectory rootDirectory;
    
    public AmigaDisk() {
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(String fileSystemType) {
        this.fileSystemType = fileSystemType;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getUsedBytes() {
        return usedBytes;
    }

    public void setUsedBytes(Integer usedBytes) {
        this.usedBytes = usedBytes;
    }

    public Integer getAvailableBytes() {
        return availableBytes;
    }
    
    public void setAvailableBytes(Integer availableBytes) {
        this.availableBytes = availableBytes;
    }
    
    public Boolean getIsFloppy() {
        return isFloppy;
    }

    public void setIsFloppy(Boolean isFloppy) {
        this.isFloppy = isFloppy;
    }

    public AmigaDirectory getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(AmigaDirectory rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Date getCreated() {
        return created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    
    public Date getModified() {
        return modified;
    }
    
    public void setModified(Date modified) {
        this.modified = modified;
    }
    
}
