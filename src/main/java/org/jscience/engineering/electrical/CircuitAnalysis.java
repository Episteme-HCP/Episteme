package org.jscience.engineering.electrical;

/**
 * Electrical circuit analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class CircuitAnalysis {

    /**
     * Ohm's law: V = I * R
     */
    public static double voltage(double current, double resistance) {
        return current * resistance;
    }

    public static double current(double voltage, double resistance) {
        return voltage / resistance;
    }

    public static double resistance(double voltage, double current) {
        return voltage / current;
    }

    /**
     * Power: P = V * I = I²R = V²/R
     */
    public static double power(double voltage, double current) {
        return voltage * current;
    }

    public static double powerFromCurrent(double current, double resistance) {
        return current * current * resistance;
    }

    public static double powerFromVoltage(double voltage, double resistance) {
        return voltage * voltage / resistance;
    }

    /**
     * Series resistance: R_total = R1 + R2 + ...
     */
    public static double seriesResistance(double... resistances) {
        double total = 0;
        for (double r : resistances)
            total += r;
        return total;
    }

    /**
     * Parallel resistance: 1/R_total = 1/R1 + 1/R2 + ...
     */
    public static double parallelResistance(double... resistances) {
        double sum = 0;
        for (double r : resistances)
            sum += 1.0 / r;
        return 1.0 / sum;
    }

    /**
     * Series capacitance: 1/C_total = 1/C1 + 1/C2 + ...
     */
    public static double seriesCapacitance(double... capacitances) {
        double sum = 0;
        for (double c : capacitances)
            sum += 1.0 / c;
        return 1.0 / sum;
    }

    /**
     * Parallel capacitance: C_total = C1 + C2 + ...
     */
    public static double parallelCapacitance(double... capacitances) {
        double total = 0;
        for (double c : capacitances)
            total += c;
        return total;
    }

    /**
     * Capacitive reactance: Xc = 1 / (2πfC)
     */
    public static double capacitiveReactance(double frequency, double capacitance) {
        return 1.0 / (2 * Math.PI * frequency * capacitance);
    }

    /**
     * Inductive reactance: XL = 2πfL
     */
    public static double inductiveReactance(double frequency, double inductance) {
        return 2 * Math.PI * frequency * inductance;
    }

    /**
     * Impedance of RLC series circuit.
     * Z = sqrt(R² + (XL - Xc)²)
     */
    public static double impedance(double resistance, double inductiveReactance,
            double capacitiveReactance) {
        double reactance = inductiveReactance - capacitiveReactance;
        return Math.sqrt(resistance * resistance + reactance * reactance);
    }

    /**
     * Phase angle of RLC circuit.
     * φ = atan((XL - Xc) / R)
     */
    public static double phaseAngle(double resistance, double inductiveReactance,
            double capacitiveReactance) {
        return Math.atan((inductiveReactance - capacitiveReactance) / resistance);
    }

    /**
     * Resonant frequency of LC circuit.
     * f0 = 1 / (2π√(LC))
     */
    public static double resonantFrequency(double inductance, double capacitance) {
        return 1.0 / (2 * Math.PI * Math.sqrt(inductance * capacitance));
    }

    /**
     * Quality factor of RLC circuit.
     * Q = (1/R) * sqrt(L/C)
     */
    public static double qualityFactor(double resistance, double inductance, double capacitance) {
        return (1.0 / resistance) * Math.sqrt(inductance / capacitance);
    }

    /**
     * RC time constant: τ = R * C
     */
    public static double rcTimeConstant(double resistance, double capacitance) {
        return resistance * capacitance;
    }

    /**
     * RL time constant: τ = L / R
     */
    public static double rlTimeConstant(double inductance, double resistance) {
        return inductance / resistance;
    }

    /**
     * Capacitor charging voltage.
     * V(t) = V0 * (1 - e^(-t/τ))
     */
    public static double capacitorCharging(double V0, double t, double tau) {
        return V0 * (1 - Math.exp(-t / tau));
    }

    /**
     * Capacitor discharging voltage.
     * V(t) = V0 * e^(-t/τ)
     */
    public static double capacitorDischarging(double V0, double t, double tau) {
        return V0 * Math.exp(-t / tau);
    }

    /**
     * RMS voltage from peak.
     * V_rms = V_peak / √2
     */
    public static double rmsFromPeak(double peakVoltage) {
        return peakVoltage / Math.sqrt(2);
    }

    /**
     * Three-phase power (balanced load).
     * P = √3 * V_line * I_line * cos(φ)
     */
    public static double threePhasePowr(double lineVoltage, double lineCurrent, double powerFactor) {
        return Math.sqrt(3) * lineVoltage * lineCurrent * powerFactor;
    }

    /**
     * Transformer voltage ratio.
     * V1/V2 = N1/N2
     */
    public static double transformerVoltage(double V1, int N1, int N2) {
        return V1 * N2 / N1;
    }
}
