package org.jscience.physics;

import org.jscience.physics.PhysicalConstants;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.HashMap;

/**
 * Standard Model particles and properties.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ParticleType {

    /** Speed of light (m/s) */
    public static final Real C = PhysicalConstants.c;

    /** Electron volt in Joules */
    public static final Real EV = PhysicalConstants.eV;

    /** Fine structure constant */
    public static final Real ALPHA = PhysicalConstants.alpha;

    /** Weak mixing angle (sin²θ_W) */
    public static final Real SIN2_THETA_W = Real.of(0.23122);

    private static final Map<String, ParticleType> PARTICLES = new HashMap<>();

    static {
        loadParticles();
    }

    private String symbol;
    private String name;
    private Real massMeV;
    private Real charge;
    private Real spin;
    private String type;

    // Default constructor for Jackson
    public ParticleType() {
    }

    public ParticleType(String symbol, String name, double massMeV, double charge, double spin, String type) {
        this.symbol = symbol;
        this.name = name;
        this.massMeV = Real.of(massMeV);
        this.charge = Real.of(charge);
        this.spin = Real.of(spin);
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Real getMassMeV() {
        return massMeV;
    }

    public Real getCharge() {
        return charge;
    }

    public Real getSpin() {
        return spin;
    }

    public String getType() {
        return type;
    }

    public Real getMassKg() {
        return massMeV.multiply(Real.of(1e6)).multiply(EV).divide(C.multiply(C));
    }

    public boolean isFermion() {
        return spin.equals(Real.of(0.5));
    }

    public boolean isBoson() {
        return spin.isZero() || spin.equals(Real.ONE);
    }

    private static void loadParticles() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.io.InputStream is = ParticleType.class.getResourceAsStream("/org/jscience/physics/particles.json");
            if (is == null) {
                java.util.logging.Logger.getLogger("StandardModel").severe("particles.json not found!");
                return;
            }

            // We need a custom deserializer or a DTO because JSON has doubles
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode node : root) {
                    // Manual extraction to cleaner types
                    String sym = node.get("symbol").asText();
                    String name = node.get("name").asText();
                    double mass = node.get("massMeV").asDouble();
                    double charge = node.get("charge").asDouble();
                    double spin = node.get("spin").asDouble();
                    String type = node.get("type").asText();

                    ParticleType p = new ParticleType(sym, name, mass, charge, spin, type);
                    PARTICLES.put(p.getSymbol(), p);
                    PARTICLES.put(p.getName().toUpperCase(), p);
                }
            }
            is.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Standard Model particles", e);
        }
    }

    public static java.util.Optional<ParticleType> getParticle(String symbolOrName) {
        if (symbolOrName == null)
            return java.util.Optional.empty();
        if (PARTICLES.containsKey(symbolOrName))
            return java.util.Optional.of(PARTICLES.get(symbolOrName));
        return java.util.Optional.ofNullable(PARTICLES.get(symbolOrName.toUpperCase()));
    }

    public static java.util.Collection<ParticleType> getAllParticles() {
        return new java.util.HashSet<>(PARTICLES.values());
    }

    /**
     * Relativistic energy-momentum relation.
     * E² = (pc)² + (mc²)²
     */
    public static Real relativisticEnergy(Real momentum, Real massMeV) {
        Real pc = momentum; // in MeV/c
        Real mc2 = massMeV;
        return pc.multiply(pc).add(mc2.multiply(mc2)).sqrt();
    }

    /**
     * Lorentz factor.
     * γ = 1 / √(1 - v²/c²)
     */
    public static Real lorentzFactor(Real velocity) {
        Real beta = velocity.divide(C);
        return Real.ONE.divide(Real.ONE.subtract(beta.multiply(beta)).sqrt());
    }

    /**
     * Relativistic momentum.
     * p = γmv
     */
    public static Real relativisticMomentum(Real mass, Real velocity) {
        return lorentzFactor(velocity).multiply(mass).multiply(velocity);
    }

    /**
     * De Broglie wavelength.
     * λ = h / p
     */
    public static Real deBroglieWavelength(Real momentum) {
        Real h = Real.of(6.62607015e-34);
        return h.divide(momentum);
    }

    /**
     * Compton wavelength.
     * λ_C = h / (mc)
     */
    public static Real comptonWavelength(Real mass) {
        Real h = Real.of(6.62607015e-34);
        return h.divide(mass.multiply(C));
    }

    /**
     * Cross-section from decay width (Breit-Wigner).
     * σ = (2J+1) * π * (ℏc/p)² * Γ_in * Γ_out / ((E-M)² + Γ²/4)
     */
    public static Real breitWignerCrossSection(Real E, Real M, Real totalWidth,
            Real partialWidthIn, Real partialWidthOut,
            Real spin) {
        Real hbarc = Real.of(197.327); // MeV·fm
        Real prefactor = spin.multiply(Real.of(2)).add(Real.ONE).multiply(PhysicalConstants.PI)
                .multiply(hbarc.multiply(hbarc));
        Real numerator = partialWidthIn.multiply(partialWidthOut);
        Real denominator = E.subtract(M).pow(2).add(totalWidth.multiply(totalWidth).divide(Real.of(4)));
        return prefactor.multiply(numerator).divide(denominator);
    }

    /**
     * QED running coupling constant at energy Q.
     * α(Q²) ≈ α / (1 - (α/3π) * ln(Q²/m_e²))
     */
    public static Real runningAlpha(Real Q_MeV) {
        Real me = Real.of(0.511); // MeV
        Real lnQ = (Q_MeV.multiply(Q_MeV).divide(me.multiply(me))).log();
        return ALPHA
                .divide(Real.ONE.subtract(ALPHA.divide(Real.of(3).multiply(PhysicalConstants.PI)).multiply(lnQ)));
    }
}
