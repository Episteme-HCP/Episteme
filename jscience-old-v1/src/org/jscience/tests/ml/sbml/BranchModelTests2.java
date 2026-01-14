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
import org.jscience.ml.sbml.SBMLLevel2Document;
import org.jscience.ml.sbml.SBMLLevel2Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * A collection of tests to exercise the reference model for branching models.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public class BranchModelTests2 extends BranchModelTests {

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
        return new TestSuite(BranchModelTests2.class);
    }

    /**
     * Creates a new test for the reference model for branching models.
     *
     * @param name Test name
     */

    public BranchModelTests2(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        new SBMLLevel2Document(model).writeDocument(new BufferedWriter(new FileWriter("x-l2v1-branch.sbml")));
        model = SBMLLevel2Reader.read("x-l2v1-branch.sbml");
    }
}
