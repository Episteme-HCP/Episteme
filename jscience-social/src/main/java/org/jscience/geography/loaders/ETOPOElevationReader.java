/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.geography.loaders;

import org.jscience.io.AbstractResourceReader;

/**
 * Reader for ETOPO1 data (Global Relief Model).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ETOPOElevationReader extends AbstractResourceReader<Double> {

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getDescription() {
        return "ETOPO1 Global Relief Model Reader.";
    }

    @Override
    public String getResourcePath() {
        return org.jscience.io.Configuration.get("data.etopo.path", "/data/etopo/");
    }

    @Override
    public Class<Double> getResourceType() {
        return Double.class;
    }

    @Override
    protected Double loadFromSource(String coordinates) throws Exception {
        // coordinates: "lat,lon"
        String[] parts = coordinates.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        String dataPath = org.jscience.io.Configuration.get("data.etopo.path", "/data/etopo/");
        java.io.File file = new java.io.File(dataPath, "ETOPO1_Ice_g_int.bin");
        if (!file.exists()) return null;

        // ETOPO1: 1 arc-minute grid.
        // 21601 columns (longitudes -180 to 180)
        // 10801 rows (latitudes 90 to -90)
        // Row 0 is North (90). Row 10800 is South (-90).
        // Col 0 is West (-180). Col 21600 is East (180).
        
        // Calculate row and col
        int row = (int) Math.round((90.0 - lat) * 60.0);
        int col = (int) Math.round((lon + 180.0) * 60.0);
        
        if (row < 0) row = 0; if (row > 10800) row = 10800;
        if (col < 0) col = 0; if (col > 21600) col = 21600;
        
        // 2 bytes per sample, 16-bit signed integer, Little Endian
        long offset = (row * 21601L + col) * 2;
        
        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "r")) {
            raf.seek(offset);
            int b1 = raf.read();
            int b2 = raf.read();
            if (b1 == -1 || b2 == -1) return null;
            
            // Little Endian conversion
            short val = (short) ((b2 << 8) | b1);
            return (double) val;
        }
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.etopoelevation.name"); }
}
