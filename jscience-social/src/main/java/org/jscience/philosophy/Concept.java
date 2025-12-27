/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.philosophy;

/**
 * Represents a philosophical concept or idea.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Concept {

    public enum Branch {
        METAPHYSICS, EPISTEMOLOGY, ETHICS, AESTHETICS, LOGIC,
        POLITICAL, PHILOSOPHY_OF_MIND, PHILOSOPHY_OF_SCIENCE
    }

    public enum Tradition {
        WESTERN, EASTERN, ANALYTIC, CONTINENTAL, PRAGMATIST,
        EXISTENTIALIST, PHENOMENOLOGICAL
    }

    private final String name;
    private final String description;
    private final Branch branch;
    private final Tradition tradition;
    private final String originatingPhilosopher;

    public Concept(String name, String description, Branch branch,
            Tradition tradition, String originatingPhilosopher) {
        this.name = name;
        this.description = description;
        this.branch = branch;
        this.tradition = tradition;
        this.originatingPhilosopher = originatingPhilosopher;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Branch getBranch() {
        return branch;
    }

    public Tradition getTradition() {
        return tradition;
    }

    public String getOriginatingPhilosopher() {
        return originatingPhilosopher;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", name, branch, tradition, originatingPhilosopher);
    }

    // Notable concepts
    public static final Concept CATEGORICAL_IMPERATIVE = new Concept("Categorical Imperative",
            "Act only according to maxims you could will to be universal law",
            Branch.ETHICS, Tradition.WESTERN, "Immanuel Kant");
    public static final Concept COGITO = new Concept("Cogito Ergo Sum",
            "I think, therefore I am",
            Branch.EPISTEMOLOGY, Tradition.WESTERN, "René Descartes");
}
