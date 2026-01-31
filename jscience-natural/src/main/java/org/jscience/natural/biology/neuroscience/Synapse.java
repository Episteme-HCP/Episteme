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

package org.jscience.natural.biology.neuroscience;

import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Synapse connecting two neurons.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Synapse implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String synapseType;

    @Attribute
    private double weight;

    @Attribute
    private double delay;

    private SpikingNeuron postSynaptic;

    /**
     * Creates a synapse with a specific type (for bridge use).
     */
    public Synapse(String synapseType) {
        this.id = new SimpleIdentification("SYN:" + UUID.randomUUID());
        this.synapseType = synapseType;
        setName(synapseType);
    }

    /**
     * Creates a synapse between spiking neurons.
     */
    public Synapse(SpikingNeuron pre, SpikingNeuron post, double weight, double delay) {
        this.id = new SimpleIdentification("SYN:" + UUID.randomUUID());
        this.postSynaptic = post;
        this.weight = weight;
        this.delay = delay;
        setName("synapse");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Propagates spike from pre to post.
     */
    public void transmit() {
        if (postSynaptic != null) {
            postSynaptic.addInputCurrent(org.jscience.core.mathematics.numbers.real.Real.of(weight));
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public String getSynapseType() {
        return synapseType;
    }

    public void setSynapseType(String synapseType) {
        this.synapseType = synapseType;
    }
}

