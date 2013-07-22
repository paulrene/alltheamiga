package com.alltheamiga.indexer.tosec;

import java.util.ArrayList;

/**
 * Title: TosecTool Description: Part of "Weltanschauung" an open source atari database project Copyright: GPL Group:
 * Foundation Two/Germany Created on: 29.05.2004
 * 
 * @author twh
 */

public class TosecFile {

    public static final String UNKOWN_TITLE = "ZZZ-UNK-TOSEC";
    public static final String UNKOWN_COMPANY = "-";
    public static final String UNKOWN_YEAR = "19xx";
    public static final String UNKOWN_LANGUAGE = "UNK-LANG";
    public static final String UNKOWN_EXTENSION = "bin";

    public static final String DEMO_DEMO = "demo";
    public static final String DEMO_PREVIEW = "preview";
    public static final String DEMO_PLAYABLE_PREVIEW = "playable preview";

    public static final String VIDEO_PAL = "PAL";
    public static final String VIDEO_NTSC = "NTSC";

    public static final String LANGUAGE_UNKOWN = "UNK-LANG";

    // The Format: Overview
    // "Title version (demo) (year)(publisher)(system)(video)
    // (Country/Language)[dump info flags](Media x of y Side z)(Media label)[more info].extender"

    private String filename = "";

    private String title = UNKOWN_TITLE;
    private String demo = "";
    private String year = UNKOWN_YEAR;
    private String publisher = UNKOWN_COMPANY;
    private String video = "";
    private String language = "";
    private String copyrightStatus = "";
    private String developementStatus = "";

    private MediaInformation mediaInformation;

    private ArrayList<DumpInformation> dumpInformationList = new ArrayList<DumpInformation>();

    private ArrayList<DumpInformation> moreDumpInformationList = new ArrayList<DumpInformation>();

    private String extender = UNKOWN_EXTENSION;

    /**
     * Creates a empty representation of a TOSEC standard file descriptor.
     */
    public TosecFile() {

    }

    /**
     * @return Returns the company.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param company
     *            The company to set.
     */
    public void setPublisher(String company) {
        this.publisher = company;
    }

    /**
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     *            The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return Returns the name.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setTitle(String name) {
        this.title = name;
    }

    /**
     * @return Returns the year.
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            The year to set.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return Returns the demo.
     */
    public String getDemo() {
        return demo;
    }

    /**
     * @param demo
     *            The demo to set.
     */
    public void setDemo(String demo) {
        this.demo = demo;
    }

    /**
     * @return Returns the extender.
     */
    public String getExtender() {
        return extender;
    }

    /**
     * @param extender
     *            The extender to set.
     */
    public void setExtender(String extender) {
        this.extender = extender;
    }

    /**
     * @return Returns the copyrightStatus.
     */
    public String getCopyrightStatus() {
        return copyrightStatus;
    }

    /**
     * @param copyrightStatus
     *            The copyrightStatus to set.
     */
    public void setCopyrightStatus(String copyrightStatus) {
        this.copyrightStatus = copyrightStatus;
    }

    /**
     * @return Returns the developementStatus.
     */
    public String getDevelopementStatus() {
        return developementStatus;
    }

    /**
     * @param developementStatus
     *            The developementStatus to set.
     */
    public void setDevelopementStatus(String developementStatus) {
        this.developementStatus = developementStatus;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return Returns the video.
     */
    public String getVideo() {
        return video;
    }

    /**
     * @param video
     *            The video to set.
     */
    public void setVideo(String video) {
        this.video = video;
    }

    public DumpInformation getDumpInformation(DumpInformationType type) {

        for (DumpInformation info : dumpInformationList) {
            if (info.getType() == type) {
                return info;
            }
        }
        return null;
    }

    public String toString() {
        String lString;
        lString = getFilename() + " Name:'" + getTitle() + "'" + " demo:'" + getDemo() + "'" + " Year:'" + getYear()
                + "'" + " publisher:'" + getPublisher() + "'" + " Language:'" + getLanguage() + "'"
                + " Copyright Status:'" + getCopyrightStatus() + "'" + " Video:'" + getVideo() + "'"
                + " Developement Status:'" + getDevelopementStatus() + "'";

        if (mediaInformation != null) {
            lString = lString + " Media:" + mediaInformation;
        }

        lString = lString + " DumpInfo:";
        for (DumpInformation info : dumpInformationList)
            lString = lString + info.toString();

        lString = lString + " MoreDumpInfo:";
        for (DumpInformation info : moreDumpInformationList)
            lString = lString + info.toString();

        lString = lString + " Extender:'" + getExtender() + "'";

        return lString;
    }

    // The Format: Overview
    // Title version (demo) (year) (publisher) (system) (video) (Country/Language)
    // (copyright status) (development status) (Media x of y Side z) (Media label)
    // [dump info flags] [more info]
    public String generateTosecFilename() {

        // Title
        String tosecFileName = getTitle();

        // (demo)
        if (!getDemo().equals(""))
            tosecFileName = tosecFileName + " (" + getDemo() + ")";

        // (Year)(Publisher)
        tosecFileName = tosecFileName + " (" + getYear() + ")" + "(" + getPublisher() + ")";
        // (video)
        if (!getVideo().equals(""))
            tosecFileName = tosecFileName + "(" + getVideo() + ")";

        // (Country/Language)
        if (!getLanguage().equals(""))
            tosecFileName = tosecFileName + "(" + getLanguage() + ")";

        // (copyright status)
        if (!getCopyrightStatus().equals(""))
            tosecFileName = tosecFileName + "(" + getCopyrightStatus() + ")";

        // (development status)
        if (!getDevelopementStatus().equals(""))
            tosecFileName = tosecFileName + "(" + getDevelopementStatus() + ")";

        // [dump info flags]
        for (DumpInformation info : dumpInformationList) {
            tosecFileName = tosecFileName + info.toString();
        }

        // (Media x of y Side z)
        if (mediaInformation != null) {
            tosecFileName = tosecFileName + mediaInformation;
        }

        // [more info]
        for (DumpInformation info : moreDumpInformationList) {
            tosecFileName = tosecFileName + info.toString();
        }

        return tosecFileName + "." + getExtender();
    }

    public boolean isGeneratedFileEqual() {
        int sum1 = 0;
        int sum2 = 0;
        String name1 = filename;
        String name2 = generateTosecFilename();

        if (name1.length() == name2.length()) {
            byte[] bytes1 = filename.getBytes();
            byte[] bytes2 = generateTosecFilename().getBytes();

            for (int i = 0; i < bytes1.length; i++) {
                sum1 = sum1 + bytes1[i];
                sum2 = sum2 + bytes2[i];
            }
            if (sum1 == sum2)
                return true;
        }

        return false;
    }

    public void setDumpInformationList(ArrayList<DumpInformation> dumpInformationList) {
        this.dumpInformationList = dumpInformationList;
    }

    public ArrayList<DumpInformation> getDumpInformationList() {
        return dumpInformationList;
    }

    public void setMoreDumpInformationList(ArrayList<DumpInformation> moreDumpInformationList) {
        this.moreDumpInformationList = moreDumpInformationList;
    }

    public ArrayList<DumpInformation> getMoreDumpInformationList() {
        return moreDumpInformationList;
    }

    public void setMediaInformation(MediaInformation mediaInformation) {
        this.mediaInformation = mediaInformation;
    }

    public MediaInformation getMediaInformation() {
        return mediaInformation;
    }

}