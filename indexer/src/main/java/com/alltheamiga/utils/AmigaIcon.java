package com.alltheamiga.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class AmigaIcon {

    public static void main(String[] args) throws IOException {

        File appIcon = new File("/Users/paulrene/git/alltheamiga/indexer/src/main/resources/DPaint.info");
        File diskIcon = new File("/Users/paulrene/git/alltheamiga/indexer/src/main/resources/disk.info");
        File dirIcon = new File("/Users/paulrene/git/alltheamiga/indexer/src/main/resources/Catalogs.info");

        
        
        //AmigaIcon icon = AmigaIcon.fromBytes(iconData);
    }

    private static final int DISKOBJECT_STRUCTURE_SIZE = 78;
    private static final int DRAWERDATA_STRUCTURE_SIZE = 56;
    private static final int IMAGE_STRUCTURE_SIZE = 20;

    private static final int[] ICON_PALETTE_AMIGAOS_V1 = new int[] { 0x55AAFF, 0xFFFFFF, 0x000000, 0xFF8800 };
    private static final int[] ICON_PALETTE_AMIGAOS_V2 = new int[] { 0x959595, 0x000000, 0xFFFFFF, 0x3B67A2 };
    private static final int[] ICON_PALETTE_MAGICWB = new int[] { 0x959595, 0x000000, 0xFFFFFF, 0x3B67A2, 0x7B7B7B,
            0xAFAFAF, 0xAA907C, 0xFFA997 };

    private int iconVersion;
    private int gadgetWidth;
    private int gadgetHeight;
    private int gadgetFlags;
    private boolean hasSecondImage;
    private IconType iconType;
    private int iconRevision;
    private int hasDefaultTool;
    private int hasToolTypesTable;
    private int currentX;
    private int currentY;
    private boolean hasDrawerData;
    private int stackSize;
    private List<ImageStructure> imageList;

    private AmigaIcon(byte[] data) throws FileNotFoundException, IOException {
        imageList = new ArrayList<>();

        int magic = BitsAndBytes.readUInt16(data, 0);
        if (magic!= 0xE310) {
            throw new IllegalArgumentException("Data does not contain magic header.");
        }

        this.iconVersion = BitsAndBytes.readUInt16(data, 2);
        this.gadgetWidth = BitsAndBytes.readUInt16(data, 12);
        this.gadgetHeight = BitsAndBytes.readUInt16(data, 14);
        this.gadgetFlags = BitsAndBytes.readUInt16(data, 16);
        if (BitsAndBytes.readUInt32(data, 22) == 0) {
            throw new IllegalArgumentException("Zero value found where positive was expected.");
        }
        this.hasSecondImage = BitsAndBytes.readUInt32(data, 26) != 0 && (gadgetFlags & 0x0003) == 0x0002; // Highlight bits set to 2?
        
        this.iconRevision = BitsAndBytes.readUInt32(data, 44);
        this.iconType = IconType.resolve(BitsAndBytes.readUInt8(data, 48));
        this.hasDefaultTool = BitsAndBytes.readUInt32(data, 50);
        this.hasToolTypesTable = BitsAndBytes.readUInt32(data, 54);
        this.currentX = BitsAndBytes.readInt32(data, 58);
        this.currentY = BitsAndBytes.readInt32(data, 62);
        this.hasDrawerData = BitsAndBytes.readUInt32(data, 66) != 0 && iconType.isDrawer();
        this.stackSize = BitsAndBytes.readUInt32(data, 74);

        int imageOffset = DISKOBJECT_STRUCTURE_SIZE + (hasDrawerData ? DRAWERDATA_STRUCTURE_SIZE : 0);

        ImageStructure imageStruct = new ImageStructure();
        imageStruct.leftEdge = BitsAndBytes.readInt16(data, imageOffset + 0);
        imageStruct.topEdge = BitsAndBytes.readInt16(data, imageOffset + 2);
        imageStruct.width = BitsAndBytes.readUInt16(data, imageOffset + 4);
        imageStruct.height = BitsAndBytes.readUInt16(data, imageOffset + 6);
        imageStruct.depth = BitsAndBytes.readUInt16(data, imageOffset + 8);
        
        System.out.println(imageStruct.width+", "+imageStruct.height);
        
        imageStruct.hasImageData = BitsAndBytes.readUInt32(data, imageOffset + 10) != 0;
        if (imageStruct.hasImageData) {
            // Select palette
            int[] palette = ICON_PALETTE_MAGICWB;
            switch (iconRevision) {
            case 0:
                palette = ICON_PALETTE_AMIGAOS_V1;
            case 1:
                palette = ICON_PALETTE_AMIGAOS_V2;
            }

            
            
            int bitplaneWidth = ((imageStruct.width + 15) >> 4) << 1;
            int imageDataOffset = imageOffset + IMAGE_STRUCTURE_SIZE;

            BufferedImage image = new BufferedImage(gadgetWidth, gadgetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setBackground(new Color(palette[0]));

            byte[] pixelBytes = new byte[imageStruct.width * imageStruct.height];
            for(int plane = 0; plane<imageStruct.depth; plane++) {
                for(int row = 0; row<imageStruct.height; row++) {
                    for(int column = 0;column<bitplaneWidth; column++) {
                        BitsAndBytes.readUInt8(data, imageDataOffset + (bitplaneWidth*imageStruct.height*plane) + (bitplaneWidth*row) + column);
                    }
                }
            }
            
            
            
            ImageIO.write(image, "png", new FileOutputStream("/Users/paulrene/Desktop/test.png"));



            
/*            
            BufferedImage image = new BufferedImage(gadgetWidth, gadgetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setBackground(new Color(palette[0]));

            
            for (int y = 0; y < imageStruct.height; y++) {
                for (int column = 0; column < (bitplaneWidth - 1); column++) {
                    int[] values = new int[imageStruct.depth];
                    for (int depth = 0; depth < imageStruct.depth; depth++) {
                        values[depth] = BitsAndBytes.readUInt8(data, imageDataOffset + (bitplaneSize * depth) + (bitplaneWidth * y) + column);
                    }
                    for(int x=0;x<8;x++) {
                        int index = 0;
                        for (int depth = 0; depth < imageStruct.depth; depth++) {
                            index += (isBitSet(values[depth], x) ? 2^x : 0);
                        }
                        image.setRGB((column*8)+x, y, index);
                    }
                }
            }

            try {
                ImageIO.write(image, "png", new FileOutputStream("/Users/paulrene/Desktop/test.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageList.add(imageStruct);
         */
        }
            
    }

    private boolean isBitSet(int value, int bit) {
        return (value & (2^bit)) == (2^bit);
    }
    
    public static final AmigaIcon fromBytes(byte[] data) throws FileNotFoundException, IOException {
        return new AmigaIcon(data);
    }
    

    public static class ImageStructure {

        public boolean hasImageData;
        public int depth;
        public int height;
        public int width;
        public int topEdge;
        public int leftEdge;

    }

    public static enum IconType {
        DISK(1, true), DRAWER(2, true), TOOL(3, false), PROJECT(4, false), TRASHCAN(5, true), DEVICE(6, false), KICKSTART(
                7, false), APPICON(8, false);

        private int id;
        private boolean drawer;

        private IconType(int id, boolean drawer) {
            this.id = id;
            this.drawer = drawer;
        }

        public boolean isDrawer() {
            return drawer;
        }

        private static IconType resolve(int id) {
            for (IconType type : IconType.values()) {
                if (type.id == id)
                    return type;
            }
            return null;
        }
    }

}