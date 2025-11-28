package org.jscience.physics.constants;

import org.jscience.mathematics.number.Real;

/**
 * Fundamental physical constants based on CODATA 2022 recommendations.
 * <p>
 * <b>Source</b>: CODATA Task Group on Fundamental Constants, published May
 * 2024.
 * These are the internationally recommended values effective as of 2024.
 * </p>
 * 
 * <p>
 * <b>About CODATA values</b>:
 * </p>
 * <ul>
 * <li>Updated every 4 years (next: 2026)</li>
 * <li>Based on measurements through December 31, 2022</li>
 * <li>Published by NIST and international scientific community</li>
 * <li>Uncertainties typically at 10^-9 to 10^-12 level</li>
 * </ul>
 * 
 * <p>
 * <b>Usage example</b>:
 * </p>
 * 
 * <pre>{@code
 * Real c = PhysicalConstants.SPEED_OF_LIGHT;
 * Real h = PhysicalConstants.PLANCK_CONSTANT;
 * Real energy = h.multiply(frequency); // E = hν
 * }</pre>
 * 
 * <p>
 * <b>Note</b>: Since 2019 SI redefinition, several constants have exact values:
 * </p>
 * <ul>
 * <li>Speed of light c = 299792458 m/s (exact)</li>
 * <li>Planck constant h = 6.62607015×10^-34 J⋅s (exact)</li>
 * <li>Elementary charge e = 1.602176634×10^-19 C (exact)</li>
 * <li>Boltzmann constant k = 1.380649×10^-23 J/K (exact)</li>
 * <li>Avogadro constant N_A = 6.02214076×10^23 mol^-1 (exact)</li>
 * </ul>
 * 
 * @see <a href="https://physics.nist.gov/cuu/Constants/">NIST Reference on
 *      Constants</a>
 * @see <a href=
 *      "https://www.codata.org/initiatives/strategic-programme/fundamental-physical-constants">CODATA
 *      TGFC</a>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class PhysicalConstants {

    private PhysicalConstants() {
    } // Prevent instantiation

    // === Electromagnetic Constants ===

    /**
     * Speed of light in vacuum: c = 299792458 m/s (exact, by definition since
     * 2019).
     * <p>
     * <b>Uncertainty</b>: 0 (exact value)
     * </p>
     */
    public static final Real SPEED_OF_LIGHT = Real.of(299792458.0);

    /**
     * Magnetic constant (vacuum permeability): μ₀ = 1.25663706127×10^-6 N/A²
     * (exact).
     * <p>
     * Related to speed of light by: μ₀ε₀ = 1/c²
     * </p>
     */
    public static final Real MAGNETIC_CONSTANT = Real.of(1.25663706127e-6);

    /**
     * Electric constant (vacuum permittivity): ε₀ = 8.8541878188×10^-12 F/m
     * (exact).
     * <p>
     * Derived from: ε₀ = 1/(μ₀c²)
     * </p>
     */
    public static final Real ELECTRIC_CONSTANT = Real.of(8.8541878188e-12);

    /**
     * Elementary charge: e = 1.602176634×10^-19 C (exact, by definition since
     * 2019).
     * <p>
     * <b>Uncertainty</b>: 0 (exact value)
     * </p>
     */
    public static final Real ELEMENTARY_CHARGE = Real.of(1.602176634e-19);

    // === Quantum Mechanics Constants ===

    /**
     * Planck constant: h = 6.62607015×10^-34 J⋅s (exact, by definition since 2019).
     * <p>
     * <b>Uncertainty</b>: 0 (exact value)<br>
     * <b>Relation</b>: E = hν (energy-frequency relation)
     * </p>
     */
    public static final Real PLANCK_CONSTANT = Real.of(6.62607015e-34);

    /**
     * Reduced Planck constant: ℏ = h/(2π) = 1.054571817×10^-34 J⋅s (exact).
     * <p>
     * Used in quantum mechanics: [x,p] = iℏ
     * </p>
     */
    public static final Real REDUCED_PLANCK_CONSTANT = PLANCK_CONSTANT.divide(Real.of(2.0 * Math.PI));

    // === Atomic and Universal Constants ===

    /**
     * Avogadro constant: N_A = 6.02214076×10^23 mol^-1 (exact, by definition since
     * 2019).
     * <p>
     * <b>Uncertainty</b>: 0 (exact value)<br>
     * <b>Definition</b>: Number of entities in one mole
     * </p>
     */
    public static final Real AVOGADRO_CONSTANT = Real.of(6.02214076e23);

    /**
     * Boltzmann constant: k = 1.380649×10^-23 J/K (exact, by definition since
     * 2019).
     * <p>
     * <b>Uncertainty</b>: 0 (exact value)<br>
     * <b>Relation</b>: PV = NkT (ideal gas law)
     * </p>
     */
    public static final Real BOLTZMANN_CONSTANT = Real.of(1.380649e-23);

    /**
     * Molar gas constant: R = N_A × k = 8.314462618 J/(mol⋅K) (exact).
     * <p>
     * <b>Relation</b>: PV = nRT (ideal gas law)
     * </p>
     */
    public static final Real MOLAR_GAS_CONSTANT = AVOGADRO_CONSTANT.multiply(BOLTZMANN_CONSTANT);

    /**
     * Stefan-Boltzmann constant: σ = 5.670374419×10^-8 W/(m²⋅K⁴).
     * <p>
     * <b>Uncertainty</b>: exact (derived from other exact constants)<br>
     * <b>Relation</b>: j = σT⁴ (black body radiation)
     * </p>
     */
    public static final Real STEFAN_BOLTZMANN_CONSTANT = Real.of(5.670374419e-8);

    // === Particle Masses ===

    /**
     * Electron mass: m_e = 9.1093837139×10^-31 kg.
     * <p>
     * <b>Relative uncertainty</b>: 3.0×10^-11<br>
     * <b>Note</b>: Most precisely known fundamental mass
     * </p>
     */
    public static final Real ELECTRON_MASS = Real.of(9.1093837139e-31);

    /**
     * Proton mass: m_p = 1.67262192595×10^-27 kg.
     * <p>
     * <b>Relative uncertainty</b>: 3.1×10^-11<br>
     * <b>Ratio</b>: m_p/m_e ≈ 1836.15
     * </p>
     */
    public static final Real PROTON_MASS = Real.of(1.67262192595e-27);

    /**
     * Neutron mass: m_n = 1.67492750056×10^-27 kg.
     * <p>
     * <b>Relative uncertainty</b>: 5.7×10^-11<br>
     * <b>Note</b>: Slightly heavier than proton (by ~0.14%)
     * </p>
     */
    public static final Real NEUTRON_MASS = Real.of(1.67492750056e-27);

    /**
     * Atomic mass unit (unified): u = 1.66053906892×10^-27 kg.
     * <p>
     * <b>Relative uncertainty</b>: 3.0×10^-11<br>
     * <b>Definition</b>: 1/12 of carbon-12 atom mass
     * </p>
     */
    public static final Real ATOMIC_MASS_UNIT = Real.of(1.66053906892e-27);

    // === Gravitational Constants ===

    /**
     * Newtonian gravitational constant: G = 6.67430×10^-11 m³/(kg⋅s²).
     * <p>
     * <b>Relative uncertainty</b>: 2.2×10^-5 (least precisely known fundamental
     * constant!)<br>
     * <b>Relation</b>: F = Gm₁m₂/r² (Newton's law of gravitation)
     * </p>
     */
    public static final Real GRAVITATIONAL_CONSTANT = Real.of(6.67430e-11);

    /**
     * Standard acceleration of gravity: g = 9.80665 m/s² (exact, by definition).
     * <p>
     * <b>Note</b>: Standard value at sea level, varies with location
     * </p>
     */
    public static final Real STANDARD_GRAVITY = Real.of(9.80665);

    // === Atomic and Nuclear Constants ===

    /**
     * Fine-structure constant: α = 7.2973525643×10^-3 (dimensionless).
     * <p>
     * <b>Relative uncertainty</b>: 1.5×10^-10<br>
     * <b>Value</b>: α ≈ 1/137.036<br>
     * <b>Relation</b>: α = e²/(4πε₀ℏc) - fundamental coupling constant
     * </p>
     */
    public static final Real FINE_STRUCTURE_CONSTANT = Real.of(7.2973525643e-3);

    /**
     * Rydberg constant: R_∞ = 10973731.568157 m^-1.
     * <p>
     * <b>Relative uncertainty</b>: 1.9×10^-12 (one of most precisely known
     * constants)<br>
     * <b>Relation</b>: Used in hydrogen spectrum calculations
     * </p>
     */
    public static final Real RYDBERG_CONSTANT = Real.of(10973731.568157);

    /**
     * Bohr radius: a_0 = 5.29177210544×10^-11 m.
     * <p>
     * <b>Relative uncertainty</b>: 1.5×10^-10<br>
     * <b>Definition</b>: Most probable distance of electron from nucleus in
     * hydrogen ground state
     * </p>
     */
    public static final Real BOHR_RADIUS = Real.of(5.29177210544e-11);

    // === Energy Conversion Factors ===

    /**
     * Electron volt: eV = 1.602176634×10^-19 J (exact).
     * <p>
     * <b>Definition</b>: Energy gained by electron accelerated through 1 volt
     * </p>
     */
    public static final Real ELECTRON_VOLT = ELEMENTARY_CHARGE;

    /**
     * Hartree energy: E_h = 4.3597447222060×10^-18 J.
     * <p>
     * <b>Relative uncertainty</b>: 1.5×10^-10<br>
     * <b>Definition</b>: Twice the ionization energy of hydrogen
     * </p>
     */
    public static final Real HARTREE_ENERGY = Real.of(4.3597447222060e-18);

    // === Magnetic Moments ===

    /**
     * Bohr magneton: μ_B = 9.2740100657×10^-24 J/T.
     * <p>
     * <b>Relative uncertainty</b>: 3.0×10^-10<br>
     * <b>Definition</b>: Magnetic moment of electron due to orbital angular
     * momentum
     * </p>
     */
    public static final Real BOHR_MAGNETON = Real.of(9.2740100657e-24);

    /**
     * Nuclear magneton: μ_N = 5.0507837393×10^-27 J/T.
     * <p>
     * <b>Relative uncertainty</b>: 3.1×10^-10<br>
     * <b>Definition</b>: Magnetic moment unit for nuclear physics
     * </p>
     */
    public static final Real NUCLEAR_MAGNETON = Real.of(5.0507837393e-27);

    // === Astronomical Constants ===

    /**
     * Astronomical unit: AU = 149597870700 m (exact, by definition since 2012).
     * <p>
     * <b>Note</b>: Mean Earth-Sun distance (historical definition, now exact)
     * </p>
     */
    public static final Real ASTRONOMICAL_UNIT = Real.of(149597870700.0);

    /**
     * Parsec: pc = 3.0856775814914×10^16 m.
     * <p>
     * <b>Definition</b>: Distance at which 1 AU subtends 1 arcsecond<br>
     * <b>Value</b>: pc = 648000/π AU
     * </p>
     */
    public static final Real PARSEC = Real.of(3.0856775814914e16);

    /**
     * Light year: ly = 9.46073047258×10^15 m.
     * <p>
     * <b>Definition</b>: Distance light travels in one Julian year (365.25 days)
     * </p>
     */
    public static final Real LIGHT_YEAR = Real.of(9.46073047258e15);
}
