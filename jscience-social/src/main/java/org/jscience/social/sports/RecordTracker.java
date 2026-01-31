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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Tracks and analyzes world records across various sports disciplines.
 * Provides statistical trend analysis and improvement rate prediction.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class RecordTracker {

    private RecordTracker() {}

    /** Data model for a world record entry. */
    public record WorldRecord(
        String discipline,
        String category,
        Real performance,
        String unit,
        String athlete,
        String nationality,
        TimeCoordinate date,
        String venue,
        boolean isHigherBetter
    ) implements Serializable {}

    /** Analysis of record progression for a discipline. */
    public record RecordProgression(
        String discipline,
        List<WorldRecord> history,
        Real improvementRate,
        Real predictedNextRecord
    ) implements Serializable {}

    private static final List<WorldRecord> RECORD_DATABASE = new ArrayList<>();

    /** Adds a record to the shared tracking database. */
    public static void addRecord(WorldRecord record) {
        if (record != null) {
            RECORD_DATABASE.add(record);
        }
    }

    /** Retrieves the current standing world record for a discipline and category. */
    public static Optional<WorldRecord> getCurrentRecord(String discipline, String category) {
        return RECORD_DATABASE.stream()
            .filter(r -> r.discipline().equalsIgnoreCase(discipline))
            .filter(r -> r.category().equalsIgnoreCase(category))
            .max((a, b) -> a.isHigherBetter() ? a.performance().compareTo(b.performance()) : b.performance().compareTo(a.performance()));
    }

    /** Generates a progression report including improvement trends. */
    public static RecordProgression getProgression(String discipline, String category) {
        List<WorldRecord> history = RECORD_DATABASE.stream()
            .filter(r -> r.discipline().equalsIgnoreCase(discipline))
            .filter(r -> r.category().equalsIgnoreCase(category))
            .sorted((a, b) -> a.date().compareTo(b.date()))
            .toList();
        
        if (history.size() < 2) return new RecordProgression(discipline, history, Real.ZERO, Real.ZERO);
        
        int n = history.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < n; i++) {
            double perf = history.get(i).performance().doubleValue();
            sumX += i; sumY += perf; sumXY += i * perf; sumX2 += i * i;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double lastPerf = history.get(n - 1).performance().doubleValue();
        double predicted = history.get(0).isHigherBetter() ? lastPerf + Math.abs(slope) : lastPerf - Math.abs(slope);
        
        return new RecordProgression(discipline, history, Real.of(Math.abs(slope)), Real.of(predicted));
    }

    /** Detects if the rate of record improvement is significantly slowing down. */
    public static boolean isPlateauing(String discipline, String category, int recentRecords) {
        RecordProgression prog = getProgression(discipline, category);
        if (prog.history().size() < recentRecords * 2) return false;
        
        List<WorldRecord> history = prog.history();
        int n = history.size();
        double recentImp = 0, histImp = 0;
        
        for (int i = n - recentRecords; i < n - 1; i++) {
            recentImp += Math.abs(history.get(i + 1).performance().doubleValue() - history.get(i).performance().doubleValue());
        }
        for (int i = 0; i < n - recentRecords - 1; i++) {
            histImp += Math.abs(history.get(i + 1).performance().doubleValue() - history.get(i).performance().doubleValue());
        }
        
        return (recentImp / (recentRecords - 1)) < (histImp / (n - recentRecords - 1)) * 0.5;
    }
}

