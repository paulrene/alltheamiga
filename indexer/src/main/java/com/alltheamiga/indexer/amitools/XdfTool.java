package com.alltheamiga.indexer.amitools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;

public class XdfTool {

    public static void main(String[] args) throws ExecuteException, IOException {
        File adfFile = new File("/Users/paulrene/alltheamiga/dropbox/Deluxe Paint V v5.2 (1995)(Electronic Arts)(AGA)(Disk 1 of 4)(Program)[WB].adf");
        File tempDir = new File("/Users/paulrene/alltheamiga/temp");

        FileUtils.cleanDirectory(tempDir);

        XdfTool tool = new XdfTool();
        if (tool.unpack(adfFile, tempDir)) {
            String volumeName = tool.findVolumeName(tempDir);
            if (volumeName != null) {
                XdfMeta disk = tool.getXdfMeta(volumeName, tempDir);
                System.out.println(disk);
            } else {
                System.out.println("Could not find volume name!");
            }
        } else {
            System.out.println("Unpack failed!");
        }

    }

    private File amitoolsHome;

    public XdfTool() {
        this.amitoolsHome = new File(System.getProperty("amitoolsHome", "/Users/paulrene/alltheamiga/amitools"));
    }

    public String findVolumeName(File tempDir) {
        for (File file : tempDir.listFiles()) {
            if (file.isDirectory()) {
                return file.getName();
            }
        }
        return null;
    }

    public boolean unpack(File fileToUnpack, File unpackToDirectory) {
        Map<String, File> params = new HashMap<>();
        params.put("file", fileToUnpack);
        params.put("directory", unpackToDirectory);
        CommandLine cmd = new CommandLine(new File(amitoolsHome, "xdftool"));
        cmd.addArgument("${file}", false).addArgument("unpack").addArgument("${directory}", false);
        cmd.setSubstitutionMap(params);
        System.out.println(cmd);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        try {
            return executor.execute(cmd) == 0;
        } catch (ExecuteException e) {
            e.printStackTrace();
            // Ignore and return false
        } catch (IOException e) {
            e.printStackTrace();
            // Ignore and return false
        }
        return false;
    }

    public XdfMeta getXdfMeta(String volumeName, File unpackToDirectory) {
        return XdfMeta.fromFile(new File(unpackToDirectory, volumeName + ".xdfmeta"));
    }

}
