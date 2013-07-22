package com.alltheamiga.database.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "amigadirectories")
public class AmigaDirectory {
    
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String comment;
    private Date modified;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rootDirectory", optional = true)
    private AmigaDisk amigaDisk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentDirectoryId", nullable = true)
    private AmigaDirectory parentDirectory;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentDirectory")
    private Set<AmigaDirectory> subDirectories;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "directory")
    private Set<AmigaFile> files;

    public AmigaDirectory() {
        subDirectories = new HashSet<>();
        files = new HashSet<>();
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

    public AmigaDisk getAmigaDisk() {
        return amigaDisk;
    }

    public void setAmigaDisk(AmigaDisk amigaDisk) {
        this.amigaDisk = amigaDisk;
    }

    public AmigaDirectory getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(AmigaDirectory parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public Set<AmigaDirectory> getSubDirectories() {
        return subDirectories;
    }

    public void setSubDirectories(Set<AmigaDirectory> subDirectories) {
        this.subDirectories = subDirectories;
    }

    public Set<AmigaFile> getFiles() {
        return files;
    }

    public void setFiles(Set<AmigaFile> files) {
        this.files = files;
    }

    public Long getId() {
        return id;
    }
    
}