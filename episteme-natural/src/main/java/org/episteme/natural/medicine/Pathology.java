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

package org.episteme.natural.medicine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Common ancestor for all medicine-related troubles (diseases, allergies, etc.).
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Pathology implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Medication> medications = new HashSet<>();

    /**
     * Creates a new Pathology with a specific name and random ID.
     *
     * @param name the scientific name
     * @throws NullPointerException if name is null
     */
    public Pathology(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID().toString());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    /**
     * Internal constructor for subclasses specifying ID.
     */
    protected Pathology(Identification id, String name) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(name);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getDescription() {
        return getComments();
    }

    public void setDescription(String description) {
        setComments(description);
    }

    public String getCause() {
        return (String) getTrait("cause");
    }

    public void setCause(String cause) {
        setTrait("cause", cause);
    }

    /**
     * Returns the set of medications associated with this pathology.
     *
     * @return an unmodifiable set of medications
     */
    public Set<Medication> getMedications() {
        return Collections.unmodifiableSet(medications);
    }

    public void addMedication(Medication medication) {
        medications.add(Objects.requireNonNull(medication));
    }

    public void removeMedication(Medication medication) {
        medications.remove(medication);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pathology pathology)) return false;
        return Objects.equals(id, pathology.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}

