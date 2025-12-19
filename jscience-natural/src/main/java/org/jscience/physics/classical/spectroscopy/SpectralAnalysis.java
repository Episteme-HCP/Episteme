package org.jscience.physics.classical.spectroscopy;

/**
 * Blackbody radiation and spectral analysis.
 */
public class SpectralAnalysis {

    public static final double h = 6.62607015e-34; // Planck constant (J·s)
    public static final double c = 299792458.0; // Speed of light (m/s)
    public static final double k = 1.380649e-23; // Boltzmann constant (J/K)

    private SpectralAnalysis() {
    }

    /**
     * Wien's displacement law.
     * λ_max = b / T
     * where b = 2.897771955e-3 m·K
     * 
     * @param temperatureKelvin Temperature in K
     * @return Peak wavelength in meters
     */
    public static double wienPeakWavelength(double temperatureKelvin) {
        double b = 2.897771955e-3; // Wien's displacement constant
        return b / temperatureKelvin;
    }

    /**
     * Stefan-Boltzmann law for total power radiated.
     * P = σ * A * T⁴
     * 
     * @param areaM2            Surface area in m²
     * @param temperatureKelvin Temperature in K
     * @return Power in Watts
     */
    public static double stefanBoltzmannPower(double areaM2, double temperatureKelvin) {
        double sigma = 5.670374419e-8; // Stefan-Boltzmann constant
        return sigma * areaM2 * Math.pow(temperatureKelvin, 4);
    }

    /**
     * Planck's law for spectral radiance.
     * B(λ,T) = (2hc²/λ⁵) * 1/(exp(hc/λkT) - 1)
     * 
     * @param wavelengthMeters  λ
     * @param temperatureKelvin T
     * @return Spectral radiance in W/(m²·sr·m)
     */
    public static double planckRadiance(double wavelengthMeters, double temperatureKelvin) {
        double lambda = wavelengthMeters;
        double T = temperatureKelvin;

        double numerator = 2 * h * c * c / Math.pow(lambda, 5);
        double exponent = h * c / (lambda * k * T);
        double denominator = Math.exp(exponent) - 1;

        return numerator / denominator;
    }

    /**
     * Photon energy from wavelength.
     * E = hc/λ
     */
    public static double photonEnergy(double wavelengthMeters) {
        return h * c / wavelengthMeters;
    }

    /**
     * Wavelength from photon energy.
     * λ = hc/E
     */
    public static double wavelengthFromEnergy(double energyJoules) {
        return h * c / energyJoules;
    }

    /**
     * Color temperature estimate from peak wavelength.
     */
    public static double colorTemperature(double peakWavelengthMeters) {
        return 2.897771955e-3 / peakWavelengthMeters;
    }
}
