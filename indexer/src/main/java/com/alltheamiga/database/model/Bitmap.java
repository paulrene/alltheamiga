package com.alltheamiga.database.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bitmaps")
public class Bitmap {
    
    @Id @GeneratedValue
    private Long id;
    
    private Boolean highlighted;
    private String hashCode;
    @Column(length = 901120)
    private byte[] data;
    
    @ManyToMany
    @JoinTable(name = "filedata_bitmaps") 
    private Set<FileData> fileDatas;
    
    public Bitmap() {
        fileDatas = new HashSet<>();
    }

    public Boolean getHighlighted() {
        return highlighted;
    }
    
    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }
        
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
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
    
    public Set<FileData> getFileDatas() {
        return fileDatas;
    }
    
    public void setFileDatas(Set<FileData> fileDatas) {
        this.fileDatas = fileDatas;
    }
    
}