package com.alltheamiga.database.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {

    @Id @GeneratedValue
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorUserId")
    private User authorUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diskRegistrationId")
    private DiskRegistration diskRegistration;
    
    private Integer rating;
    private String content;
    private Date created;
    private Date updated;
    
    public Comment() {
    }

    public User getAuthorUser() {
        return authorUser;
    }

    public void setAuthorUser(User authorUser) {
        this.authorUser = authorUser;
    }

    public DiskRegistration getDiskRegistration() {
        return diskRegistration;
    }

    public void setDiskRegistration(DiskRegistration diskRegistration) {
        this.diskRegistration = diskRegistration;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }
    
}