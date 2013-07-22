package com.alltheamiga.indexer.tosec;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TosecFileParser {

    private TosecFile tFile;
    List<String> isoLanguageCodeList = Arrays.asList(java.util.Locale.getISOLanguages());

    public TosecFileParser() {

    }

    public TosecFile parse(String pFilename) {
        tFile = new TosecFile();
        String part;
        int helpIndex = 0;

        // read out the extender
        helpIndex = pFilename.lastIndexOf(".");
        if (helpIndex == (-1))
            throw new TosecException("Could not handle file extension:" + this.toString());
        try {
            tFile.setExtender(pFilename.substring(helpIndex + 1, pFilename.length()));
            pFilename = pFilename.substring(0, helpIndex);
        } catch (StringIndexOutOfBoundsException e) {
            throw new TosecException("Could not handle file extension:" + this.toString());
        }

        StringTokenizer tokenizer = new StringTokenizer(pFilename, "(");

        // mandatory fields
        // Title
        try {
            part = (String) tokenizer.nextElement();
            tFile.setTitle(part.trim());
            if (pFilename.indexOf("(") < 1)
                throw new NoSuchElementException();

            // version not supported

        } catch (NoSuchElementException e) {
            throw new TosecException("Could not handle program name for:" + this.toString());
        }

        if (pFilename.toLowerCase().indexOf("(demo)") > 0)
            tFile.setTitle(tFile.getTitle() + " (Demo)");

        String patternStr = "\\((.||[^\\)]*(?<!\\bDemo))\\)"; // look for: "name (demo) (year)(pub)(lang)"

        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pFilename);

        // (year)
        if (matcher.find()) {
            tFile.setYear(matcher.group(1));
        } else
            throw new TosecException("Could not handle year for:" + pFilename);

        if (tFile.getYear().startsWith("19") || tFile.getYear().startsWith("20")) {
            // very well
        } else {
            throw new TosecException(TosecParseException.TOSEC_EXCEPTION_YEAR);
        }

        // (Publisher)
        if (matcher.find()) {
            tFile.setPublisher(matcher.group(1));
        } else
            throw new TosecException("Could not handle publisher for:" + pFilename);

        // (video) || ((Country/Language)
        // (Media x of y Side z) || (Media label)
        while (matcher.find()) {
            part = matcher.group(1);

            if (TosecFile.VIDEO_PAL.equalsIgnoreCase(part) || TosecFile.VIDEO_NTSC.equalsIgnoreCase(part)) {
                tFile.setVideo(part);
            } else if (isoLanguageCodeList.contains(part.toLowerCase())) {
                tFile.setLanguage(part);
            } else if (MediaInformation.isMediaInformation(part)) {
                MediaInformation mediaInfo = new MediaInformation(part);
                if (matcher.find()) {
                    mediaInfo.setMediaLabel(matcher.group(1));
                }
                tFile.setMediaInformation(mediaInfo);
            }
        }

        // process Dump Information and More (untyped) Dump Information
        pattern = Pattern.compile("\\[(.||[^\\]]*)\\]"); // everything in brackets [..]
        matcher = pattern.matcher(pFilename);

        while (matcher.find()) {
            part = matcher.group(1);
            DumpInformation dumpInfo = DumpInformation.getDumpInformation(part);
            if (dumpInfo.getType() != DumpInformationType.OTHER)
                tFile.getDumpInformationList().add(dumpInfo);
            else
                tFile.getMoreDumpInformationList().add(dumpInfo);
        }

        return tFile;
    }

}
