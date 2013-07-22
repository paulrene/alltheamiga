package com.alltheamiga.database.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diskregistrationflags")
public class DiskRegistrationFlag {
    
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diskRegistrationId")
    private DiskRegistration diskRegistration;

    private String type;
    private Integer number;
    private String information;

    public DiskRegistrationFlag() {
    }
    
    public Long getId() {
        return id;
    }
    
    public DiskRegistration getDiskRegistration() {
        return diskRegistration;
    }
    
    public void setDiskRegistration(DiskRegistration diskRegistration) {
        this.diskRegistration = diskRegistration;
    }
    
    public String getInformation() {
        return information;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
    
    public Integer getNumber() {
        return number;
    }
    
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
}
