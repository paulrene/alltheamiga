package com.alltheamiga.database.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alltheamiga.database.model.types.Chipset;
import com.alltheamiga.database.model.types.Platform;
import com.alltheamiga.database.model.types.SoftwareCategory;
import com.alltheamiga.database.model.types.VideoMode;

@Entity
@Table(name = "diskregistrations")
public class DiskRegistration {

    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amigaDiskId")
    private AmigaDisk amigaDisk;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "diskRegistration")
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "diskRegistration")
    private Set<DiskRegistrationFlag> flags;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originUserId")
    private User originUser;

    @Enumerated(value = EnumType.STRING)
    private SoftwareCategory category;

    @Enumerated(value = EnumType.STRING)
    private Platform platform;
    
    @Enumerated(value = EnumType.STRING)
    private Chipset chipset;
    
    @Enumerated(value = EnumType.STRING)
    private VideoMode videoMode;
    
    private String title;
    private String filename;
    private String year;
    private String company;
    private String publisher;
    private String language;
    private String originUrl;
    private String copyrightStatus;
    private String demo;
    private String developmentStatus;
    private String mediaIndex;
    private String diskLabel;
    private Integer diskIndex;
    private Integer diskCount;
    private Date created;
    private Date updated;
    
    public DiskRegistration() {
        comments = new HashSet<>();
        flags = new HashSet<>();
    }
    
    public Integer getDiskCount() {
        return diskCount;
    }
    
    public void setDiskCount(Integer diskCount) {
        this.diskCount = diskCount;
    }
    
    public Integer getDiskIndex() {
        return diskIndex;
    }
    
    public void setDiskIndex(Integer diskIndex) {
        this.diskIndex = diskIndex;
    }
    
    public Set<DiskRegistrationFlag> getFlags() {
        return flags;
    }
    
    public void setFlags(Set<DiskRegistrationFlag> flags) {
        this.flags = flags;
    }

    public AmigaDisk getAmigaDisk() {
        return amigaDisk;
    }

    public void setAmigaDisk(AmigaDisk amigaDisk) {
        this.amigaDisk = amigaDisk;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    public SoftwareCategory getCategory() {
        return category;
    }

    public void setCategory(SoftwareCategory category) {
        this.category = category;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public VideoMode getVideoMode() {
        return videoMode;
    }
    
    public void setVideoMode(VideoMode videoMode) {
        this.videoMode = videoMode;
    }
    
    public Chipset getChipset() {
        return chipset;
    }
    
    public void setChipset(Chipset chipset) {
        this.chipset = chipset;
    }
    
    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
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
    
    public String getCopyrightStatus() {
        return copyrightStatus;
    }
    
    public void setCopyrightStatus(String copyrightStatus) {
        this.copyrightStatus = copyrightStatus;
    }

    public String getDemo() {
        return demo;
    }
    
    public void setDemo(String demo) {
        this.demo = demo;
    }
    
    public String getDevelopmentStatus() {
        return developmentStatus;
    }
    
    public void setDevelopmentStatus(String developmentStatus) {
        this.developmentStatus = developmentStatus;
    }
    
    public String getDiskLabel() {
        return diskLabel;
    }
    
    public void setDiskLabel(String diskLabel) {
        this.diskLabel = diskLabel;
    }
    
    public String getMediaIndex() {
        return mediaIndex;
    }
    
    public void setMediaIndex(String mediaIndex) {
        this.mediaIndex = mediaIndex;
    }
    
    public Long getId() {
        return id;
    }

}