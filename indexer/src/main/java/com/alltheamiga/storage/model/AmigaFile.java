package com.alltheamiga.storage.model;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("file")
public class AmigaFile {
    
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String hashCode;
    @XStreamAsAttribute
    private String comment;
    @XStreamAsAttribute
    private Integer size;
    @XStreamAsAttribute
    private Date modified;

    public AmigaFile() {
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Date getModified() {
        return modified;
    }
    
    public void setModified(Date modified) {
        this.modified = modified;
    }
    
}
