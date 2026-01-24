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

import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a medical diagnostic result or finding.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Diagnostic implements Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final String id;

    @Attribute
    private final String name;

    @Attribute
    private String description;

    @Attribute
    private String icdCode; // Often ICD-10 or ICD-11

    @Attribute
    private final List<String> symptoms = new ArrayList<>();

    @Attribute
    private final List<String> recommendedTests = new ArrayList<>();

    public Diagnostic(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name);
    }

    public Diagnostic(String name, String icdCode) {
        this(name);
        this.icdCode = icdCode;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public List<String> getSymptoms() {
        return Collections.unmodifiableList(symptoms);
    }

    public void addSymptom(String symptom) {
        this.symptoms.add(symptom);
    }

    public List<String> getRecommendedTests() {
        return Collections.unmodifiableList(recommendedTests);
    }

    public void addRecommendedTest(String test) {
        this.recommendedTests.add(test);
    }

    @Override
    public String toString() {
        return icdCode != null ? String.format("%s (%s)", name, icdCode) : name;
    }
}
