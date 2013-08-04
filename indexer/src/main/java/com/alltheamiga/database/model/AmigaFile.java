package com.alltheamiga.database.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "amigafiles")
public class AmigaFile {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directoryId")
    private AmigaDirectory directory;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fileDataId")
    private FileData fileData;
    
    private String name;
    private String comment;
    private Date modified;
    
    public AmigaFile() {
    }

    public FileData getFileData() {
        return fileData;
    }
    
    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
        
    public AmigaDirectory getDirectory() {
        return directory;
    }

    public void setDirectory(AmigaDirectory directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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