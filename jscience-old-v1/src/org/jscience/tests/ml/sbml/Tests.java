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
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Test harness for all SBML parser tests.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public abstract class Tests extends TestCase {

    /**
     * Starts a new test suite run.
     *
     * @param args Program arguments
     */

    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * A suite containing all test suites for the SBML parser.
     */

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(CompartmentModelTests.suite());
        suite.addTest(CompartmentModelTests2.suite());
        suite.addTest(AlgebraicModelTests.suite());
        suite.addTest(AlgebraicModelTests2.suite());
        suite.addTest(AssignmentModelTests.suite());
        suite.addTest(AssignmentModelTests2.suite());
        suite.addTest(BoundaryModelTests.suite());
        suite.addTest(BoundaryModelTests2.suite());
        suite.addTest(BranchModelTests.suite());
        suite.addTest(BranchModelTests2.suite());
        suite.addTest(DelayModelTests.suite());
        suite.addTest(DelayModelTests2.suite());
        suite.addTest(EventModelTests.suite());
        suite.addTest(EventModelTests2.suite());
        suite.addTest(FunctionModelTests.suite());
        suite.addTest(FunctionModelTests2.suite());
        suite.addTest(ODEModelTests.suite());
        suite.addTest(ODEModelTests2.suite());
        suite.addTest(UnitModelTests.suite());
        suite.addTest(UnitModelTests2.suite());
        return suite;
    }

    /**
     * Tests should never be instantiated.
     */

    private Tests() {
    }
}
