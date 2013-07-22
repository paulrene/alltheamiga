package com.alltheamiga.indexer.tosec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DumpInformation {

    private DumpInformationType type;
    private String information = "";
    private int number;

    public DumpInformation() {

    }

    public DumpInformation(DumpInformationType type) {
        this.type = type;
    }

    public DumpInformationType getType() {
        return type;
    }

    public void setType(DumpInformationType type) {
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static DumpInformation getDumpInformation(String dumpInformationText) {
        String patternStr = "^([A-Za-z]+)(\\d)*\\s?(.*)$";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(dumpInformationText);

        DumpInformation dumpInfo = new DumpInformation();
        while (matcher.find()) {
            String flag = matcher.group(1);
            String number = matcher.group(2) == null ? "0" : matcher.group(2);
            String information = matcher.group(3);
            DumpInformationType dumpType = DumpInformationType.getDumpInformationType(flag);
            if (dumpType == null) {
                dumpType = DumpInformationType.OTHER;
                information = dumpInformationText;
            }
            dumpInfo.setType(dumpType);
            dumpInfo.setNumber(Integer.parseInt(number));
            dumpInfo.setInformation(information);
        }

        return dumpInfo;
    }

    public String toString() {
        String retVal = "";
        if (type != null) {
            if (type != DumpInformationType.OTHER)
                retVal = "[" + DumpInformationType.getFlag(type) + (number > 1 ? String.valueOf(number) : "")
                        + (information.equals("") ? "" : " " + information) + "]";
            else
                retVal = "[" + information + "]";
        }

        return retVal;
    }
}
