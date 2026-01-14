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

package org.jscience.ml.sbml.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.jscience.ml.sbml.SBMLLevel2Reader;

import java.io.InputStreamReader;

/**
 * A collection of tests to exercise the reference model for delay equations.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public class DelayModelTests extends SBMLModelTests {

    /**
     * Starts a new test suite run.
     *
     * @param args Program arguments
     */

    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * All of the tests in this test.
     */

    public static Test suite() {
        return new TestSuite(DelayModelTests.class);
    }

    /**
     * Creates a new test for the reference model for delay equations.
     *
     * @param name Test name
     */

    public DelayModelTests(String name) {
        super(name);
    }

    /**
     * A test for compartments.
     */

    public void testCompartment0() throws Exception {
        assertTrue(model.getCompartments().size() == 1);
    }

    /**
     * A test for compartments.
     */

    public void testCompartment1() throws Exception {
        testCompartment("cell", true, null, 1.0, 3, null);
    }

    /**
     * A test for events.
     */

    public void testEvent0() throws Exception {
        assertTrue(model.getEvents().size() == 0);
    }

    /**
     * A test for function definitions.
     */

    public void testFunction0() throws Exception {
        assertTrue(model.getFunctionDefinitions().size() == 0);
    }

    /**
     * A test for model.
     */

    public void testModel1() throws Exception {
        assertTrue(model.getId() == null);
    }

    /**
     * A test for parameters.
     */

    public void testParameter0() throws Exception {
        assertTrue(model.getParameters().size() == 4);
    }

    /**
     * A test for parameters.
     */

    public void testParameter1() throws Exception {
        testParameter("tau", 1.0);
    }

    /**
     * A test for parameters.
     */

    public void testParameter2() throws Exception {
        testParameter("m", 0.5);
    }

    /**
     * A test for parameters.
     */

    public void testParameter3() throws Exception {
        testParameter("q", 1.0);
    }

    /**
     * A test for parameters.
     */

    public void testParameter4() throws Exception {
        testParameter("delta_t", 1.0);
    }

    /**
     * A test for reactions.
     */

    public void testReaction0() throws Exception {
        assertTrue(model.getReactions().size() == 0);
    }

    /**
     * A test for rules.
     */

    public void testRule0() throws Exception {
        assertTrue(model.getRules().size() == 1);
    }

    /**
     * A test for species.
     */

    public void testSpecies0() throws Exception {
        assertTrue(model.getSpecies().size() == 1);
    }

    /**
     * A test for species.
     */

    public void testSpecies1() throws Exception {
        testSpecies("P", "cell", 0.0, false, false);
    }

    /**
     * A test for units.
     */

    public void testUnit0() throws Exception {
        assertTrue(model.getUnitDefinitions().size() == 0);
    }

    protected void setUp() throws Exception {
        model = SBMLLevel2Reader.read(new InputStreamReader(DelayModelTests.class.getResourceAsStream("l2v1-delay.sbml")));
    }
}
