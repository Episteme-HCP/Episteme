package org.jscience.medicine.cardiology;

/**
 * Basic ECG (Electrocardiogram) analysis utilities.
 */
public class ECGAnalysis {

    private ECGAnalysis() {
    }

    /**
     * Calculates heart rate from R-R intervals.
     * HR = 60 / RR_interval_seconds
     * 
     * @param rrIntervalMs R-R interval in milliseconds
     * @return Heart rate in beats per minute
     */
    public static double heartRateFromRR(double rrIntervalMs) {
        return 60000.0 / rrIntervalMs;
    }

    /**
     * Calculates average heart rate from multiple R-peak timestamps.
     */
    public static double averageHeartRate(double[] rPeakTimesMs) {
        if (rPeakTimesMs.length < 2)
            return 0;

        double totalInterval = rPeakTimesMs[rPeakTimesMs.length - 1] - rPeakTimesMs[0];
        int numIntervals = rPeakTimesMs.length - 1;
        double avgRR = totalInterval / numIntervals;

        return 60000.0 / avgRR;
    }

    /**
     * Heart rate variability (SDNN) - standard deviation of NN intervals.
     */
    public static double hrvSDNN(double[] rrIntervalsMs) {
        if (rrIntervalsMs.length < 2)
            return 0;

        double mean = 0;
        for (double rr : rrIntervalsMs)
            mean += rr;
        mean /= rrIntervalsMs.length;

        double sumSq = 0;
        for (double rr : rrIntervalsMs) {
            sumSq += Math.pow(rr - mean, 2);
        }

        return Math.sqrt(sumSq / rrIntervalsMs.length);
    }

    /**
     * RMSSD - root mean square of successive differences.
     * Important HRV metric.
     */
    public static double hrvRMSSD(double[] rrIntervalsMs) {
        if (rrIntervalsMs.length < 2)
            return 0;

        double sumSqDiff = 0;
        for (int i = 1; i < rrIntervalsMs.length; i++) {
            double diff = rrIntervalsMs[i] - rrIntervalsMs[i - 1];
            sumSqDiff += diff * diff;
        }

        return Math.sqrt(sumSqDiff / (rrIntervalsMs.length - 1));
    }

    /**
     * QTc calculation (Bazett's formula).
     * QTc = QT / sqrt(RR)
     * 
     * @param qtIntervalMs QT interval in ms
     * @param rrIntervalMs RR interval in ms
     * @return Corrected QT interval
     */
    public static double qtcBazett(double qtIntervalMs, double rrIntervalMs) {
        return qtIntervalMs / Math.sqrt(rrIntervalMs / 1000);
    }

    /**
     * QTc using Fridericia formula (better for fast/slow HR).
     * QTc = QT / RR^(1/3)
     */
    public static double qtcFridericia(double qtIntervalMs, double rrIntervalMs) {
        return qtIntervalMs / Math.cbrt(rrIntervalMs / 1000);
    }

    /**
     * Checks if QTc is prolonged.
     * Normal: <440ms male, <460ms female
     */
    public static boolean isQTcProlonged(double qtcMs, boolean isMale) {
        return qtcMs > (isMale ? 440 : 460);
    }

    /**
     * Simple R-peak detection (threshold-based).
     * Returns indices of detected R-peaks.
     */
    public static int[] detectRPeaks(double[] ecgSignal, double threshold) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();

        for (int i = 1; i < ecgSignal.length - 1; i++) {
            if (ecgSignal[i] > threshold &&
                    ecgSignal[i] > ecgSignal[i - 1] &&
                    ecgSignal[i] > ecgSignal[i + 1]) {
                peaks.add(i);
            }
        }

        return peaks.stream().mapToInt(Integer::intValue).toArray();
    }
}
