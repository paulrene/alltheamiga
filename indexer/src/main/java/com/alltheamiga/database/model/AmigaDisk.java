package com.alltheamiga.database.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "amigadisks")
public class AmigaDisk {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "rootDirectoryId")
    private AmigaDirectory rootDirectory;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "amigaDisk")
    private Set<DiskRegistration> diskRegistrations;
    
    private String volumeName;
    private String hashCode;
    private Integer usedBytes;
    private Integer availableBytes;
    private String fileSystemType;
    private Boolean isFloppy;
    private Boolean valid;
    @Column(length = 901120)
    private byte[] data;
    @Column(length = 1024)
    private byte[] bootBlockData;
    private Date created;
    private Date modified;
    
    public AmigaDisk() {
        diskRegistrations = new HashSet<>();
    }
    
    public Set<DiskRegistration> getDiskRegistrations() {
        return diskRegistrations;
    }
    
    public void setDiskRegistrations(Set<DiskRegistration> diskRegistrations) {
        this.diskRegistrations = diskRegistrations;
    }
    
    public byte[] getBootBlockData() {
        return bootBlockData;
    }
    
    public void setBootBlockData(byte[] bootBlockData) {
        this.bootBlockData = bootBlockData;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public AmigaDirectory getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(AmigaDirectory rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
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

    public String getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(String fileSystemType) {
        this.fileSystemType = fileSystemType;
    }

    public Boolean getIsFloppy() {
        return isFloppy;
    }

    public void setIsFloppy(Boolean isFloppy) {
        this.isFloppy = isFloppy;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
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

    public Long getId() {
        return id;
    }

}