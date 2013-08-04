package com.alltheamiga.database.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "filedata")
public class FileData {

    @Id @GeneratedValue
    private Long id;
    
    private String hashCode;
    @Column(length = 901120)
    private byte[] data;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fileData")
    private Set<AmigaFile> amigaFiles;
    private String version;
    private Integer size;
    
    @ManyToMany(mappedBy = "fileDatas", cascade=CascadeType.PERSIST)      
    private Set<Bitmap> bitmaps;

    public FileData() {
        amigaFiles = new HashSet<>();
        bitmaps = new HashSet<>();
    }
    
    public Set<Bitmap> getBitmaps() {
        return bitmaps;
    }
    
    public void setBitmaps(Set<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public Set<AmigaFile> getAmigaFiles() {
        return amigaFiles;
    }
    
    public void setAmigaFiles(Set<AmigaFile> amigaFiles) {
        this.amigaFiles = amigaFiles;
    }
    
    public String getHashCode() {
        return hashCode;
    }
    
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
    
    public Long getId() {
        return id;
    }

}
