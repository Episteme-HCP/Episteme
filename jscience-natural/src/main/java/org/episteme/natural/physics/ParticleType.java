/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics;

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.HashMap;

import org.episteme.core.util.identity.Identified;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.Named;

/**
 * Standard Model particles and properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ParticleType implements Identified<Identification>, Named {

    /** Speed of light (m/s) */
    public static final Real C = PhysicalConstants.c;

    /** Electron volt in Joules */
    public static final Real EV = PhysicalConstants.eV;

    /** Fine structure constant */
    public static final Real ALPHA = PhysicalConstants.alpha;

    /** Weak mixing angle (sinÂ²Î¸_W) */
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

    @Override
    public Identification getId() {
        return new SimpleIdentification(symbol);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
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
            java.io.InputStream is = ParticleType.class.getResourceAsStream("/org/episteme/physics/particles.json");
            if (is == null) {
                java.util.logging.Logger.getLogger("StandardModel").severe("particles.json not found!");
                return;
            }

            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode node : root) {
                    String sym = node.get("symbol").asText();
                    String name = node.get("name").asText();
                    double mass = node.get("massMeV").asDouble();
                    double charge = node.get("charge").asDouble();
                    double spin = node.get("spin").asDouble();
                    String type = node.get("type").asText();

                    ParticleType p = new ParticleType(sym, name, mass, charge, spin, type);
                    PARTICLES.put(p.getSymbol(), p);
                    if (p.getName() != null) {
                        PARTICLES.put(p.getName().toUpperCase(), p);
                    }
                }
            }
            is.close();
        } catch (Exception e) {
            // Log instead of throw to allow partial initialization
            java.util.logging.Logger.getLogger("StandardModel").warning("Failed to load Standard Model particles: " + e.getMessage());
        }
    }

    public static void registerParticle(ParticleType particle) {
        if (particle != null && particle.getSymbol() != null) {
            PARTICLES.put(particle.getSymbol(), particle);
            if (particle.getName() != null) {
                PARTICLES.put(particle.getName().toUpperCase(), particle);
            }
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

    public static Real relativisticEnergy(Real momentum, Real massMeV) {
        return momentum.multiply(momentum).add(massMeV.multiply(massMeV)).sqrt();
    }

    public static Real lorentzFactor(Real velocity) {
        Real beta = velocity.divide(C);
        return Real.ONE.divide(Real.ONE.subtract(beta.multiply(beta)).sqrt());
    }

    public static Real relativisticMomentum(Real mass, Real velocity) {
        return lorentzFactor(velocity).multiply(mass).multiply(velocity);
    }

    public static Real deBroglieWavelength(Real momentum) {
        Real h = Real.of(6.62607015e-34);
        return h.divide(momentum);
    }

    public static Real comptonWavelength(Real mass) {
        Real h = Real.of(6.62607015e-34);
        return h.divide(mass.multiply(C));
    }

    public static Real breitWignerCrossSection(Real E, Real M, Real totalWidth,
            Real partialWidthIn, Real partialWidthOut,
            Real spin) {
        Real hbarc = Real.of(197.327); // MeVÂ·fm
        Real prefactor = spin.multiply(Real.of(2)).add(Real.ONE).multiply(Real.of(Math.PI))
                .multiply(hbarc.multiply(hbarc));
        Real numerator = partialWidthIn.multiply(partialWidthOut);
        Real denominator = E.subtract(M).pow(2).add(totalWidth.multiply(totalWidth).divide(Real.of(4)));
        return prefactor.multiply(numerator).divide(denominator);
    }

    public static Real runningAlpha(Real Q_MeV) {
        Real me = Real.of(0.511); // MeV
        Real lnQ = (Q_MeV.multiply(Q_MeV).divide(me.multiply(me))).log();
        return ALPHA
                .divide(Real.ONE.subtract(ALPHA.divide(Real.of(3).multiply(Real.of(Math.PI))).multiply(lnQ)));
    }
}

