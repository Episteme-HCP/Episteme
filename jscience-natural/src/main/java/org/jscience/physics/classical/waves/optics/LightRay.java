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

package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a light ray for geometric optics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LightRay {

    private Real wavelength; // nm
    private Real intensity; // W/m^2
    private Real[] direction; // 3D unit vector
    private Real[] origin; // 3D position

    public LightRay(Real wavelength, Real intensity, Real[] origin, Real[] direction) {
        this.wavelength = wavelength;
        this.intensity = intensity;
        this.origin = origin;
        this.direction = normalize(direction);
    }

    private Real[] normalize(Real[] v) {
        Real mag = v[0].pow(2).add(v[1].pow(2)).add(v[2].pow(2)).sqrt();
        return new Real[] { v[0].divide(mag), v[1].divide(mag), v[2].divide(mag) };
    }

    /**
     * Frequency: $\nu = c / \lambda$
     */
    public Real getFrequency() {
        Real c = Real.of(299792458.0); // m/s
        Real wavelengthM = wavelength.multiply(Real.of(1e-9)); // nm to m
        return c.divide(wavelengthM);
    }

    /**
     * Photon energy: $E = h\nu$
     */
    public Real getPhotonEnergy() {
        Real h = Real.of(6.62607015e-34); // J*s
        return h.multiply(getFrequency());
    }

    /**
     * Wave number: $k = 2\pi / \lambda$
     */
    public Real getWaveNumber() {
        Real wavelengthM = wavelength.multiply(Real.of(1e-9));
        return Real.of(2.0 * Math.PI).divide(wavelengthM);
    }

    // --- Accessors ---
    public Real getWavelength() {
        return wavelength;
    }

    public Real getIntensity() {
        return intensity;
    }

    public Real[] getDirection() {
        return direction;
    }

    public Real[] getOrigin() {
        return origin;
    }
}


