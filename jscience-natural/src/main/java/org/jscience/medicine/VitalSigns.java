/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.medicine;

/**
 * Immutable record representing a snapshot of vital signs.
 *
 * @param heartRate       heart rate in beats per minute (BPM)
 * @param systolic        systolic blood pressure in mmHg
 * @param diastolic       diastolic blood pressure in mmHg
 * @param spO2            oxygen saturation percentage (0-100)
 * @param respirationRate breaths per minute
 * @param temperature     body temperature in Fahrenheit
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public record VitalSigns(
        int heartRate,
        int systolic,
        int diastolic,
        int spO2,
        int respirationRate,
        double temperature) {
    /**
     * Returns blood pressure as a formatted string "systolic/diastolic".
     */
    public String bloodPressureString() {
        return systolic + "/" + diastolic;
    }

    /**
     * Returns temperature formatted to one decimal place.
     */
    public String temperatureString() {
        return String.format("%.1f", temperature);
    }
}
