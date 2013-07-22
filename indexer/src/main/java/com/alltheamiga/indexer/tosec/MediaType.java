package com.alltheamiga.indexer.tosec;

import java.util.HashMap;
import java.util.Map;

public enum MediaType {
    MEDIA("Media"), SIDE("Side"), DISK("Disk"), DISC("Disc"), FILE("File"), TAPE("Tape");

    private final String abbreviation;

    private MediaType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    private static final Map<String, MediaType> lookup = new HashMap<String, MediaType>();
    static {
        for (MediaType m : MediaType.values()) {
            lookup.put(m.getAbbreviation(), m);
        }
    }

    public static MediaType get(String abbreviation) {
        return lookup.get(abbreviation);
    }

    public static boolean contains(String abbreviation) {
        return lookup.containsKey(abbreviation);
    }
}
