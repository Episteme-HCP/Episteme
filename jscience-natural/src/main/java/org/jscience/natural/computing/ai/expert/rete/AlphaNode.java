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
package org.jscience.natural.computing.ai.expert.rete;

import org.jscience.natural.computing.ai.expert.InferenceEngine;
import org.jscience.natural.computing.ai.expert.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 * Alpha Node in Rete Network.
 * Checks simple conditions (1-input nodes) and propagates to Beta nodes.
 */
public class AlphaNode {
    private final Rule rule;
    private final List<BetaNode> children = new ArrayList<>();

    public AlphaNode(Rule rule) {
        this.rule = rule;
    }

    public void addChild(BetaNode node) {
        children.add(node);
    }

    public void processFact(Object fact, InferenceEngine engine) {
        if (rule.matches(fact)) {
            // For simple rules without joins, we might execute directly
            // In a full Rete, Alpha nodes feed Beta nodes
            
            // Execute if it's a terminal rule (simplified for hybrid engine)
            rule.execute(engine);

            // Propagate to Beta children
            for (BetaNode child : children) {
                child.rightActivate(fact);
            }
        }
    }
    
    public Rule getRule() {
        return rule;
    }
}
