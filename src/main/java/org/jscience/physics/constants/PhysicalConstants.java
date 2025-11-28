package org.jscience.physics.constants;

import org.jscience.mathematics.number.Real;

/**
 * Fundamental physical constants (CODATA 2018 values).
 * <p>
 * All constants provided as {@link Real} numbers. SI units indicated in
 * documentation.
 * Values sourced from NIST CODATA 2018 recommended values.
 * </p>
 * 
 * @see <a href="https://physics.nist.gov/cuu/Constants/">NIST Physical
 *      Constants</a>
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class PhysicalConstants {

    private PhysicalConstants() {
    } // Prevent instantiation

    // ===================
    // UNIVERSAL
    // ===================

    /** Speed of light: c = 299,792,458 m/s (exact, defines meter) */
    public static final Real SPEED_OF_LIGHT = Real.of(299792458.0);

    /** Gravitational constant: G = 6.674 30 × 10⁻¹¹ m³/(kg·s²) */
    public static final Real GRAVITATIONAL_CONSTANT = Real.of(6.67430e-11);

    /** Planck constant: h = 6.626 070 15 × 10⁻³⁴ J·s (exact, defines kg) */
    public static final Real PLANCK_CONSTANT = Real.of(6.62607015e-34);

    /** Reduced Planck: ℏ = h/(2π) */
    public static final Real REDUCED_PLANCK_CONSTANT = PLANCK_CONSTANT.divide(Real.of(2 * Math.PI));

    // ===================
    // ELECTROMAGNETIC
    // ===================

    /** Elementary charge: e = 1.602 176 634 × 10⁻¹⁹ C (exact, defines A) */
    public static final Real ELEMENTARY_CHARGE = Real.of(1.602176634e-19);

    /** Vacuum permittivity: ε₀ = 8.854 187 8128 × 10⁻¹² F/m */
    public static final Real VACUUM_PERMITTIVITY = Real.of(8.8541878128e-12);

    /** Vacuum permeability: μ₀ = 1.256 637 062 12 × 10⁻⁶ N/A² */
    public static final Real VACUUM_PERMEABILITY = Real.of(1.25663706212e-6);

    /** Coulomb constant: k_e = 8.987 551 7923 × 10⁹ N·m²/C² */
    public static final Real COULOMB_CONSTANT = Real.of(8.9875517923e9);

    // ===================
    // ATOMIC & NUCLEAR
    // ===================

    /** Electron mass: m_e = 9.109 383 7015 × 10⁻³¹ kg */
    public static final Real ELECTRON_MASS = Real.of(9.1093837015e-31);

    /** Proton mass: m_p = 1.672 621 923 69 × 10⁻²⁷ kg */
    public static final Real PROTON_MASS = Real.of(1.67262192369e-27);

    /** Neutron mass: m_n = 1.674 927 498 04 × 10⁻²⁷ kg */
    public static final Real NEUTRON_MASS = Real.of(1.67492749804e-27);

    /** Atomic mass unit: u = 1.660 539 066 60 × 10⁻²⁷ kg */
    public static final Real ATOMIC_MASS_UNIT = Real.of(1.66053906660e-27);

    /** Fine-structure constant: α = 7.297 352 5693 × 10⁻³ */
    public static final Real FINE_STRUCTURE_CONSTANT = Real.of(7.2973525693e-3);

    /** Rydberg constant: R∞ = 10,973,731.568 160 m⁻¹ */
    public static final Real RYDBERG_CONSTANT = Real.of(10973731.568160);

    /** Bohr radius: a₀ = 5.291 772 109 03 × 10⁻¹¹ m */
    public static final Real BOHR_RADIUS = Real.of(5.29177210903e-11);

    // ===================
    // THERMODYNAMIC
    // ===================

    /** Boltzmann constant: k_B = 1.380 649 × 10⁻²³ J/K (exact, defines K) */
    public static final Real BOLTZMANN_CONSTANT = Real.of(1.380649e-23);

    /** Avogadro constant: N_A = 6.022 140 76 × 10²³ mol⁻¹ (exact, defines mole) */
    public static final Real AVOGADRO_CONSTANT = Real.of(6.02214076e23);

    /** Gas constant: R = 8.314 462 618 J/(mol·K) */
    public static final Real GAS_CONSTANT = Real.of(8.314462618);

    /** Stefan-Boltzmann: σ = 5.670 374 419 × 10⁻⁸ W/(m²·K⁴) */
    public static final Real STEFAN_BOLTZMANN_CONSTANT = Real.of(5.670374419e-8);

    // ===================
    // ASTRONOMICAL
    // ===================

    /** Astronomical unit: AU = 149,597,870,700 m (exact since 2012) */
    public static final Real ASTRONOMICAL_UNIT = Real.of(149597870700.0);

    /** Light year: ly = 9.460 730 472 5808 × 10¹⁵ m */
    public static final Real LIGHT_YEAR = Real.of(9.4607304725808e15);

    /** Parsec: pc = 3.085 677 581 × 10¹⁶ m */
    public static final Real PARSEC = Real.of(3.085677581e16);

    /** Solar mass: M☉ = 1.988 47 × 10³⁰ kg */
    public static final Real SOLAR_MASS = Real.of(1.98847e30);

    /** Earth mass: M⊕ = 5.972 168 × 10²⁴ kg */
    public static final Real EARTH_MASS = Real.of(5.972168e24);

    /** Earth radius (equatorial): R⊕ = 6.378 137 × 10⁶ m */
    public static final Real EARTH_RADIUS = Real.of(6.378137e6);

    /** Standard gravity: g = 9.806 65 m/s² */
    public static final Real STANDARD_GRAVITY = Real.of(9.80665);
}
