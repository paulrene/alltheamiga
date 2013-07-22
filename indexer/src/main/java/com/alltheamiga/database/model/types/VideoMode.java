package com.alltheamiga.database.model.types;

public enum VideoMode {
    
    PAL, NTSC;

    public static VideoMode fromString(String video) {
        if(video==null) {
            return null;
        }
        if("PAL".equalsIgnoreCase(video)) {
            return PAL;
        } else if("NTSC".equalsIgnoreCase(video)) {
            return NTSC;
        } else {
            return null;
        }
    }

}
