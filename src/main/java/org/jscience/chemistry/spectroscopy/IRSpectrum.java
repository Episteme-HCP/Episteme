package org.jscience.chemistry.spectroscopy;

/**
 * IR spectroscopy functional group frequencies.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class IRSpectrum {

    /**
     * Functional group with characteristic IR absorption.
     */
    public enum FunctionalGroup {
        // Bonds to hydrogen
        OH_ALCOHOL("O-H (alcohol)", 3200, 3550, "broad"),
        OH_CARBOXYLIC("O-H (carboxylic acid)", 2500, 3300, "very broad"),
        NH_AMINE("N-H (amine)", 3300, 3500, "medium"),
        CH_ALKANE("C-H (sp³)", 2850, 3000, "strong"),
        CH_ALKENE("C-H (sp²)", 3000, 3100, "medium"),
        CH_ALKYNE("C-H (sp)", 3300, 3300, "strong, sharp"),
        CH_ALDEHYDE("C-H (aldehyde)", 2700, 2850, "two peaks"),

        // Multiple bonds
        CC_TRIPLE("C≡C", 2100, 2260, "weak"),
        CN_TRIPLE("C≡N (nitrile)", 2210, 2260, "medium"),
        CC_DOUBLE("C=C", 1620, 1680, "variable"),
        CO_DOUBLE_KETONE("C=O (ketone)", 1705, 1725, "strong"),
        CO_DOUBLE_ALDEHYDE("C=O (aldehyde)", 1720, 1740, "strong"),
        CO_DOUBLE_ESTER("C=O (ester)", 1735, 1750, "strong"),
        CO_DOUBLE_CARBOXYLIC("C=O (carboxylic acid)", 1700, 1725, "strong"),
        CO_DOUBLE_AMIDE("C=O (amide)", 1640, 1690, "strong"),

        // Single bonds
        CO_ETHER("C-O (ether)", 1000, 1300, "strong"),
        CN_AMINE("C-N (amine)", 1020, 1250, "medium"),
        NO2_NITRO("N-O (nitro)", 1515, 1560, "strong");

        private final String name;
        private final int wavenumberLow; // cm⁻¹
        private final int wavenumberHigh;
        private final String intensity;

        FunctionalGroup(String name, int low, int high, String intensity) {
            this.name = name;
            this.wavenumberLow = low;
            this.wavenumberHigh = high;
            this.intensity = intensity;
        }

        public String getName() {
            return name;
        }

        public int getWavenumberLow() {
            return wavenumberLow;
        }

        public int getWavenumberHigh() {
            return wavenumberHigh;
        }

        public String getIntensity() {
            return intensity;
        }

        public boolean matches(double wavenumber) {
            return wavenumber >= wavenumberLow && wavenumber <= wavenumberHigh;
        }
    }

    /**
     * Identifies possible functional groups from a peak wavenumber.
     */
    public static java.util.List<FunctionalGroup> identifyPeak(double wavenumber) {
        java.util.List<FunctionalGroup> matches = new java.util.ArrayList<>();
        for (FunctionalGroup fg : FunctionalGroup.values()) {
            if (fg.matches(wavenumber)) {
                matches.add(fg);
            }
        }
        return matches;
    }

    /**
     * Converts wavelength (μm) to wavenumber (cm⁻¹).
     * ν̃ = 10000 / λ
     */
    public static double wavelengthToWavenumber(double wavelengthMicrons) {
        return 10000.0 / wavelengthMicrons;
    }

    /**
     * Converts wavenumber to wavelength.
     */
    public static double wavenumberToWavelength(double wavenumber) {
        return 10000.0 / wavenumber;
    }

    /**
     * Calculates stretching frequency using harmonic oscillator model.
     * ν̃ = (1/2πc) · √(k/μ)
     * 
     * @param k  Force constant (N/m)
     * @param mu Reduced mass (kg)
     * @return Frequency in cm⁻¹
     */
    public static double stretchingFrequency(double k, double mu) {
        double c = 2.998e10; // cm/s
        return (1.0 / (2 * Math.PI * c)) * Math.sqrt(k / mu);
    }
}
