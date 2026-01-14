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

package org.jscience.util.mapper;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class dispatch data between an array and several domain objects.
 * <p/>
 * This class handles all the burden of mapping each domain object it
 * handles to a slice of a single array.
 *
 * @author L. Maisonobe
 * @version $Id: ArrayMapper.java,v 1.2 2007-10-21 17:48:13 virtualcall Exp $
 * @see ArraySliceMappable
 */

public class ArrayMapper {

    /**
     * Simple constructor.
     * Build an empty array mapper
     */
    public ArrayMapper() {
        domainObjects = new ArrayList();
        size = 0;
        internalData = null;
    }

    /**
     * Simple constructor.
     * Build an array mapper managing one object. Other objects can be
     * added later using the {@link #manageMappable manageMappable}
     * method. This call is equivalent to build the mapper with the
     * default constructor and adding the object.
     *
     * @param object domain object to handle
     */
    public ArrayMapper(ArraySliceMappable object) {

        domainObjects = new ArrayList();
        domainObjects.add(new ArrayMapperEntry(object, 0));

        size = object.getStateDimension();

        internalData = new double[size];

    }

    /**
     * Take a new domain object into account.
     *
     * @param object domain object to handle
     */
    public void manageMappable(ArraySliceMappable object) {

        domainObjects.add(new ArrayMapperEntry(object, size));

        size += object.getStateDimension();

        if (internalData != null) {
            internalData = new double[size];
        }

    }

    /**
     * Get the internal data array.
     *
     * @return internal data array
     */
    public double[] getInternalDataArray() {
        if (internalData == null) {
            internalData = new double[size];
        }
        return internalData;
    }

    /**
     * Map data from the internal array to the domain objects.
     */
    public void updateObjects() {
        if (internalData == null) {
            internalData = new double[size];
        }
        updateObjects(internalData);
    }

    /**
     * Map data from the specified array to the domain objects.
     *
     * @param data flat array holding the data to dispatch
     */
    public void updateObjects(double[] data) {
        for (Iterator iter = domainObjects.iterator(); iter.hasNext();) {
            ArrayMapperEntry entry = (ArrayMapperEntry) iter.next();
            entry.object.mapStateFromArray(entry.offset, data);
        }
    }

    /**
     * Map data from the domain objects to the internal array.
     */
    public void updateArray() {
        if (internalData == null) {
            internalData = new double[size];
        }
        updateArray(internalData);
    }

    /**
     * Map data from the domain objects to the specified array.
     *
     * @param data flat array where to put the data
     */
    public void updateArray(double[] data) {
        for (Iterator iter = domainObjects.iterator(); iter.hasNext();) {
            ArrayMapperEntry entry = (ArrayMapperEntry) iter.next();
            entry.object.mapStateToArray(entry.offset, data);
        }
    }

    /**
     * Container for all handled objects.
     */
    private ArrayList domainObjects;

    /**
     * Total number of scalar elements handled.
     * (size of the array)
     */
    private int size;

    /**
     * Flat array holding all data.
     * This is null as long as nobody uses it (lazy creation)
     */
    private double[] internalData;

}
