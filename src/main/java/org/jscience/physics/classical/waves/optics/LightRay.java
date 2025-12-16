package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a light ray for geometric optics.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
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
