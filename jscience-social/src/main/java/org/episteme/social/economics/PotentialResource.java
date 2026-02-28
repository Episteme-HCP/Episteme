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

package org.episteme.social.economics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a potential or virtual resource, such as oil in the soil 
 * that is expected to be found, or ingredients required in a recipe or task.
 * 
 * <p>Resources can be people, equipment, facilities, funding, or anything else 
 * capable of definition required for the completion of a project activity.</p>
 * Implements ComprehensiveIdentification and modernized to use Real for continuous factors.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class PotentialResource implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String description;

    @Attribute
    private Quantity<?> amount;
 
    @Attribute
    private Quantity<Time> decayTime;

    @Attribute
    private ResourceKind kind;

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
        
        // Default to no decay (marker value)
        this.decayTime = Quantities.create(Real.of(-1), Units.SECOND);
        this.kind = ResourceKind.UNKNOWN;
    }

    /**
     * Creates a new PotentialResource with a specific kind.
     */
    public PotentialResource(String name, String description, Quantity<?> amount, ResourceKind kind) {
        this(new UUIDIdentification(UUID.randomUUID().toString()), name, description, amount);
        this.kind = Objects.requireNonNull(kind, "Kind cannot be null");
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

    public Quantity<Time> getDecayTime() {
        return decayTime;
    }
 
    public void setDecayTime(Quantity<Time> decayTime) {
        this.decayTime = decayTime;
    }
    
    // Convenience for double input (default to seconds)
    public void setDecaySeconds(double decayTime) {
        this.decayTime = org.episteme.core.measure.Quantities.create(decayTime, Units.SECOND);
    }

    public ResourceKind getKind() {
        return kind;
    }

    public void setKind(ResourceKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    @Deprecated
    public int getLegacyKind() {
        return kind != null ? kind.ordinal() : -1;
    }

    @Deprecated
    public void setLegacyKind(int legacyKind) {
        this.kind = ResourceKind.valueOf(legacyKind);
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

