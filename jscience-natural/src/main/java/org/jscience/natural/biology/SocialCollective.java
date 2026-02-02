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

package org.jscience.natural.biology;

import org.jscience.natural.engineering.eventdriven.EventDrivenEngine;
import org.jscience.core.util.identity.Identification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a collective of biological or social agents (individuals or subgroups).
 * @param <T> The type of individuals in the collective.
 */
public abstract class SocialCollective<T extends Individual> extends SocialEntity {
    
    protected final List<T> members = new ArrayList<>();
    protected T leader;
    
    /**
     * Social cohesion or biological group integration level.
     */
    protected double cohesion = 1.0;

    public SocialCollective(Identification id, EventDrivenEngine engine) {
        super(id, engine);
    }

    public List<T> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public void addMember(T individual) {
        if (individual != null && !members.contains(individual)) {
            members.add(individual);
        }
    }

    public void removeMember(T individual) {
        members.remove(individual);
        if (leader == individual) {
            leader = null;
        }
    }

    public T getLeader() {
        return leader;
    }

    public void setLeader(T leader) {
        this.leader = leader;
        addMember(leader);
    }

    public double getCohesion() {
        return cohesion;
    }

    public void setCohesion(double cohesion) {
        this.cohesion = Math.max(0.0, Math.min(1.0, cohesion));
    }
    
    public int size() {
        return members.size();
    }
}
