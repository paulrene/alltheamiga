package com.alltheamiga.storage.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("directory")
public class AmigaDirectory {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String comment;
    @XStreamAsAttribute
    private Date modified;
    
    @XStreamImplicit
    private List<AmigaDirectory> subDirectories;
    @XStreamImplicit
    private List<AmigaFile> files;

    public AmigaDirectory() {
        this.subDirectories = new ArrayList<>();
        this.files = new ArrayList<>();
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

    public List<AmigaDirectory> getSubDirectories() {
        return subDirectories;
    }

    public void setSubDirectories(List<AmigaDirectory> subDirectories) {
        this.subDirectories = subDirectories;
    }

    public List<AmigaFile> getFiles() {
        return files;
    }

    public void setFiles(List<AmigaFile> files) {
        this.files = files;
    }

}
