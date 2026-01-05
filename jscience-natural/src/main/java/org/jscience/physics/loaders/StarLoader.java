/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jscience.io.AbstractResourceReader;

/**
 * Loads Star Catalog Data (CSV).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarLoader extends AbstractResourceReader<List<StarLoader.Star>> {

    @Override
    protected List<Star> loadFromSource(String id) throws Exception {
        // StarLoader typically loads a full catalog from resource
        return loadResource(id);
    }

    @Override
    public String getResourcePath() {
        return "/org/jscience/physics/astronomy/data/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Star>> getResourceType() {
        return (Class<List<Star>>) (Class<?>) List.class;
    }

    public List<Star> loadResource(String path) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null)
                throw new java.io.IOException("Star catalog not found: " + path);
            return loadCSV(is);
        }
    }

    public static class Star {
        public String name;
        public double ra; // Right Ascension (degrees)
        public double dec; // Declination (degrees)
        public double dist; // Distance (light years)
        public double mag; // Apparent Magnitude
        public String spectralType; // O, B, A, F, G, K, M

        public Star(String name, double ra, double dec, double dist, double mag, String spectralType) {
            this.name = name;
            this.ra = ra;
            this.dec = dec;
            this.dist = dist;
            this.mag = mag;
            this.spectralType = spectralType;
        }
    }

    public static List<Star> loadCSV(InputStream is) {
        List<Star> stars = new ArrayList<>();
        if (is == null)
            return stars;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // Name,RA(deg),Dec(deg),Dist(ly),Mag,Type
                    String name = parts[0].trim();
                    double ra = Double.parseDouble(parts[1].trim());
                    double dec = Double.parseDouble(parts[2].trim());
                    double dist = Double.parseDouble(parts[3].trim());
                    double mag = Double.parseDouble(parts[4].trim());
                    String type = parts[5].trim();

                    stars.add(new Star(name, ra, dec, dist, mag, type));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stars;
    }
}
