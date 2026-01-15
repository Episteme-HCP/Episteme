/*
 *        %Z%%M% %I% %E% %U%
 *
 * Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.medicine.volumetric;

import java.io.DataInputStream;
import java.io.FileNotFoundException;

import java.net.URL;


/**
 * A VolFile is object holds the volume data from a file.
 */
public class VolFile {
    /** DOCUMENT ME! */
    int xDim = 0;

    /** DOCUMENT ME! */
    int dummyint = 0;

    /** DOCUMENT ME! */
    byte dummybyte = 0;

    /** DOCUMENT ME! */
    short dummyshort = 0;

    /** DOCUMENT ME! */
    int yDim = 0;

    /** DOCUMENT ME! */
    int zDim = 0;

    /** DOCUMENT ME! */
    float xSpace = 0;

    /** DOCUMENT ME! */
    float ySpace = 0;

    /** DOCUMENT ME! */
    float zSpace = 0;

    /** DOCUMENT ME! */
    int dataOffset = 0;

    /** DOCUMENT ME! */
    int minVal = 0;

    /** DOCUMENT ME! */
    int maxVal = 0;

    /** DOCUMENT ME! */
    int bytesPerVoxel = 0;

    /** DOCUMENT ME! */
    byte[] id = new byte[64];

    /** DOCUMENT ME! */
    int pos = 0;

    /** DOCUMENT ME! */
    DataInputStream file = null;

    /** DOCUMENT ME! */
    byte[][][] fileData;

    // This windowing stuff is for the 16->8 mapping for the CT data
    // TODO: make this another attribute
    /** DOCUMENT ME! */
    double windowCenter = 880.0;

    /** DOCUMENT ME! */
    double windowWidth = 930.0;

    /** DOCUMENT ME! */
    double windowBase = windowCenter - (windowWidth / 2);

/**
     * Creates a VolFile from a URL.
     *
     * @param voldat The URL for a .vol file holding the volume
     * @throws java.io.IOException DOCUMENT ME!
     */
    public VolFile(URL voldat) throws java.io.IOException {
        try {
            file = new DataInputStream(voldat.openStream());
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        }

        byte[] magicBuffer = new byte[4];
        file.read(magicBuffer, 0, 4);
        pos += 4;

        String magic = new String(magicBuffer, 0, 4);

        if (!magic.equals("vol3")) {
            System.out.println("file specified is not a .vol file" +
                " header begins with " + magic);
            System.exit(0);
        }

        xDim = file.readInt();
        pos += 4;
        yDim = file.readInt();
        pos += 4;
        zDim = file.readInt();
        pos += 4;
        xSpace = file.readFloat();
        pos += 4;
        ySpace = file.readFloat();
        pos += 4;
        zSpace = file.readFloat();
        pos += 4;
        dataOffset = file.readInt();
        pos += 4;
        minVal = (int) file.readShort();
        pos += 2;
        maxVal = (int) file.readShort();
        pos += 2;
        bytesPerVoxel = (int) file.readByte();
        pos++;

        /* Following is padding to avoid need to seek */
        dummybyte = file.readByte();
        pos++;

        for (int i = 0; i < 13; i++) {
            dummyshort = file.readShort();
            pos += 2;
        }

        file.read(id, 0, 64);
        pos += 64;

        fileData = new byte[zDim][yDim][xDim];

        if (pos != dataOffset) {
            System.out.print("VolFile: Pointer Mismatch");
        }

        if (bytesPerVoxel == 1) {
            System.out.print("Reading data...");

            for (int z = 0; z < zDim; z++) {
                for (int y = 0; y < yDim; y++) {
                    int vIndex = ((z * xDim * yDim) + (y * xDim));
                    byte[] dataRow = fileData[z][y];
                    file.readFully(dataRow, 0, xDim);
                }
            }

            System.out.println("done");
        } else {
            byte[] buffer = new byte[xDim * 2];
            System.out.print("Reading and windowing data");

            for (int z = 0; z < zDim; z++) {
                for (int y = 0; y < yDim; y++) {
                    int vIndex = ((z * xDim * yDim) + (y * xDim)) * 2;
                    byte[] dataRow = fileData[z][y];
                    file.readFully(buffer, 0, xDim * 2);

                    for (int x = 0; x < xDim; x++) {
                        int index = x * 2;

                        // Map the pair of bytes into a short and then window
                        // into a byte
                        int low = buffer[index + 1];

                        if (low < 0) {
                            low += 256;
                        }

                        int high = buffer[index];

                        if (high < 0) {
                            high += 256;
                        }

                        short fileValue = (short) ((high << 8) + (low << 0));
                        fileValue -= minVal;

                        double scaleValue = (fileValue - windowBase) / windowWidth;

                        if (scaleValue > 1.0) {
                            scaleValue = 1.0;
                        }

                        if (scaleValue < 0.0) {
                            scaleValue = 0.0;
                        }

                        dataRow[x] = (byte) (scaleValue * 255);
                    }
                }

                System.out.print(".");
            }

            System.out.println("done");
        }
    }

    /**
     * Returns the ID string for the volume file
     *
     * @return DOCUMENT ME!
     */
    String getId() {
        return new String(id);
    }

    /**
     * Gets the X dimension of the volume
     *
     * @return DOCUMENT ME!
     */
    int getXDim() {
        return xDim;
    }

    /**
     * Gets the Y dimension of the volume
     *
     * @return DOCUMENT ME!
     */
    int getYDim() {
        return yDim;
    }

    /**
     * Gets the Z dimension of the volume
     *
     * @return DOCUMENT ME!
     */
    int getZDim() {
        return zDim;
    }
}
