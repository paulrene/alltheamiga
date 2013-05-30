package com.alltheamiga.storage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;

import org.dmpp.adf.app.Directory;
import org.dmpp.adf.app.DosFile;
import org.dmpp.adf.app.UserFile;
import org.dmpp.adf.app.UserVolume;
import org.dmpp.adf.logical.LogicalVolume;
import org.dmpp.adf.physical.PhysicalVolume;
import org.dmpp.adf.physical.PhysicalVolumeFactory;

import scala.collection.Iterator;
import scala.collection.immutable.List;

import com.alltheamiga.storage.model.AmigaDirectory;
import com.alltheamiga.storage.model.AmigaDisk;
import com.alltheamiga.storage.model.AmigaFile;
import com.alltheamiga.storage.model.DiskDatabase;
import com.alltheamiga.storage.model.DiskRegistration;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Storage {

    private File diskFileDirectory;
    private File databaseFile;
    private DiskDatabase database;
    private XStream xs;

    public Storage(File databaseFile, File diskFileDirectory) {
        this.diskFileDirectory = diskFileDirectory;
        this.databaseFile = databaseFile;
        this.xs = createXStream();
        if (this.databaseFile.exists()) {
            this.database = (DiskDatabase) xs.fromXML(databaseFile);
        } else {
            this.database = new DiskDatabase();
        }
    }

    private XStream createXStream() {
        XStream xs = new XStream(new StaxDriver());
        xs.processAnnotations(AmigaDisk.class);
        xs.processAnnotations(AmigaDirectory.class);
        xs.processAnnotations(AmigaFile.class);
        xs.processAnnotations(DiskDatabase.class);
        xs.processAnnotations(DiskRegistration.class);
        return xs;
    }

    public void addFloppyDiskImage(String source, File diskImageFile) throws NoSuchAlgorithmException, IOException {
        // Create Registration
        DiskRegistration diskReg = new DiskRegistration();
        diskReg.setFilename(diskImageFile.getName());
        diskReg.setIngestTime(new Date());
        diskReg.setSource(source);

        // Read ADF image and create UserVolume
        UserVolume userVolume = loadUserVolume(diskImageFile);

        // Create Disk
        AmigaDisk disk = createAmigaDiskFromUserVolume(userVolume);
        disk.setHashCode(sha1OfFile(diskImageFile));

        disk.setRootDirectory(createAmigaDirectoryFromDirectory(userVolume.rootDirectory()));

        diskImageFile.renameTo(new File(diskFileDirectory.getPath(), disk.getHashCode()+".adf"));

        diskReg.setDisk(disk);
        addDiskRegistration(diskReg);

    }

    private AmigaDirectory createAmigaDirectoryFromDirectory(Directory directory) {
        AmigaDirectory amigaDirectory = new AmigaDirectory();
        amigaDirectory.setComment(directory.comment());
        amigaDirectory.setModified(directory.lastModificationTime());
        amigaDirectory.setName(directory.name());

        List<DosFile> directoryList = directory.listDirectories();
        for (Iterator<DosFile> i = directoryList.iterator(); i.hasNext();) {
            amigaDirectory.getSubDirectories().add(createAmigaDirectoryFromDirectory((Directory) i.next()));
        }

        List<DosFile> fileList = directory.list();
        for (Iterator<DosFile> i = fileList.iterator(); i.hasNext();) {
            DosFile file = i.next();
            if (!file.isFile()) {
                continue;
            }
            AmigaFile amigaFile = createAmigaFileFromFile((UserFile) file);
            amigaDirectory.getFiles().add(amigaFile);
        }

        return amigaDirectory;
    }

    private AmigaFile createAmigaFileFromFile(UserFile file) {
        AmigaFile amigaFile = new AmigaFile();
        amigaFile.setComment(file.comment());
        amigaFile.setModified(file.lastModificationTime());
        amigaFile.setName(file.name());
        amigaFile.setSize(file.size());
        try {
            amigaFile.setHashCode(sha1OfByteArray(file.dataBytes()));
        } catch (UnsupportedOperationException e) {
            amigaFile.setHashCode(null);
        } catch (IllegalStateException e) {
            amigaFile.setHashCode(null);
        }
        return amigaFile;
    }

    private AmigaDisk createAmigaDiskFromUserVolume(UserVolume userVolume) {
        AmigaDisk disk = new AmigaDisk();
        disk.setCreated(userVolume.creationTime());
        disk.setFileSystemType(userVolume.filesystemType());
        disk.setIsFloppy(true);
        disk.setModified(userVolume.lastModificationTime());
        disk.setAvailableBytes(userVolume.numBytesAvailable());
        disk.setUsedBytes(userVolume.numBytesUsed());
        disk.setValid(userVolume.isValid());
        disk.setVolumeName(userVolume.name());
        return disk;
    }

    private UserVolume loadUserVolume(File diskImageFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(diskImageFile);
        PhysicalVolume physicalVolume = PhysicalVolumeFactory.readDoubleDensityDisk(fis);
        return new UserVolume(new LogicalVolume(physicalVolume));
    }

    public boolean addDiskRegistration(DiskRegistration diskRegistration) {
        synchronized (this) {
            java.util.List<DiskRegistration> diskRegistrations = database.getDiskRegistrations();
            for (DiskRegistration dr : diskRegistrations) {
                if(diskRegistration.getDisk().getHashCode().equals(dr.getDisk().getHashCode())) {
                    System.err.println("Disk "+diskRegistration.getDisk().getVolumeName()+" with hash "+diskRegistration.getDisk().getHashCode()+" is already in the database.");
                    return false;
                }
            }
            database.getDiskRegistrations().add(diskRegistration);
            return true;
        }
    }

    public void persist() throws IOException {
        synchronized (this) {
            if (databaseFile.exists()) {
                if (!databaseFile.renameTo(new File(databaseFile.getPath(), databaseFile.getName() + ".bak"))) {
                    System.err.println("Warning: Could not rename old database file before persisting. Will overwrite old file.");
                }
            }
            FileOutputStream out = new FileOutputStream(databaseFile);
            database.setLastSaved(new Date());
            xs.toXML(database, out);
            out.close();
        }
    }

    private static String sha1OfByteArray(byte[] dataBytes) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(dataBytes);
            return convertMessageDigestToHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String sha1OfFile(final File fileToHash) throws FileNotFoundException {
        return sha1OfInputStream(new FileInputStream(fileToHash));
    }

    private static String sha1OfInputStream(final InputStream inputStream) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            // Generate hash
            try (InputStream is = new BufferedInputStream(inputStream)) {
                final byte[] buffer = new byte[1024];
                for (int read = 0; (read = is.read(buffer)) != -1;) {
                    messageDigest.update(buffer, 0, read);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return convertMessageDigestToHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private static String convertMessageDigestToHexString(final MessageDigest messageDigest) {
        // Convert the byte to hex format
        try (Formatter formatter = new Formatter()) {
            for (final byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

}
