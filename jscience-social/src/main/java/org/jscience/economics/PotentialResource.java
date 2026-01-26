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
package org.jscience.economics;

import org.jscience.measure.Quantity;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a potential or virtual resource, such as oil in the soil 
 * that is expected to be found, or ingredients required in a recipe or task.
 * 
 * <p>Resources can be people, equipment, facilities, funding, or anything else 
 * capable of definition (usually other than labour) required for the completion 
 * of a project activity.</p>
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.3
 */
@Persistent
public class PotentialResource implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String description;

    @Attribute
    private Quantity<?> amount;

    @Attribute
    private double decayTime;

    @Attribute
    private int kind;

    /**
     * Creates a new PotentialResource object.
     *
     * @param name        the name of the resource, not null.
     * @param description the description, not null.
     * @param amount      the quantity, not null.
     */
    public PotentialResource(String name, String description, Quantity<?> amount) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, description, amount);
    }

    /**
     * Creates a new PotentialResource with a specific identification.
     */
    public PotentialResource(Identification id, String name, String description, Quantity<?> amount) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.amount = Objects.requireNonNull(amount, "Quantity cannot be null");
        
        this.decayTime = -1;
        this.kind = EconomicsConstants.UNKNOWN;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public Quantity<?> getAmount() {
        return amount;
    }

    public void setQuantity(Quantity<?> amount) {
        this.amount = Objects.requireNonNull(amount, "Quantity cannot be null");
    }

    public double getDecayTime() {
        return decayTime;
    }

    public void setDecayTime(double decayTime) {
        this.decayTime = decayTime;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PotentialResource)) return false;
        PotentialResource that = (PotentialResource) o;
        return Objects.equals(id, that.id);
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
