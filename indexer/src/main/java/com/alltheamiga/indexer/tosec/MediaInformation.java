package com.alltheamiga.indexer.tosec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaInformation {

    // (Media x of y Side z)(Media label)
    // (Disk 1 of 2 Sida a)(Character Disc)

    private String mediaIndex;
    private String mediaLabel;

    public MediaInformation(String mediaIndex) {
        this.mediaIndex = mediaIndex;
    }

    public static boolean isMediaInformation(String part) {
        String patternStr = "\\b(\\w*)\\b";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(part);
        while (matcher.find()) {
            if (MediaType.contains(matcher.group(1)))
                return true;
        }
        return false;
    }

    public String toString() {
        String retVal;
        retVal = "(" + mediaIndex + ")" + (mediaLabel == null ? "" : "(" + mediaLabel + ")");
        return retVal;
    }

    public void setMediaLabel(String mediaLabel) {
        this.mediaLabel = mediaLabel;
    }

    public String getMediaLabel() {
        return mediaLabel;
    }

    public void setMediaIndex(String mediaIndex) {
        this.mediaIndex = mediaIndex;
    }

    public String getMediaIndex() {
        return mediaIndex;
    }
}
