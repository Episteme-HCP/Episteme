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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Beta Node in Rete Network.
 * Joins tokens from two parent nodes (typically an Alpha node and another Beta node).
 */
public class BetaNode {
    private final List<Map<String, Object>> memory = new ArrayList<>();
    private BetaNode leftParent;
    private AlphaNode rightParent;

    public BetaNode() {}

    public void setLeftParent(BetaNode leftParent) {
        this.leftParent = leftParent;
    }

    public void setRightParent(AlphaNode rightParent) {
        this.rightParent = rightParent;
    }

    public void leftActivate(Map<String, Object> token) {
        // Simple join logic (cartesian product for now, needs condition matching)
        // In full implementation, we'd check join conditions here
        memory.add(token);
        propagate(token);
    }

    public void rightActivate(Object fact) {
        // In full implementation, we'd match fact against left memory
    }

    private void propagate(Map<String, Object> token) {
        // Send to children
    }
}
