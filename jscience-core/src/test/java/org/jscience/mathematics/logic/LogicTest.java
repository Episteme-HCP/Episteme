/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.logic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.logic.multivalued.*;
import org.jscience.mathematics.logic.modal.PossibleWorlds;
import org.jscience.mathematics.logic.temporal.CTL;

public class LogicTest {

    @Test
    public void testLukasiewiczLogic() {
        LukasiewiczLogic logic = LukasiewiczLogic.getInstance();

        assertEquals(1.0, logic.trueValue());
        assertEquals(0.0, logic.falseValue());

        // Test conjunction
        assertEquals(0.5, logic.and(0.7, 0.8), 0.001);

        // Test implication
        assertEquals(1.0, logic.implies(0.5, 0.8), 0.001);
    }

    @Test
    public void testGodelLogic() {
        GodelLogic logic = GodelLogic.getInstance();

        assertEquals(1.0, logic.trueValue());
        assertEquals(0.0, logic.falseValue());

        // Test conjunction (min)
        assertEquals(0.5, logic.and(0.5, 0.8));

        // Test disjunction (max)
        assertEquals(0.8, logic.or(0.5, 0.8));
    }

    @Test
    public void testIntuitionisticLogic() {
        IntuitionisticLogic logic = IntuitionisticLogic.getInstance();

        assertTrue(logic.and(true, true));
        assertFalse(logic.and(true, false));
        assertTrue(logic.or(true, false));
    }

    @Test
    public void testParaconsistentLogic() {
        ParaconsistentLogic logic = ParaconsistentLogic.getInstance();

        assertEquals(ParaconsistentLogic.TruthValue.TRUE, logic.trueValue());
        assertEquals(ParaconsistentLogic.TruthValue.BOTH,
                logic.not(ParaconsistentLogic.TruthValue.BOTH));
    }

    @Test
    public void testLinearLogic() {
        LinearLogic.Proposition p = LinearLogic.linear("A");
        assertTrue(p.canUse());
        p.use();
        assertFalse(p.canUse());
    }

    @Test
    public void testPossibleWorlds() {
        PossibleWorlds<String, String> pw = new PossibleWorlds<>();
        pw.addWorld("w1");
        pw.addWorld("w2");
        pw.addAccessibility("w1", "w2");
        pw.setValue("w2", "p", true);

        assertTrue(pw.isPossible("w1", "p"));
    }

    @Test
    public void testCTL() {
        CTL<String, String> ctl = new CTL<>();
        ctl.addState("s1");
        ctl.addState("s2");
        ctl.addTransition("s1", "s2");
        ctl.label("s2", "p");

        assertTrue(ctl.EF(s -> ctl.holds(s, "p")).contains("s1"));
    }
}