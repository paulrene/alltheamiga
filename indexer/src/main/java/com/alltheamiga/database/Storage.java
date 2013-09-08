package com.alltheamiga.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.alltheamiga.database.model.AmigaDisk;
import com.alltheamiga.database.model.AmigaFile;
import com.alltheamiga.database.model.Bitmap;
import com.alltheamiga.database.model.DiskRegistration;
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
    
    public AmigaFile findAmigaFileByFileDataHashAndFilename(String fileDataHash, String filename) {
        try {
            Query q = manager.createQuery("select a from AmigaFile a where a.name like :filename and a.fileData.hashCode like :hashCode", AmigaFile.class);
            q.setParameter("hashCode", fileDataHash);
            q.setParameter("filename", filename);
            @SuppressWarnings("unchecked")
            List<AmigaFile> amigaFileList = q.getResultList();
            if(amigaFileList.size()>0) {
                return amigaFileList.get(0);
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public AmigaFile findAmigaFileById(long fileId) {
        try {
            Query q = manager.createQuery("select d from AmigaFile d where d.id=:id", AmigaFile.class);
            q.setParameter("id", fileId);
            return (AmigaFile) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    
    /******** USED BY WEB (VELOCITY) ********/
    
    @SuppressWarnings("unchecked")
    public List<DiskRegistration> getDiskRegistrations() {
        Query q = manager.createQuery("select r from DiskRegistration r order by r.filename", DiskRegistration.class);
        List<DiskRegistration> list = (List<DiskRegistration>) q.getResultList();
        return list;
    }

    public AmigaDisk getDiskByHash(String hashCode) {
        return findAmigaDiskByHashCode(hashCode);
    }
    
}