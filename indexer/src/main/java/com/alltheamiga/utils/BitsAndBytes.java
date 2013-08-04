package com.alltheamiga.utils;

public class BitsAndBytes {

    public static final int readUInt8(byte[] data, int offset) {
        return data[offset] & 0xff;
    }

    public static final int readUInt16(byte[] data, int offset) {
        return (data[offset + 0] << 8) & 0xff00 |
                data[offset + 1]       & 0x00ff;
    }

    public static final int readUInt32(byte[] data, int offset) {
        return (data[offset + 0] << 24) & 0xff000000 |
               (data[offset + 1] << 16) & 0x00ff0000 |
               (data[offset + 2] <<  8) & 0x0000ff00 |
                data[offset + 3]        & 0x000000ff;
    }
    
    public static final int readInt16(byte[] data, int offset) {
        return readUInt16(data, offset);
    }

    public static final int readInt32(byte[] data, int offset) {
        return readUInt32(data, offset);
    }

}