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
public class MappableScalarTest extends TestCase {
    /** DOCUMENT ME! */
    private MappableScalar scalar1;

    /** DOCUMENT ME! */
    private MappableScalar scalar2;

    /** DOCUMENT ME! */
    private MappableScalar scalar3;

    /** DOCUMENT ME! */
    private ArrayMapper mapper;

/**
     * Creates a new MappableScalarTest object.
     *
     * @param name DOCUMENT ME!
     */
    public MappableScalarTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        assertTrue(mapper.getInternalDataArray().length == 3);
    }

    /**
     * DOCUMENT ME!
     */
    public void testUpdateObjects() {
        double[] data = new double[mapper.getInternalDataArray().length];

        for (int i = 0; i < data.length; ++i) {
            data[i] = i * 0.1;
        }

        mapper.updateObjects(data);

        assertTrue(Math.abs(scalar1.getValue() - 0.0) < 1.0e-10);
        assertTrue(Math.abs(scalar2.getValue() - 0.1) < 1.0e-10);
        assertTrue(Math.abs(scalar3.getValue() - 0.2) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     */
    public void testUpdateArray() {
        scalar1.setValue(00.0);
        scalar2.setValue(10.0);
        scalar3.setValue(20.0);

        mapper.updateArray();

        double[] data = mapper.getInternalDataArray();

        for (int i = 0; i < data.length; ++i) {
            assertTrue(Math.abs(data[i] - (i * 10.0)) < 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(MappableScalarTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        scalar1 = new MappableScalar();
        scalar2 = new MappableScalar(2);
        scalar3 = new MappableScalar(-3);

        mapper = new ArrayMapper();
        mapper.manageMappable(scalar1);
        mapper.manageMappable(scalar2);
        mapper.manageMappable(scalar3);
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        scalar1 = null;
        scalar2 = null;
        scalar3 = null;

        mapper = null;
    }
}
