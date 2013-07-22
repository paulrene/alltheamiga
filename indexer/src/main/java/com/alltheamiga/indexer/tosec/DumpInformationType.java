package com.alltheamiga.indexer.tosec;

import java.util.HashMap;

public enum DumpInformationType {
    ALTERNATE_VERSION("a"), CRACKED("cr"), FIXED("f"), HACKED("h"), MODIFIED("m"), PIRATED("p"), TRAINED("t"), OTHER(
            "o");

    private final String flag;

    private DumpInformationType(String flag) {
        this.flag = flag;
    }

    private final static HashMap<DumpInformationType, String> lookupString = new HashMap<DumpInformationType, String>();
    static {
        for (DumpInformationType aType : DumpInformationType.values()) {
            lookupString.put(aType, aType.flag);
        }
    }

    private final static HashMap<String, DumpInformationType> lookupEnum = new HashMap<String, DumpInformationType>();
    static {
        for (DumpInformationType aType : DumpInformationType.values()) {
            lookupEnum.put(aType.flag, aType);
        }
    }

    public static String getFlag(DumpInformationType dumpInfoType) {
        return lookupString.get(dumpInfoType);
    }

    public static boolean contains(String flag) {
        return lookupString.containsKey(flag);
    }

    public static DumpInformationType getDumpInformationType(String flag) {
        return lookupEnum.get(flag);
    }

}
