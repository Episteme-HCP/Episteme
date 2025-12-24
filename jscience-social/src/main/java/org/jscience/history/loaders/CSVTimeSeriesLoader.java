package org.jscience.history.loaders;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.TimePoint;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Loads time-series data from CSV files.
 * Useful for economic and sociological historical data.
 */
public class CSVTimeSeriesLoader {

    /**
     * Loads a CSV file into a time-series map.
     * Assumes format: Date,Value (e.g., "2023-01-01,100.5")
     * 
     * @param resourcePath Classpath resource
     * @return Map of TimePoint to Real value
     */
    public static Map<TimePoint, Real> loadTimeSeries(String resourcePath) {
        Map<TimePoint, Real> series = new TreeMap<>();

        try (InputStream is = CSVTimeSeriesLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                // Try file system or fallback
                return Collections.emptyMap();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                boolean header = true;
                while ((line = reader.readLine()) != null) {
                    if (header) {
                        header = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            String dateStr = parts[0].trim();
                            // Handles standard ISO date
                            LocalDate date = LocalDate.parse(dateStr);
                            TimePoint tp = TimePoint.of(date);

                            Real val = Real.of(Double.parseDouble(parts[1].trim()));
                            series.put(tp, val);
                        } catch (Exception e) {
                            // skip bad lines
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return series;
    }
}
