package com.alltheamiga.database.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authorUser")
    private Set<Comment> comments;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originUser")
    private Set<DiskRegistration> diskRegistrations;
    
    private String name;
    private String username;
    private String email;
    private Date created;
    private Date updated;
    
    public User() {
        comments = new HashSet<>();
        diskRegistrations = new HashSet<>();
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<DiskRegistration> getDiskRegistrations() {
        return diskRegistrations;
    }

    public void setDiskRegistrations(Set<DiskRegistration> diskRegistrations) {
        this.diskRegistrations = diskRegistrations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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