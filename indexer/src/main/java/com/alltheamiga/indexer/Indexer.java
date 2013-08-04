package com.alltheamiga.indexer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.dmpp.adf.app.Directory;
import org.dmpp.adf.app.DosFile;
import org.dmpp.adf.app.UserFile;
import org.dmpp.adf.app.UserVolume;
import org.dmpp.adf.logical.LogicalVolume;
import org.dmpp.adf.physical.PhysicalVolume;
import org.dmpp.adf.physical.PhysicalVolumeFactory;
import org.dmpp.infolib.AmigaIcon;
import org.dmpp.infolib.AmigaInfoReader;

import scala.collection.Iterator;
import scala.collection.immutable.List;

import com.alltheamiga.database.Storage;
import com.alltheamiga.database.model.AmigaDirectory;
import com.alltheamiga.database.model.AmigaDisk;
import com.alltheamiga.database.model.AmigaFile;
import com.alltheamiga.database.model.Bitmap;
import com.alltheamiga.database.model.DiskRegistration;
import com.alltheamiga.database.model.DiskRegistrationFlag;
import com.alltheamiga.database.model.FileData;
import com.alltheamiga.database.model.types.Platform;
import com.alltheamiga.database.model.types.VideoMode;
import com.alltheamiga.indexer.amitools.XdfTool;
import com.alltheamiga.indexer.tosec.DumpInformation;
import com.alltheamiga.indexer.tosec.MediaInformation;
import com.alltheamiga.indexer.tosec.TosecException;
import com.alltheamiga.indexer.tosec.TosecFile;
import com.alltheamiga.indexer.tosec.TosecFileParser;
import com.alltheamiga.utils.GZip;
import com.alltheamiga.utils.KMPMatch;

public class Indexer {
    private static Pattern diskPattern = Pattern.compile("Disk ([0-9]?[0-9]) of ([0-9]?[0-9])");

    private File workDir;
    private Storage storage;
    private TosecFileParser tosecFileParser;
    private XdfTool xdfTool;
    private Map<String, Bitmap> bitmapCache = new HashMap<>();
    private Map<String, FileData> fileDataCache = new HashMap<>();

    public Indexer(Storage storage, File workDir) {
        this.workDir = workDir;
        this.storage = storage;
        tosecFileParser = new TosecFileParser();
        xdfTool = new XdfTool();
    }

    public void process(File originalFile) throws IOException, ZipException {
        File tempDir = new File(workDir, UUID.randomUUID().toString());
        FileUtils.forceMkdir(tempDir);
        copyAndUnpackFileToTempDir(originalFile, tempDir);
        processDirectory(tempDir, tempDir);
        try {
            FileUtils.forceDelete(tempDir);
        } catch (FileNotFoundException e) {
            log(2, "Could not delete temp directory. You need to manually clean it up!");
        }
    }

