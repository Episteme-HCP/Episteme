/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.classical.waves.acoustics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Acoustics equations - sound propagation, intensity, resonance.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Acoustics {

    /**
     * Speed of sound in air: v ≈ 331 + 0.6T (m/s, T in °C)
     */
    public static Real speedOfSoundInAir(Real temperatureCelsius) {
        return Real.of(331).add(Real.of(0.6).multiply(temperatureCelsius));
    }

    /**
     * Sound intensity level (decibels): L = 10 log₁₀(I/I₀)
     * where I₀ = 10⁻¹² W/m² (threshold of hearing)
     */
    public static Real soundIntensityLevel(Real intensity) {
        Real I0 = Real.of(1e-12); // W/m²
        return Real.of(10 * Math.log10(intensity.divide(I0).doubleValue()));
    }

    /**
     * Doppler effect: f' = f(v + v_observer)/(v - v_source)
     * Positive velocities approach each other
     */
    public static Real dopplerFrequency(Real sourceFreq, Real soundSpeed,
            Real observerVelocity, Real sourceVelocity) {
        Real numerator = soundSpeed.add(observerVelocity);
        Real denominator = soundSpeed.subtract(sourceVelocity);
        return sourceFreq.multiply(numerator.divide(denominator));
    }

    /**
     * Resonance frequency of closed pipe: f_n = nv/(2L)
     */
    public static Real closedPipeResonance(int harmonicNumber, Real soundSpeed, Real length) {
        return Real.of(harmonicNumber).multiply(soundSpeed).divide(Real.TWO.multiply(length));
    }

    /**
     * Resonance frequency of open pipe: f_n = nv/(2L)
     * Same as closed but different harmonic series
     */
    public static Real openPipeResonance(int harmonicNumber, Real soundSpeed, Real length) {
        return Real.of(harmonicNumber).multiply(soundSpeed).divide(Real.TWO.multiply(length));
    }

    /**
     * Beat frequency: f_beat = |f₁ - f₂|
     */
    public static Real beatFrequency(Real freq1, Real freq2) {
        return freq1.subtract(freq2).abs();
    }

    /**
     * Acoustic impedance: Z = ρv (density × sound speed)
     */
    public static Real acousticImpedance(Real density, Real soundSpeed) {
        return density.multiply(soundSpeed);
    }

    /**
     * Sound intensity from power: I = P/(4πr²)
     */
    public static Real soundIntensity(Real power, Real distance) {
        Real area = Real.of(4 * Math.PI).multiply(distance).multiply(distance);
        return power.divide(area);
    }

    // --- Methods merged from Acoustics2 ---

    public static final double C_AIR_20C = 343.0;
    public static final double C_WATER = 1480.0;

    public static double speedOfSoundInAir(double tempCelsius) {
        return 331.3 * Math.sqrt(1 + tempCelsius / 273.15);
    }

    public static double pressureToDecibels(double pressure) {
        return 20 * Math.log10(pressure / 2e-5);
    }

    public static double closedPipeFrequency(int harmonic, double length, double c) {
        return (2 * harmonic - 1) * c / (4 * length);
    }

    public static double openPipeFrequency(int harmonic, double length, double c) {
        return harmonic * c / (2 * length);
    }

    public static double beatFrequency(double f1, double f2) {
        return Math.abs(f1 - f2);
    }

    public static double midiToFrequency(int midiNote) {
        return 440.0 * Math.pow(2, (midiNote - 69) / 12.0);
    }

    public static int frequencyToMidi(double frequency) {
        return (int) Math.round(69 + 12 * Math.log(frequency / 440.0) / Math.log(2));
    }
}
