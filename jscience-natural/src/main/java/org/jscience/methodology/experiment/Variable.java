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

package org.jscience.methodology.experiment;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.Named;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a measurable or manipulable variable in a scientific experiment.
 * Suitable for psychology, clinical trials, and other experimental sciences.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Variable implements Named, Identified<Identification>, Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final String name;

    @Attribute
    private final String unit;

    public Variable(String name, String unit) {
        this.id = new org.jscience.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.name = Objects.requireNonNull(name, "Variable name cannot be null");
        this.unit = Objects.requireNonNull(unit, "Variable unit cannot be null");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name + " (" + unit + ")";
    }
}