    private void processDirectory(File directory, File tempDir) throws IOException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(file, tempDir);
            } else {
                processFile(file, tempDir);
            }
        }
    }

    private synchronized void processFile(File fileToProcess, File tempDir) throws IOException {
        bitmapCache.clear();
        fileDataCache.clear();
        storage.begin();

        log(1, "Processing file \"" + fileToProcess.getName() + "\"");
        String filename = fileToProcess.getName();
        DiskRegistration diskReg = new DiskRegistration();

        populateGenericDiskRegFields(filename, diskReg);
        checkAndPopulateTosecFilename(filename, diskReg);

        AmigaDisk disk = createAmigaDisk(fileToProcess, tempDir);
        disk.setDiskRegistration(diskReg);
        diskReg.setAmigaDisk(disk);

        storage.persist(diskReg);
        storage.commit();

        bitmapCache.clear();
        fileDataCache.clear();
    }

    private AmigaDisk createAmigaDisk(File adfFile, File tempDir) throws IOException {
        String hashCode = sha1OfFile(adfFile);
        AmigaDisk disk = storage.findAmigaDiskByHashCode(hashCode);
        if (disk == null) {
            disk = new AmigaDisk();
            File unpackedRoot = null;
            boolean unpacked = xdfTool.unpack(adfFile, tempDir);
            if (unpacked) {
                String volumeName = xdfTool.findVolumeName(tempDir);
                unpackedRoot = new File(tempDir, volumeName);
                File bootcodeFile = new File(tempDir, volumeName + ".bootcode");
                if (bootcodeFile.exists()) {
                    disk.setBootBlockData(FileUtils.readFileToByteArray(bootcodeFile));
                }
                log(2, "Volume " + volumeName + " unpacked");
            }
            disk.setHashCode(sha1OfFile(adfFile));
            populateDiskWithADF(adfFile, unpackedRoot, disk);
        }
        return disk;
    }

    private AmigaDisk populateDiskWithADF(File adfFile, File unpackedRoot, AmigaDisk disk) throws IOException {
        byte[] diskData = FileUtils.readFileToByteArray(adfFile);
        disk.setData(GZip.compress(diskData));
        try {
            // Read ADF image and create UserVolume
            UserVolume userVolume = loadUserVolume(adfFile);
            if (disk.getBootBlockData() == null) {
                // If we could not extract the bootblock using the
                // .bootcode file we try using this method instead.
                disk.setBootBlockData(extractBootBlock(userVolume));
            }
            // Populate disk with data from user volume
            disk.setFileSystemType(userVolume.filesystemType());
            disk.setIsFloppy(true);
            disk.setValid(userVolume.isValid());
            disk.setVolumeName(userVolume.name());
            disk.setAvailableBytes(userVolume.numBytesAvailable());
            disk.setUsedBytes(userVolume.numBytesUsed());
            disk.setCreated(userVolume.creationTime());
            disk.setModified(userVolume.lastModificationTime());
            disk.setRootDirectory(createAmigaDirectoryFromDirectory(userVolume.rootDirectory(), unpackedRoot));
        } catch (RuntimeException e) {
            log(2, "Could not read meta data from disk.");
        }
        return disk;
    }

    private byte[] extractBootBlock(UserVolume userVolume) {
        byte[] one = userVolume.logicalVolume().dataBlock(0).dataBytes();
        byte[] two = userVolume.logicalVolume().dataBlock(1).dataBytes();
        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }

    private AmigaDirectory createAmigaDirectoryFromDirectory(Directory directory, File unpackedRoot) {
        AmigaDirectory amigaDirectory = new AmigaDirectory();
        amigaDirectory.setComment(directory.comment());
        amigaDirectory.setModified(directory.lastModificationTime());
        amigaDirectory.setName(directory.name());

        List<DosFile> directoryList = directory.listDirectories();
        for (Iterator<DosFile> i = directoryList.iterator(); i.hasNext();) {
            AmigaDirectory childDirectory = createAmigaDirectoryFromDirectory((Directory) i.next(), unpackedRoot);
            amigaDirectory.getSubDirectories().add(childDirectory);
            childDirectory.setParentDirectory(amigaDirectory);
        }

        List<DosFile> fileList = (List<DosFile>) directory.list();
        for (Iterator<DosFile> i = fileList.iterator(); i.hasNext();) {
            DosFile file = i.next();
            if (!file.isFile()) {
                continue;
            }
            AmigaFile amigaFile = createAmigaFileFromFile((UserFile) file, unpackedRoot);
            amigaDirectory.getFiles().add(amigaFile);
            amigaFile.setDirectory(amigaDirectory);
        }
        return amigaDirectory;
    }

    private AmigaFile createAmigaFileFromFile(UserFile file, File unpackedRoot) {
        AmigaFile amigaFile = new AmigaFile();
        amigaFile.setComment(file.comment());
        amigaFile.setModified(file.lastModificationTime());
        amigaFile.setName(file.name());
        byte[] data = null;
        try {
            data = file.dataBytes();
        } catch (RuntimeException e) {
            StringBuilder filePath = new StringBuilder(file.name());
            Directory dir = file.parentDirectory();
            while (!dir.isRoot()) {
                filePath.insert(0, dir.name() + "/");
                dir = dir.parentDirectory();
            }
            try {
                data = FileUtils.readFileToByteArray(new File(unpackedRoot, filePath.toString()));
            } catch (IOException e2) {
                log(2, "Can't read data for file: " + filePath);
            }
        }
        if (data != null) {
            String hashCode = sha1OfByteArray(data);
            FileData fileData = storage.findFileDataByHashCode(hashCode);
            if (fileData == null) {
                fileData = fileDataCache.get(hashCode);
                if (fileData == null) {
                    fileData = new FileData();
                    fileData.setData(data);
                    fileData.setHashCode(hashCode);
                    fileData.setSize(file.size());
                    fileData.setVersion(findVersionInBytes(data));
                    if (amigaFile.getName().endsWith(".info")) {
                        processInfoFile(fileData);
                    }
                    fileDataCache.put(hashCode, fileData);
                }
            }
            fileData.getAmigaFiles().add(amigaFile);
            amigaFile.setFileData(fileData);
        }
        return amigaFile;
    }

    private void processInfoFile(FileData fileData) {
        try {
            AmigaInfoReader info = new AmigaInfoReader(AmigaInfoReader.Palette_2_x());
            AmigaIcon icon = info.createIcon(fileData.getData());
            createAddBitmapToAmigaFile(imageToPngByteArray(icon.normalImage()), fileData, false);
            if (!icon.highlightImage().isEmpty()) {
                createAddBitmapToAmigaFile(imageToPngByteArray(icon.highlightImage().get()), fileData, true);
            }
        } catch (Throwable e) {
            // Ignore
        }
    }

    private void createAddBitmapToAmigaFile(byte[] data, FileData fileData, boolean highlighted) {
        String hashCode = sha1OfByteArray(data);
        Bitmap bitmap = storage.findBitmapByHashCode(hashCode);
        if (bitmap == null) {
            bitmap = bitmapCache.get(hashCode);
            if (bitmap == null) {
                bitmap = new Bitmap();
                bitmap.setHashCode(hashCode);
                bitmap.setData(data);
                bitmap.setHighlighted(highlighted);
                bitmapCache.put(hashCode, bitmap);
            }
        }
        bitmap.getFileDatas().add(fileData);
        fileData.getBitmaps().add(bitmap);
    }

    private byte[] imageToPngByteArray(Image icon) throws IOException {
        BufferedImage image = new BufferedImage(icon.getWidth(null), icon.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(icon, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    // "\0$VER: programname version.revision (dd.mm.yyyy) comment\0"
    private String findVersionInBytes(byte[] data) {
        byte[] pattern = new byte[] { 0x00, 0x24, 0x56, 0x45, 0x52, 0x3A }; // \0$VER:
        int index = KMPMatch.indexOf(data, pattern);
        if (index != -1) {
            int endIndex = KMPMatch.indexOf(data, new byte[] { 0x00 }, index + 1);
            if (endIndex != -1) {
                index += 6; // Skip the "\0$VER:" part
                int size = endIndex - index;
                byte[] buf = new byte[size];
                System.arraycopy(data, index, buf, 0, size);
                try {
                    return new String(buf, "latin1").trim();
                } catch (UnsupportedEncodingException e) {
                    // Ignore
                }
            }
        }
        return null;
    }

    private UserVolume loadUserVolume(File diskImageFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(diskImageFile);
        PhysicalVolume physicalVolume = PhysicalVolumeFactory.readDoubleDensityDisk(fis);
        return new UserVolume(new LogicalVolume(physicalVolume));
    }

    private static String sha1OfByteArray(byte[] dataBytes) {
        if (dataBytes == null)
            return null;
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

    private void populateGenericDiskRegFields(String filename, DiskRegistration diskReg) {
        diskReg.setFilename(filename);
        diskReg.setCreated(new Date());
        diskReg.setUpdated(new Date());
        diskReg.setPlatform(Platform.AMIGA);
        diskReg.setCompany(null);
        diskReg.setChipset(null);
        diskReg.setCategory(null);
    }

    private void checkAndPopulateTosecFilename(String filename, DiskRegistration diskReg) {
        try {
            TosecFile tosec = tosecFileParser.parse(filename);
            diskReg.setTitle(tosec.getTitle());
            diskReg.setYear(tosec.getYear());
            diskReg.setPublisher(tosec.getPublisher());
            diskReg.setLanguage(tosec.getLanguage());
            diskReg.setOriginUrl("http://www.tosec.org");
            diskReg.setVideoMode(VideoMode.fromString(tosec.getVideo()));
            diskReg.setCopyrightStatus(tosec.getCopyrightStatus());
            diskReg.setDemo(tosec.getDemo());
            diskReg.setDevelopmentStatus(tosec.getDevelopementStatus());
            MediaInformation mediaInfo = tosec.getMediaInformation();
            if (mediaInfo != null) {
                diskReg.setMediaIndex(mediaInfo.getMediaIndex());
                diskReg.setDiskLabel(mediaInfo.getMediaLabel());
                Matcher matcher = diskPattern.matcher(mediaInfo.getMediaIndex());
                if (matcher.matches()) {
                    diskReg.setDiskIndex(parseInt(matcher.group(1)));
                    diskReg.setDiskCount(parseInt(matcher.group(2)));
                }
            }
            addDumpInformationToDiskRegistration(diskReg, tosec.getDumpInformationList());
            addDumpInformationToDiskRegistration(diskReg, tosec.getMoreDumpInformationList());
        } catch (TosecException e) {
            // Ignore
        }
    }

    private void addDumpInformationToDiskRegistration(DiskRegistration diskReg, ArrayList<DumpInformation> dumpInfoList) {
        for (DumpInformation di : dumpInfoList) {
            DiskRegistrationFlag flag = new DiskRegistrationFlag();
            flag.setInformation(di.getInformation());
            flag.setNumber(di.getNumber());
            flag.setType(di.getType().name());
            flag.setDiskRegistration(diskReg);
            diskReg.getFlags().add(flag);
        }
    }

    private void copyAndUnpackFileToTempDir(File file, File tempDir) throws IOException, ZipException {
        log(0, "Copy and unpack \"" + file.getName() + "\"");
        ZipFile zipFile = new ZipFile(file);
        if (zipFile.isValidZipFile()) {
            zipFile.extractAll(tempDir.getAbsolutePath());
        } else {
            FileUtils.copyFileToDirectory(file, tempDir);
        }
    }

    public static void log(int ident, String message) {
        StringBuilder out = new StringBuilder();
        out.append(">>");
        for (int n = 0; n < ident; n++)
            out.append(">>");
        out.append(" ").append(message);
        System.out.println(out.toString());
    }

    private Integer parseInt(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}