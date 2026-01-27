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

package org.jscience.history.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.jscience.history.time.TimePoint;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Loads time-series data from CSV files.
 * Format: Date,Value (e.g., "2023-01-01,100.5")
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CSVTimeSeriesReader extends AbstractResourceReader<Map<TimePoint, Real>> {

    public CSVTimeSeriesReader() {
    }

    @Override
    public String getResourcePath() {
        return "/data/history/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<TimePoint, Real>> getResourceType() {
        return (Class<Map<TimePoint, Real>>) (Class<?>) Map.class;
    }

    @Override
    public String getCategory() {
        return "History";
    }

    @Override
    public String getDescription() {
        return "Historical Time Series Reader (CSV).";
    }

    @Override
    public String getLongDescription() {
        return "Reads historical time-series data from CSV files for economic and sociological analysis.";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"RFC 4180"};
    }

    @Override
    protected Map<TimePoint, Real> loadFromSource(String id) throws Exception {
        return loadTimeSeries(id);
    }

    @Override
    protected MiniCatalog<Map<TimePoint, Real>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Map<TimePoint, Real>> getAll() {
                return List.of(Collections.emptyMap());
            }

            @Override
            public Optional<Map<TimePoint, Real>> findByName(String name) {
                return Optional.of(Collections.emptyMap());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public static Map<TimePoint, Real> loadTimeSeries(String resourcePath) {
        Map<TimePoint, Real> series = new TreeMap<>();
        try (InputStream is = CSVTimeSeriesReader.class.getResourceAsStream(resourcePath)) {
            if (is == null) return Collections.emptyMap();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                boolean header = true;
                while ((line = reader.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            LocalDate date = LocalDate.parse(parts[0].trim());
                            TimePoint tp = TimePoint.of(date.atStartOfDay(ZoneOffset.UTC).toInstant());
                            Real val = Real.of(Double.parseDouble(parts[1].trim()));
                            series.put(tp, val);
                        } catch (Exception e) {}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return series;
    }

    @Override public String getName() { return "CSV Time Series Reader"; }
}
