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

package org.jscience.medicine;

import org.jscience.biology.taxonomy.Species;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an infectious target-based disease.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Disease extends Pathology {

    private static final long serialVersionUID = 1L;

    public enum Transmission {
        UNKNOWN, GENETIC, WATER, FOOD, AIR, CONTACT, PARASITIC
    }

    public enum Origin {
        UNKNOWN, VIRUS, BACTERIA, PRION, GENETIC, FUNGAL
    }

    @Attribute
    private Transmission transmission = Transmission.UNKNOWN;

    @Attribute
    private Origin origin = Origin.UNKNOWN;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Species> vectors = new HashSet<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Species> targets = new HashSet<>();

    @Attribute
    private String microorganismId; // ID or name of the biological agent

    @Attribute
    private String icdCode;

    public Disease(String name) {
        super(name);
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Set<Species> getVectors() {
        return Collections.unmodifiableSet(vectors);
    }

    public void addVector(Species species) {
        vectors.add(Objects.requireNonNull(species));
    }

    public void removeVector(Species species) {
        vectors.remove(species);
    }

    public Set<Species> getTargets() {
        return Collections.unmodifiableSet(targets);
    }

    public void addTarget(Species species) {
        targets.add(Objects.requireNonNull(species));
    }

    public void removeTarget(Species species) {
        targets.remove(species);
    }

    public String getMicroorganismId() {
        return microorganismId;
    }

    public void setMicroorganismId(String microorganismId) {
        this.microorganismId = microorganismId;
    }
}
