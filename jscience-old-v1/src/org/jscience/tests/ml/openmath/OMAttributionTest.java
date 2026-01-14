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

import java.util.Hashtable;


/**
 * A JUnit test for OMAttribution.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMAttributionTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMAttributionTest(String testName) {
        super(testName);
    }

    /**
     * Test suite method.<p></p>
     *
     * @return a test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMAttributionTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGetType() {
        OMAttribution attribution = new OMAttribution();
        assertTrue(attribution.getType().equals("OMATTR"));
    }

    /**
     * Test of getAttributions method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGetAttributions() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);
        assertTrue(attribution.getAttributions() != null);
    }

    /**
     * Test of setAttributions method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testSetAttributions() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);
        assertTrue(attribution.getAttributions() != null);
    }

    /**
     * Test of getConstructor method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGetConstructor() {
        OMAttribution attribution = new OMAttribution();
        OMInteger integer = new OMInteger("1");
        attribution.setConstructor(integer);

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);

        assertTrue(attribution.getConstructor() == integer);
    }

    /**
     * Test of setConstructor method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testSetConstructor() {
        OMAttribution attribution = new OMAttribution();
        OMInteger integer = new OMInteger("1");
        attribution.setConstructor(integer);

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);

        assertTrue(attribution.getConstructor() == integer);
    }

    /**
     * Test of put method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testPut() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));
        attribution.put(new OMSymbol("a", "a"), new OMInteger("1"));
        assertTrue(attribution.getAttributions().size() == 1);
    }

    /**
     * Test of remove method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testRemove() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        attribution.put(symbol, new OMInteger("1"));
        attribution.remove(symbol);
        assertTrue(attribution.getAttributions().size() == 0);
    }

    /**
     * Test of get method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGet() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        OMInteger integer = new OMInteger("1");
        attribution.put(symbol, integer);
        assertTrue(attribution.get(symbol) == integer);
    }

    /**
     * Test of hasKey method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testHasKey() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        OMInteger integer = new OMInteger("1");
        attribution.put(symbol, integer);
        assertTrue(attribution.hasKey(symbol));
    }

    /**
     * Test of hasValue method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testHasValue() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        OMInteger integer = new OMInteger("1");
        attribution.put(symbol, integer);
        assertTrue(attribution.hasValue(integer));
    }

    /**
     * Test of getKeys method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGetKeys() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        OMInteger integer = new OMInteger("1");
        attribution.put(symbol, integer);
        assertTrue(attribution.getKeys().nextElement() == symbol);
    }

    /**
     * Test of getValues method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testGetValues() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        OMSymbol symbol = new OMSymbol("a", "a");
        OMInteger integer = new OMInteger("1");
        attribution.put(symbol, integer);
        assertTrue(attribution.getValues().nextElement() == integer);
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testToString() {
        OMAttribution attribution = new OMAttribution();
        OMInteger integer = new OMInteger("1");
        attribution.setConstructor(integer);

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);

        assertTrue(attribution.toString()
                              .equals("<OMATTR><OMATP><OMS cd=\"a\" name=\"a\"/><OMI>1</OMI></OMATP><OMI>1</OMI></OMATTR>"));
    }

    /**
     * Test of flatten method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testFlatten() {
        OMAttribution attribution1 = new OMAttribution();
        OMAttribution attribution2 = new OMAttribution();
        Hashtable hashtable = new Hashtable();
        OMSymbol symbol = new OMSymbol("a", "a");
        hashtable.put(symbol, new OMInteger("1"));
        attribution2.setConstructor(new OMInteger("2"));
        attribution2.setAttributions(hashtable);
        attribution1.setConstructor(attribution2);
        attribution1.setAttributions(new Hashtable());

        OMAttribution attribution = attribution1.flatten();
        assertTrue(attribution.hasKey(symbol));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testClone() {
        OMAttribution attribution = new OMAttribution();
        OMInteger integer = new OMInteger("1");
        attribution.setConstructor(integer);

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);

        OMAttribution copy = (OMAttribution) attribution.clone();
        assertTrue(copy.getConstructor() == integer);
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testCopy() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMInteger("1"));

        Hashtable hashtable = new Hashtable();
        hashtable.put(new OMSymbol("a", "a"), new OMInteger("1"));
        attribution.setAttributions(hashtable);

        OMAttribution copy = (OMAttribution) attribution.copy();
        assertTrue(copy.getConstructor() instanceof OMInteger);
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testIsComposite() {
        OMAttribution attribution = new OMAttribution();
        assertTrue(attribution.isComposite() == true);
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testIsAtom() {
        OMAttribution attribution = new OMAttribution();
        assertTrue(attribution.isAtom() == false);
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testIsSame() {
        OMAttribution attribution1 = new OMAttribution();
        attribution1.setConstructor(new OMSymbol("a", "b"));
        attribution1.put(new OMSymbol("a", "b"), new OMInteger("1"));

        OMAttribution attribution2 = new OMAttribution();
        attribution2.setConstructor(new OMSymbol("a", "b"));
        attribution2.put(new OMSymbol("a", "b"), new OMInteger("1"));

        assertTrue(attribution1.isSame(attribution2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMAttribution.
     */
    public void testIsValid() {
        OMAttribution attribution = new OMAttribution();
        attribution.setConstructor(new OMSymbol("a", "b"));
        attribution.put(new OMSymbol("a", "b"), new OMInteger("1"));

        assertTrue(attribution.isValid());
    }
}
