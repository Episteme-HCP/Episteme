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
 * Reader for SRTM (Shuttle Radar Topography Mission) data.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SRTMElevationReader extends AbstractResourceReader<Double> {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.geography", "Geography");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.srtmelevation.desc", "SRTM Data Reader (HGT format).");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.srtmelevation.longdesc", "Reads high-resolution elevation data from the Shuttle Radar Topography Mission (SRTM) files in HGT format.");
    }

    @Override
    public String getResourcePath() {
        return org.jscience.io.Configuration.get("data.srtm.path", "/data/srtm/");
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

        int latInt = (int) Math.floor(lat);
        int lonInt = (int) Math.floor(lon);

        // File name: N34W119.hgt
        String latStr = (latInt >= 0 ? "N" : "S") + String.format("%02d", Math.abs(latInt));
        String lonStr = (lonInt >= 0 ? "E" : "W") + String.format("%03d", Math.abs(lonInt));
        String filename = latStr + lonStr + ".hgt";

        String dataPath = org.jscience.io.Configuration.get("data.srtm.path", "/data/srtm/");
        java.io.File file = new java.io.File(dataPath, filename);
        if (!file.exists()) return null;

        // SRTM3: 1201x1201. 1200 rows/cols per degree.
        // Row 0 is North. Row 1200 is South.
        // Col 0 is West. Col 1200 is East.
        
        double latDiff = lat - latInt; // 0.0 to 1.0 (relative to bottom-left corner essentially)
        double lonDiff = lon - lonInt; // 0.0 to 1.0

        int row = (int) Math.round((1.0 - latDiff) * 1200);
        int col = (int) Math.round(lonDiff * 1200);

        if (row < 0) row = 0; if (row > 1200) row = 1200;
        if (col < 0) col = 0; if (col > 1200) col = 1200;

        // 2 bytes per sample
        long offset = (row * 1201L + col) * 2;
        
        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "r")) {
            raf.seek(offset);
            short val = raf.readShort(); // Big Endian
            
            // Void value check
            if (val == -32768) return null;
            
            return (double) val;
        }
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.srtmelevation.name", "SRTM Elevation Reader"); }
}
