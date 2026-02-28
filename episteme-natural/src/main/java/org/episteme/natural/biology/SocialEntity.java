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

package org.episteme.natural.biology;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.natural.engineering.eventdriven.EventDrivenEngine;
import org.episteme.natural.engineering.eventdriven.Event;
import org.episteme.natural.engineering.eventdriven.EventSpec;
import org.episteme.natural.engineering.eventdriven.SimulationAgent;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Base class for all social entities (individuals or collectives).
 * Implements traits common to social science modeling such as prestige and influence.
 */
@Persistent
public abstract class SocialEntity implements ComprehensiveIdentification, SimulationAgent {

    @Id
    protected final Identification id;
    
    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();
    
    protected double prestige = 0.0;
    protected double influence = 1.0;
    
    protected transient EventDrivenEngine engine;

    public SocialEntity(Identification id, EventDrivenEngine engine) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.engine = engine;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getSimId() {
        return id.toString();
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public double getPrestige() {
        return prestige;
    }

    public void setPrestige(double prestige) {
        this.prestige = prestige;
    }

    public double getInfluence() {
        return influence;
    }

    public void setInfluence(double influence) {
        this.influence = influence;
    }

    public EventDrivenEngine getEngine() {
        return engine;
    }

    public void setEngine(EventDrivenEngine engine) {
        this.engine = engine;
    }

    /**
     * Helper method to interact with another agent by sending an event.
     */
    public void interact(String targetId, EventSpec type, Object... data) {
        if (engine != null) {
            engine.sendEvent(targetId, type, data);
        }
    }

    @Override
    public abstract void processEvent(Event event);
}
