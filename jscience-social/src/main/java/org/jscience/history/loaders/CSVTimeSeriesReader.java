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

package org.jscience.history.loaders;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.TimePoint;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Loads time-series data from CSV files.
 * Useful for economic and sociological historical data.
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

    /**
     * Loads a CSV file into a time-series map.
     * Assumes format: Date,Value (e.g., "2023-01-01,100.5")
     * 
     * @param resourcePath Classpath resource
     * @return Map of TimePoint to Real value
     */
    public static Map<TimePoint, Real> loadTimeSeries(String resourcePath) {
        Map<TimePoint, Real> series = new TreeMap<>();

        try (InputStream is = CSVTimeSeriesReader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
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

