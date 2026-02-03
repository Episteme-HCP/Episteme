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

package org.jscience.natural.computing.ai.expert;

import java.util.Collection;

/**
 * The inference engine is responsible for matching rules against facts (objects)
 * and executing them.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface InferenceEngine {

    /**
     * Adds a rule to the knowledge base.
     *
     * @param rule the rule to be added.
     */
    void addRule(Rule rule);
    
    /**
     * Adds a fact (data object) to the working memory.
     *
     * @param fact the object to be reasoned about.
     */
    void addFact(Object fact);
    
    /**
     * Removes a fact from the working memory.
     *
     * @param fact the object to remove.
     */
    void removeFact(Object fact);

    /**
     * Triggers the inference cycle.
     * Automatically matches rules against facts and executes them.
     */
    void fireRules();

    /**
     * Retrieves all facts currently in the working memory.
     *
     * @return a collection of facts.
     */
    Collection<Object> getFacts();
    
    /**
     * clears the working memory.
     */
    void clearFacts();
}
