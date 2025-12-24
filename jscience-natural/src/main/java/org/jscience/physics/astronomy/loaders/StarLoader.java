/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.physics.astronomy.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads Star Catalog Data (CSV).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarLoader {

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
