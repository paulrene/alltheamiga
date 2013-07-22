package com.alltheamiga.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Storage {

    private EntityManager manager;
    
    public Storage() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AllTheAmiga");
        this.manager = emf.createEntityManager();
    }

    public void begin() {
        manager.getTransaction().begin();
    }
    
    public void commit() {
        manager.getTransaction().commit();
    }

    public void rollback() {
        manager.getTransaction().rollback();
    }
    
    public void persist(Object obj) {
        manager.persist(obj);
    }
    
}