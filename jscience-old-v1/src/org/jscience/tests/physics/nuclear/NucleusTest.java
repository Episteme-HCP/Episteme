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

package org.jscience.physics.nuclear.kinematics.nuclear;

import junit.framework.TestCase;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;


/**
 * JUnit test of <code>Nucleus</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 */
public class NucleusTest extends TestCase {
/**
     * Constructor for NucleusTest.
     *
     * @param arg0
     */
    public NucleusTest(String arg0) {
        super(arg0);
    }

    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        UncertainNumber ex = new UncertainNumber(20.1, 1.0);

        try {
            Nucleus a1 = new Nucleus(2, 4);
            Nucleus a2 = new Nucleus(2, 4, 0.0);
            Nucleus a3 = new Nucleus(2, 4, ex.value);
            Nucleus a4 = new Nucleus(2, 4, ex);
            assertEquals(a1, a2);
            assertEquals(a2, a3);
            assertEquals(a3, a4);
            assertEquals(a3.Ex.value, a4.Ex.value, a3.Ex.error);
            a2 = Nucleus.parseNucleus("he4");
            assertEquals(a1, a2);
        } catch (NuclearException ne) {
            ne.printStackTrace();
        }
    }
}
