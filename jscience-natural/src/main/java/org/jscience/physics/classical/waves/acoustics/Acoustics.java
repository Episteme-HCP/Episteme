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

package org.jscience.physics.classical.waves.acoustics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Acoustics equations - sound propagation, intensity, resonance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Acoustics {

    /** Speed of sound in air at 20Ã‚Â°C */
    public static final Real C_AIR_20C = Real.of(343.0);
    /** Speed of sound in water */
    public static final Real C_WATER = Real.of(1480.0);

    /**
     * Speed of sound in air: v Ã¢â€°Ë† 331 + 0.6T (m/s, T in Ã‚Â°C)
     */
    public static Real speedOfSoundInAir(Real temperatureCelsius) {
        return Real.of(331).add(Real.of(0.6).multiply(temperatureCelsius));
    }

    /**
     * Speed of sound in air (more accurate formula).
     * v = 331.3 * sqrt(1 + T/273.15)
     */
    public static Real speedOfSoundInAirAccurate(Real tempCelsius) {
        return Real.of(331.3).multiply(Real.ONE.add(tempCelsius.divide(Real.of(273.15))).sqrt());
    }

    /**
     * Sound intensity level (decibels): L = 10 logÃ¢â€šÂÃ¢â€šâ‚¬(I/IÃ¢â€šâ‚¬)
     * where IÃ¢â€šâ‚¬ = 10Ã¢ÂÂ»Ã‚Â¹Ã‚Â² W/mÃ‚Â² (threshold of hearing)
     */
    public static Real soundIntensityLevel(Real intensity) {
        Real I0 = Real.of(1e-12);
        return Real.of(10).multiply(intensity.divide(I0).log10());
    }

    /**
     * Pressure to decibels: L = 20 logÃ¢â€šÂÃ¢â€šâ‚¬(p/pÃ¢â€šâ‚¬)
     * where pÃ¢â€šâ‚¬ = 2Ãƒâ€”10Ã¢ÂÂ»Ã¢ÂÂµ Pa (reference pressure)
     */
    public static Real pressureToDecibels(Real pressure) {
        Real p0 = Real.of(2e-5);
        return Real.of(20).multiply(pressure.divide(p0).log10());
    }

    /**
     * Doppler effect: f' = f(v + v_observer)/(v - v_source)
     */
    public static Real dopplerFrequency(Real sourceFreq, Real soundSpeed,
            Real observerVelocity, Real sourceVelocity) {
        Real numerator = soundSpeed.add(observerVelocity);
        Real denominator = soundSpeed.subtract(sourceVelocity);
        return sourceFreq.multiply(numerator.divide(denominator));
    }

    /**
     * Resonance frequency of closed pipe: f_n = (2n-1)v/(4L)
     */
    public static Real closedPipeResonance(int harmonicNumber, Real soundSpeed, Real length) {
        return Real.of(2 * harmonicNumber - 1).multiply(soundSpeed)
                .divide(Real.of(4).multiply(length));
    }

    /**
     * Resonance frequency of open pipe: f_n = nv/(2L)
     */
    public static Real openPipeResonance(int harmonicNumber, Real soundSpeed, Real length) {
        return Real.of(harmonicNumber).multiply(soundSpeed).divide(Real.TWO.multiply(length));
    }

    /**
     * Beat frequency: f_beat = |fÃ¢â€šÂ - fÃ¢â€šâ€š|
     */
    public static Real beatFrequency(Real freq1, Real freq2) {
        return freq1.subtract(freq2).abs();
    }

    /**
     * Acoustic impedance: Z = ÃÂv (density Ãƒâ€” sound speed)
     */
    public static Real acousticImpedance(Real density, Real soundSpeed) {
        return density.multiply(soundSpeed);
    }

    /**
     * Sound intensity from power: I = P/(4Ãâ‚¬rÃ‚Â²)
     */
    public static Real soundIntensity(Real power, Real distance) {
        Real area = Real.of(4).multiply(Real.PI).multiply(distance.pow(2));
        return power.divide(area);
    }

    /**
     * MIDI note number to frequency.
     * f = 440 * 2^((n-69)/12)
     */
    public static Real midiToFrequency(int midiNote) {
        return Real.of(440).multiply(Real.of(2).pow(Real.of(midiNote - 69).divide(Real.of(12))));
    }

    /**
     * Frequency to MIDI note number.
     * n = 69 + 12 * logÃ¢â€šâ€š(f/440)
     */
    public static int frequencyToMidi(Real frequency) {
        // n = 69 + 12 * log(f/440) / log(2)
        Real ratio = frequency.divide(Real.of(440));
        Real log2Ratio = ratio.log().divide(Real.LN2);
        Real result = Real.of(69).add(Real.of(12).multiply(log2Ratio));
        // Round to nearest integer
        return (int) Math.round(result.doubleValue());
    }
}


