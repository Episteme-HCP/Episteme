package org.jscience.ml.sbml.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.jscience.ml.sbml.SBMLLevel2Document;
import org.jscience.ml.sbml.SBMLLevel2Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * A collection of tests to exercise the reference model for algebraic rules.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public class AlgebraicModelTests2 extends AlgebraicModelTests {

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
        return new TestSuite(AlgebraicModelTests2.class);
    }

    /**
     * Creates a new test for the reference model for algebraic rules.
     *
     * @param name Test name
     */

    public AlgebraicModelTests2(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        new SBMLLevel2Document(model).writeDocument(new BufferedWriter(new FileWriter("x-l2v1-algebraic.sbml")));
        model = SBMLLevel2Reader.read("x-l2v1-algebraic.sbml");
    }
}
