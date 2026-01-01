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

package org.jscience.engineering.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads weather/solar data for SmartGrid simulations.
 * Supports CSV format with hour, wind speed, solar irradiance, temperature.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WeatherDataLoader implements org.jscience.io.InputLoader<List<WeatherDataLoader.WeatherRecord>> {

    @Override
    public List<WeatherRecord> load(String resourceId) throws java.io.IOException {
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            return loadCSV(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/data/weather/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<WeatherDataLoader.WeatherRecord>> getResourceType() {
        return (Class<List<WeatherDataLoader.WeatherRecord>>) (Class<?>) List.class;
    }

    public static class WeatherRecord {
        public int hour;
        public double windSpeedMs;
        public double solarIrradianceWm2;
        public double temperatureC;

        public WeatherRecord(int hour, double wind, double solar, double temp) {
            this.hour = hour;
            this.windSpeedMs = wind;
            this.solarIrradianceWm2 = solar;
            this.temperatureC = temp;
        }

        /** Calculate wind power factor (0-1) based on typical turbine curve */
        public double getWindPowerFactor() {
            // Cut-in at 3 m/s, rated at 12 m/s, cut-out at 25 m/s
            if (windSpeedMs < 3)
                return 0;
            if (windSpeedMs > 25)
                return 0;
            if (windSpeedMs >= 12)
                return 1.0;
            return (windSpeedMs - 3) / 9.0;
        }

        /** Calculate solar power factor (0-1) based on irradiance */
        public double getSolarPowerFactor() {
            // Max irradiance ~1000 W/mÃ‚Â²
            return Math.min(1.0, solarIrradianceWm2 / 1000.0);
        }
    }

    public static List<WeatherRecord> loadCSV(InputStream is) throws IOException {
        List<WeatherRecord> records = new ArrayList<>();
        if (is == null)
            return records;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isBlank())
                    continue;
                if (header) {
                    header = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int hour = Integer.parseInt(parts[0].trim());
                    double wind = Double.parseDouble(parts[1].trim());
                    double solar = Double.parseDouble(parts[2].trim());
                    double temp = Double.parseDouble(parts[3].trim());
                    records.add(new WeatherRecord(hour, wind, solar, temp));
                }
            }
        }
        return records;
    }
}


