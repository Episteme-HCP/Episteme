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

package org.jscience.core.methodology;

import java.time.Instant;
import java.util.Optional;

import org.jscience.core.measure.Quantity;
 
import org.jscience.core.util.Positioned;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a physical or conceptual sample used in a scientific experiment.
 * <p>
 * A sample is the object of study, which can be material (e.g., a rock, a tissue),
 * biological (e.g., an individual), or even social.
 * </p>
 * 
 * @param <P> The type of the position (e.g., GeoCoordinates, Vector3D, String default).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Sample<P> implements ComprehensiveIdentification, Positioned<P> {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String name;

    @Attribute
    private String description;

    @Attribute
    private Instant samplingDate;

    @Attribute
    private String category; // e.g., "Rock", "Biological Tissue", "Individual"

    @Attribute
    private Quantity<?> amount; // Mass, Volume, Count, etc.

    @Attribute
    private String source; // Origin of the sample

    @Attribute
    private P position;

    @Attribute
    private String thermalProperties;

    /**
     * Creates a new Sample.
     * @param name The name of the sample.
     */
    public Sample(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(name);
        this.samplingDate = Instant.now();
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(Instant samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Optional<Quantity<?>> getAmount() {
        return Optional.ofNullable(amount);
    }

    public void setAmount(Quantity<?> amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public P getPosition() {
        return position;
    }

    public void setPosition(P position) {
        this.position = position;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public String toString() {
        return String.format("Sample{name='%s', category='%s', date=%s}", name, category, samplingDate);
    }
}

