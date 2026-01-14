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

package org.jscience.tests.mathematics.analysis.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ArrayMapperTest extends TestCase {
    /** DOCUMENT ME! */
    private DomainObject b1;

    /** DOCUMENT ME! */
    private DomainObject b2;

    /** DOCUMENT ME! */
    private DomainObject b3;

    /** DOCUMENT ME! */
    private ArrayMapper mapper;

/**
     * Creates a new ArrayMapperTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ArrayMapperTest(String name) {
        super(name);
        mapper = null;
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        int size = b1.getStateDimension();
        size += b2.getStateDimension();
        size += b3.getStateDimension();
        assertTrue(mapper.getInternalDataArray().length == size);
    }

    /**
     * DOCUMENT ME!
     */
    public void testUpdateObjects() {
        double[] data = new double[7];

        for (int i = 0; i < 7; ++i) {
            data[i] = i * 0.1;
        }

        mapper.updateObjects(data);

        assertTrue(Math.abs(b1.getElement(0) - 0.0) < 1.0e-10);

        assertTrue(Math.abs(b2.getElement(0) - 0.4) < 1.0e-10);
        assertTrue(Math.abs(b2.getElement(1) - 0.3) < 1.0e-10);
        assertTrue(Math.abs(b2.getElement(2) - 0.2) < 1.0e-10);
        assertTrue(Math.abs(b2.getElement(3) - 0.1) < 1.0e-10);

        assertTrue(Math.abs(b3.getElement(0) - 0.6) < 1.0e-10);
        assertTrue(Math.abs(b3.getElement(1) - 0.5) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     */
    public void testUpdateArray() {
        b1.setElement(0, 0.0);

        b2.setElement(0, 40.0);
        b2.setElement(1, 30.0);
        b2.setElement(2, 20.0);
        b2.setElement(3, 10.0);

        b3.setElement(0, 60.0);
        b3.setElement(1, 50.0);

        mapper.updateArray();

        double[] data = mapper.getInternalDataArray();

        for (int i = 0; i < 7; ++i) {
            assertTrue(Math.abs(data[i] - (i * 10.0)) < 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        b1 = new DomainObject(1);
        b2 = new DomainObject(4);
        b3 = new DomainObject(2);

        mapper = new ArrayMapper();
        mapper.manageMappable(b1);
        mapper.manageMappable(b2);
        mapper.manageMappable(b3);
    }

    /**
     * DOCUMENT ME!
     */
    public void tearOff() {
        b1 = null;
        b2 = null;
        b3 = null;

        mapper = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(ArrayMapperTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DomainObject implements ArraySliceMappable {
        /** DOCUMENT ME! */
        private double[] data;

/**
         * Creates a new DomainObject object.
         *
         * @param size DOCUMENT ME!
         */
        public DomainObject(int size) {
            data = new double[size];
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getStateDimension() {
            return data.length;
        }

        /**
         * DOCUMENT ME!
         *
         * @param start DOCUMENT ME!
         * @param array DOCUMENT ME!
         */
        public void mapStateFromArray(int start, double[] array) {
            for (int i = 0; i < data.length; ++i) {
                data[data.length - 1 - i] = array[start + i];
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param start DOCUMENT ME!
         * @param array DOCUMENT ME!
         */
        public void mapStateToArray(int start, double[] array) {
            for (int i = 0; i < data.length; ++i) {
                array[start + i] = data[data.length - 1 - i];
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getElement(int i) {
            return data[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        public void setElement(int i, double value) {
            data[i] = value;
        }
    }
}
