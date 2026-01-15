/*
 *  $Id: OMAttributionTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
 *
 *  Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e)
 *  All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
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
