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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a medical diagnostic.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Diagnostic {

    private final String name;
    private String description;
    private String icd10Code; // International Classification of Diseases
    private final List<String> symptoms = new ArrayList<>();
    private final List<String> recommendedTests = new ArrayList<>();

    public Diagnostic(String name) {
        this.name = name;
    }

    public Diagnostic(String name, String icd10Code) {
        this.name = name;
        this.icd10Code = icd10Code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public void addSymptom(String symptom) {
        this.symptoms.add(symptom);
    }

    public List<String> getSymptoms() {
        return Collections.unmodifiableList(symptoms);
    }

    public void addRecommendedTest(String test) {
        this.recommendedTests.add(test);
    }

    public List<String> getRecommendedTests() {
        return Collections.unmodifiableList(recommendedTests);
    }

    @Override
    public String toString() {
        return name + (icd10Code != null ? " (" + icd10Code + ")" : "");
    }
}
