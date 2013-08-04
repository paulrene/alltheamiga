package com.alltheamiga.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.alltheamiga.database.model.AmigaDisk;
import com.alltheamiga.database.model.Bitmap;
import com.alltheamiga.database.model.FileData;

public class Storage {

    private EntityManager manager;

    public Storage() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AllTheAmiga");
        this.manager = emf.createEntityManager();
        this.manager.setFlushMode(FlushModeType.AUTO);
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

    public Bitmap findBitmapByHashCode(String hashCode) {
        try {
            Query q = manager.createQuery("select b from Bitmap b where b.hashCode like :hashCode", Bitmap.class);
            q.setParameter("hashCode", hashCode);
            return (Bitmap) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public FileData findFileDataByHashCode(String hashCode) {
        try {
            Query q = manager.createQuery("select f from FileData f where f.hashCode like :hashCode", FileData.class);
            q.setParameter("hashCode", hashCode);
            return (FileData) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public AmigaDisk findAmigaDiskByHashCode(String hashCode) {
        try {
            Query q = manager.createQuery("select d from AmigaDisk d where d.hashCode like :hashCode", AmigaDisk.class);
            q.setParameter("hashCode", hashCode);
            return (AmigaDisk) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}