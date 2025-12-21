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
package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReactionBalancerTest {

    @Test
    public void testComplexBalancing() {
        // Calcium hydroxide + Phosphoric acid -> Calcium phosphate + Water
        // 3Ca(OH)2 + 2H3PO4 -> Ca3(PO4)2 + 6H2O
        String eq = "Ca(OH)2 + H3PO4 -> Ca3(PO4)2 + H2O";
        ChemicalReaction balanced = ReactionBalancer.balance(eq);

        System.out.println("Balanced: " + balanced);

        assertEquals(3, balanced.getReactants().get("Ca(OH)2"));
        assertEquals(2, balanced.getReactants().get("H3PO4"));
        assertEquals(1, balanced.getProducts().get("Ca3(PO4)2"));
        assertEquals(6, balanced.getProducts().get("H2O"));
    }
}
