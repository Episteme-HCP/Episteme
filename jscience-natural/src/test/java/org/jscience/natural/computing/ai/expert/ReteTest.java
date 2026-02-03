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

import org.jscience.natural.computing.ai.expert.rete.ReteEngine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification for Rete Engine.
 */
public class ReteTest {

    @Test
    public void testSimpleRuleFiring() {
        ReteEngine engine = new ReteEngine();
        boolean[] fired = {false};

        Rule rule = new Rule() {
            @Override
            public String getName() { return "TestRule"; }

            @Override
            public int getPriority() { return 0; }

            @Override
            public boolean matches(Object fact) {
                return fact instanceof String && ((String) fact).startsWith("Hello");
            }

            @Override
            public void execute(InferenceEngine eng) {
                fired[0] = true;
            }
        };

        engine.addRule(rule);
        engine.addFact("Hello World");

        assertTrue(fired[0], "Rule should have fired on matching fact");
    }

    @Test
    public void testFactRetraction() {
        ReteEngine engine = new ReteEngine();
        engine.addFact("Fact1");
        assertTrue(engine.getFacts().contains("Fact1"));
        
        engine.removeFact("Fact1");
        assertFalse(engine.getFacts().contains("Fact1"));
    }
}
