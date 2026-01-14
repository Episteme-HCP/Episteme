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

package org.jscience.ml.openmath;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Vector;


/**
 * A JUnit test for an OpenMath binding.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMBindingTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMBindingTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMBindingTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testGetType() {
        OMBinding binding = new OMBinding();
        assertTrue(binding.getType().equals("OMBIND"));
    }

    /**
     * Test of getBinder method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testGetBinder() {
        OMBinding binding = new OMBinding();
        binding.setBinder(new OMSymbol("cd", "name"));
        assertTrue(binding.getBinder() != null);
    }

    /**
     * Test of setBinder method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testSetBinder() {
        OMBinding binding = new OMBinding();
        binding.setBinder(new OMSymbol("cd", "name"));
        assertTrue(binding.getBinder().getType().equals("OMS"));
    }

    /**
     * Test of getVariables method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testGetVariables() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("a"));
        assertTrue(binding.getVariables() != null);
    }

    /**
     * Test of setVariables method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testSetVariables() {
        OMBinding binding = new OMBinding();
        Vector variables = new Vector();
        variables.add(new OMVariable("a"));
        binding.setVariables(variables);
        assertTrue(((OMVariable) binding.getVariableAt(0)).getName().equals("a"));
    }

    /**
     * Test of getBody method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testGetBody() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        assertTrue(binding.getBody() != null);
    }

    /**
     * Test of setBody method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testSetBody() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        assertTrue(binding.getBody().getType().equals("OMI"));
    }

    /**
     * Test of getVariableAt method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testGetVariableAt() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        binding.addVariable(new OMVariable("c"));

        assertTrue(((OMVariable) binding.getVariableAt(1)).getName().equals("b"));
    }

    /**
     * Test of setVariableAt method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testSetVariableAt() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        binding.addVariable(new OMVariable("c"));
        binding.setVariableAt(new OMVariable("z"), 1);
        assertTrue(((OMVariable) binding.getVariableAt(1)).getName().equals("z"));
    }

    /**
     * Test of insertVariableAt method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testInsertVariableAt() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        binding.addVariable(new OMVariable("c"));
        binding.insertVariableAt(new OMVariable("z"), 1);
        assertTrue(((OMVariable) binding.getVariableAt(2)).getName().equals("b"));
    }

    /**
     * Test of removeVariableAt method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testRemoveVariableAt() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        binding.addVariable(new OMVariable("c"));
        binding.removeVariableAt(1);
        assertTrue(((OMVariable) binding.getVariableAt(1)).getName().equals("c"));
    }

    /**
     * Test of addVariable method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testAddVariable() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        assertTrue(((OMVariable) binding.getVariableAt(0)).getName().equals("a"));
    }

    /**
     * Test of removeVariable method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testRemoveVariable() {
        OMBinding binding = new OMBinding();
        OMVariable variable = new OMVariable("a");
        binding.setBody(new OMInteger("1"));
        binding.addVariable(variable);
        binding.removeVariable(variable);
        assertTrue(binding.getVariables().isEmpty());
    }

    /**
     * Test of removeAllVariables method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testRemoveAllVariables() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        binding.removeAllVariables();
        assertTrue(binding.getVariables().isEmpty());
    }

    /**
     * Test of firstVariable method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testFirstVariable() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        assertTrue(((OMVariable) binding.firstVariable()).getName().equals("a"));
    }

    /**
     * Test of lastVariable method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testLastVariable() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.addVariable(new OMVariable("b"));
        assertTrue(((OMVariable) binding.lastVariable()).getName().equals("b"));
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testToString() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.setBinder(new OMInteger("2"));
        assertTrue(binding.toString()
                          .equals("<OMBIND><OMI>2</OMI><OMBVAR><OMV name=\"a\"/></OMBVAR><OMI>1</OMI></OMBIND>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testClone() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.setBinder(new OMInteger("2"));

        OMBinding clone = (OMBinding) binding.clone();
        assertTrue((binding.getBody() == clone.getBody()) &&
            (binding.getBinder() == clone.getBinder()) &&
            (binding.getVariables() == clone.getVariables()));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testCopy() {
        OMBinding binding = new OMBinding();
        binding.setBody(new OMInteger("1"));
        binding.addVariable(new OMVariable("a"));
        binding.setBinder(new OMInteger("2"));

        OMBinding clone = (OMBinding) binding.copy();
        assertTrue((binding.getBody() != clone.getBody()) &&
            (binding.getBinder() != clone.getBinder()) &&
            (binding.getVariables() != clone.getVariables()));
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testIsComposite() {
        OMBinding binding = new OMBinding();
        assertTrue(binding.isComposite());
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testIsAtom() {
        OMBinding binding = new OMBinding();
        assertTrue(!binding.isAtom());
    }

    /**
     * Test of betaReduce method.<p></p>
     */
    public void testBetaReduce() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("x"));
        binding.setBinder(new OMSymbol("fns1", "lambda"));
        binding.setBody(new OMVariable("x"));

        OMObject reduction = binding.betaReduce(new OMVariable("x"),
                new OMInteger(12));
        assertTrue(reduction instanceof OMInteger);
    }

    /**
     * Test of betaReduce (version 2) method.<p></p>
     */
    public void testBetaReduce2() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("x"));
        binding.setBinder(new OMSymbol("fns1", "lambda"));

        OMBinding binding2 = new OMBinding();
        binding2.addVariable(new OMVariable("x"));
        binding2.setBinder(new OMSymbol("fns1", "lambda"));
        binding2.setBody(new OMVariable("x"));

        OMApplication application = new OMApplication();
        application.addElement(new OMSymbol("arith1", "times"));
        application.addElement(new OMVariable("x"));
        application.addElement(binding2);

        binding.setBody(application);

        OMApplication reduction = (OMApplication) binding.betaReduce(new OMVariable(
                    "x"), new OMInteger(12));

        assertTrue((reduction.getLength() == 3) &&
            reduction.getElementAt(2) instanceof OMBinding);
    }

    /**
     * Test of betaReduce (version 2) method.<p></p>
     */
    public void testBetaReduce3() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("x"));
        binding.setBinder(new OMSymbol("fns1", "lambda"));

        OMApplication application2 = new OMApplication();
        application2.addElement(new OMSymbol("arith1", "times"));
        application2.addElement(new OMVariable("x"));
        application2.addElement(new OMVariable("y"));

        OMBinding binding2 = new OMBinding();
        binding2.addVariable(new OMVariable("y"));
        binding2.setBinder(new OMSymbol("fns1", "lambda"));
        binding2.setBody(application2);

        OMApplication application = new OMApplication();
        application.addElement(new OMSymbol("arith1", "times"));
        application.addElement(new OMVariable("x"));
        application.addElement(binding2);

        binding.setBody(application);

        OMApplication reduction = (OMApplication) binding.betaReduce(new OMVariable(
                    "x"), new OMInteger(12));

        System.out.println(reduction);
        assertTrue((reduction.getLength() == 3) &&
            reduction.getElementAt(2) instanceof OMBinding &&
            (reduction.toString().indexOf("x") == -1));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testIsSame() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("x"));
        binding.setBinder(new OMSymbol("fns1", "lambda"));
        binding.setBody(new OMVariable("x"));

        OMBinding binding2 = new OMBinding();
        binding2.addVariable(new OMVariable("x"));
        binding2.setBinder(new OMSymbol("fns1", "lambda"));
        binding2.setBody(new OMVariable("x"));

        assertTrue(binding.isSame(binding2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMBinding.
     */
    public void testIsValid() {
        OMBinding binding = new OMBinding();
        binding.addVariable(new OMVariable("x"));
        binding.setBinder(new OMSymbol("fns1", "lambda"));
        binding.setBody(new OMVariable("x"));

        assertTrue(binding.isValid());
    }
}
