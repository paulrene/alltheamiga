package com.alltheamiga.indexer.tosec;

import java.util.HashMap;
import java.util.Map;

public enum CopyrightType {
    FREEWARE("FW"), PUBLICDOMAIN("PD"), SHAREWARE("SW"), SHAREWARE_REGISTERED("SW-R");

    private final String abbreviation;

    private CopyrightType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    private static final Map<String, CopyrightType> lookup = new HashMap<String, CopyrightType>();
    static {
        for (CopyrightType m : CopyrightType.values()) {
            lookup.put(m.getAbbreviation(), m);
        }
    }

    public static CopyrightType get(String abbreviation) {
        return lookup.get(abbreviation);
    }

    public static boolean contains(String abbreviation) {
        return lookup.containsKey(abbreviation);
    }
}
