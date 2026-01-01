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

package org.jscience.medicine.cardiology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Basic ECG (Electrocardiogram) analysis utilities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ECGAnalysis {

    private ECGAnalysis() {
    }

    /** Heart rate from R-R interval: HR = 60000 / RR_ms */
    public static Real heartRateFromRR(Real rrIntervalMs) {
        return Real.of(60000).divide(rrIntervalMs);
    }

    /** Average heart rate from R-peak timestamps */
    public static Real averageHeartRate(Real[] rPeakTimesMs) {
        if (rPeakTimesMs.length < 2)
            return Real.ZERO;
        Real totalInterval = rPeakTimesMs[rPeakTimesMs.length - 1].subtract(rPeakTimesMs[0]);
        Real avgRR = totalInterval.divide(Real.of(rPeakTimesMs.length - 1));
        return Real.of(60000).divide(avgRR);
    }

    /** HRV SDNN - standard deviation of NN intervals */
    public static Real hrvSDNN(Real[] rrIntervalsMs) {
        if (rrIntervalsMs.length < 2)
            return Real.ZERO;
        Real mean = Real.ZERO;
        for (Real rr : rrIntervalsMs)
            mean = mean.add(rr);
        mean = mean.divide(Real.of(rrIntervalsMs.length));

        Real sumSq = Real.ZERO;
        for (Real rr : rrIntervalsMs) {
            sumSq = sumSq.add(rr.subtract(mean).pow(2));
        }
        return sumSq.divide(Real.of(rrIntervalsMs.length)).sqrt();
    }

    /** RMSSD - root mean square of successive differences */
    public static Real hrvRMSSD(Real[] rrIntervalsMs) {
        if (rrIntervalsMs.length < 2)
            return Real.ZERO;
        Real sumSqDiff = Real.ZERO;
        for (int i = 1; i < rrIntervalsMs.length; i++) {
            Real diff = rrIntervalsMs[i].subtract(rrIntervalsMs[i - 1]);
            sumSqDiff = sumSqDiff.add(diff.pow(2));
        }
        return sumSqDiff.divide(Real.of(rrIntervalsMs.length - 1)).sqrt();
    }

    /** QTc Bazett: QTc = QT / sqrt(RR/1000) */
    public static Real qtcBazett(Real qtIntervalMs, Real rrIntervalMs) {
        return qtIntervalMs.divide(rrIntervalMs.divide(Real.of(1000)).sqrt());
    }

    /** QTc Fridericia: QTc = QT / (RR/1000)^(1/3) */
    public static Real qtcFridericia(Real qtIntervalMs, Real rrIntervalMs) {
        return qtIntervalMs.divide(rrIntervalMs.divide(Real.of(1000)).cbrt());
    }

    /** Checks if QTc is prolonged */
    public static boolean isQTcProlonged(Real qtcMs, boolean isMale) {
        return qtcMs.compareTo(Real.of(isMale ? 440 : 460)) > 0;
    }

    /** Simple R-peak detection (threshold-based) */
    public static int[] detectRPeaks(Real[] ecgSignal, Real threshold) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();
        for (int i = 1; i < ecgSignal.length - 1; i++) {
            if (ecgSignal[i].compareTo(threshold) > 0 &&
                    ecgSignal[i].compareTo(ecgSignal[i - 1]) > 0 &&
                    ecgSignal[i].compareTo(ecgSignal[i + 1]) > 0) {
                peaks.add(i);
            }
        }
        return peaks.stream().mapToInt(Integer::intValue).toArray();
    }
}


