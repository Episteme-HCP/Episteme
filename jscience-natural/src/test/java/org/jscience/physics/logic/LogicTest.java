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
package org.jscience.physics.logic;

import org.jscience.computing.logic.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Logic Circuits.
 */
public class LogicTest {

    @Test
    public void testLogicStateOperations() {
        assertEquals(LogicState.HIGH, LogicState.LOW.not());
        assertEquals(LogicState.LOW, LogicState.HIGH.not());
        assertEquals(LogicState.UNKNOWN, LogicState.UNKNOWN.not());

        assertEquals(LogicState.HIGH, LogicState.HIGH.and(LogicState.HIGH));
        assertEquals(LogicState.LOW, LogicState.HIGH.and(LogicState.LOW));
        assertEquals(LogicState.LOW, LogicState.LOW.and(LogicState.UNKNOWN));
        assertEquals(LogicState.UNKNOWN, LogicState.HIGH.and(LogicState.UNKNOWN));

        assertEquals(LogicState.HIGH, LogicState.HIGH.or(LogicState.LOW));
        assertEquals(LogicState.LOW, LogicState.LOW.or(LogicState.LOW));
        assertEquals(LogicState.HIGH, LogicState.LOW.or(LogicState.HIGH));
        assertEquals(LogicState.UNKNOWN, LogicState.LOW.or(LogicState.UNKNOWN));
    }

    @Test
    public void testAndGate() {
        AndGate gate = new AndGate();
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.HIGH, LogicState.HIGH));
        assertEquals(LogicState.LOW, gate.evaluate(LogicState.HIGH, LogicState.LOW));
        assertEquals(LogicState.LOW, gate.evaluate(LogicState.LOW, LogicState.LOW));
        assertEquals(LogicState.LOW, gate.evaluate(LogicState.LOW, LogicState.HIGH, LogicState.HIGH));
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.HIGH, LogicState.HIGH, LogicState.HIGH));
    }

    @Test
    public void testOrGate() {
        OrGate gate = new OrGate();
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.HIGH, LogicState.LOW));
        assertEquals(LogicState.LOW, gate.evaluate(LogicState.LOW, LogicState.LOW));
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.HIGH, LogicState.HIGH));
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.LOW, LogicState.LOW, LogicState.HIGH));
    }

    @Test
    public void testNotGate() {
        NotGate gate = new NotGate();
        assertEquals(LogicState.LOW, gate.evaluate(LogicState.HIGH));
        assertEquals(LogicState.HIGH, gate.evaluate(LogicState.LOW));

        assertThrows(IllegalArgumentException.class, () -> gate.evaluate(LogicState.HIGH, LogicState.LOW));
    }

    @Test
    public void testCompositeLogic() {
        // NAND = AND + NOT
        AndGate andGate = new AndGate();
        NotGate notGate = new NotGate();

        LogicState input1 = LogicState.HIGH;
        LogicState input2 = LogicState.HIGH;

        LogicState andResult = andGate.evaluate(input1, input2);
        LogicState nandResult = notGate.evaluate(andResult);

        assertEquals(LogicState.LOW, nandResult); // NAND(1,1) = 0
    }

    @Test
    public void testFuzzyLogic() {
        org.jscience.mathematics.logic.fuzzy.FuzzySet<Double> setA = new org.jscience.mathematics.logic.fuzzy.FuzzySet<>(
                x -> x / 10.0);

        assertEquals(0.5, setA.membership(5.0), 1e-9);
        assertEquals(0.5, setA.complement().membership(5.0), 1e-9);

        org.jscience.mathematics.logic.fuzzy.FuzzySet<Double> setB = new org.jscience.mathematics.logic.fuzzy.FuzzySet<>(
                x -> 1.0);

        assertEquals(0.5, setA.intersection(setB).membership(5.0), 1e-9);
        assertEquals(1.0, setA.union(setB).membership(5.0), 1e-9);
    }

    @Test
    public void testModalLogic() {
        org.jscience.mathematics.logic.modal.KripkeStructure<String> kripke = new org.jscience.mathematics.logic.modal.KripkeStructure<>();

        kripke.addWorld("w1");
        kripke.addWorld("w2");
        kripke.addRelation("w1", "w2");
        kripke.setTrue("w2", "p");

        assertTrue(kripke.isTrue("w2", "p"));
        assertFalse(kripke.isTrue("w1", "p"));
        assertTrue(kripke.getAccessibleWorlds("w1").contains("w2"));
    }
}
// Final touch for IDE re-indexing