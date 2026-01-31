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

package org.jscience.natural.methodology.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.core.util.Named;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.Identified;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * An abstract representation of a specific task within an experimental study.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class Task implements Named, Identified<Identification>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final String name;

    @Attribute
    private final String description;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Trial> trials;

    private Subject subject;

    protected Task(String name, String description, List<Trial> trials) {
        this.id = new org.jscience.core.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.name = Objects.requireNonNull(name, "Task name cannot be null");
        this.description = Objects.requireNonNull(description, "Task description cannot be null");
        this.trials = new ArrayList<>(Objects.requireNonNull(trials, "Trials list cannot be null"));
        
        for (Trial trial : this.trials) {
            trial.setTask(this);
        }
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Trial> getTrials() {
        return Collections.unmodifiableList(trials);
    }

    public Subject getSubject() {
        return subject;
    }

    protected void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Executes the task.
     */
    public abstract void doTask();
}

