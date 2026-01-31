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

package org.jscience.natural.chemistry.loaders.cml.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * a 'cyclic vector' that is  element[0] succeeds element[nElem-1] where
 * nElem is size. Provides elements(startIndex) as Enumeration which iterates
 * through whole of Vector cyclicly. [Normal elements() is inherited from
 * Vector and starts from element[0]]. This class is not thread-safe. The
 * vector and its contents can be changed but Enumerations will be
 * invalidated.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CyclicVector extends Vector<Object> {
/**
     * create with an empty vector
     */
    public CyclicVector() {
        this(new Vector<>());
    }

    /**
     * Creates a new CyclicVector from an existing vector.
     *
     * @param vector the vector to copy elements from
     */
    public CyclicVector(Vector<?> vector) {
        for (Enumeration<?> e = vector.elements(); e.hasMoreElements();) {
            this.addElement(e.nextElement());
        }
    }

    /**
     * Creates an Enumeration starting with the first element that contains startObject.
     *
     * @param startObject the object to start the iteration from
     *
     * @return a cyclic enumeration of the vector's elements
     *
     * @throws IllegalArgumentException if the startObject is not found in the vector
     */
    public Enumeration<Object> elements(Object startObject)
        throws IllegalArgumentException {
        int index = this.indexOf(startObject);

        if (index == -1) {
            throw new IllegalArgumentException("No such element: " +
                startObject.toString());
        }

        return new CyclicVectorEnumeration(this, index);
    }

    /**
     * Creates an Enumeration starting at the specified index.
     *
     * @param startIndex the index to start the iteration from
     *
     * @return a cyclic enumeration of the vector's elements
     *
     * @throws IllegalArgumentException if the startIndex is out of bounds
     */
    public Enumeration<Object> elements(int startIndex) throws IllegalArgumentException {
        return new CyclicVectorEnumeration(this, startIndex);
    }

    /**
     * Test main method for CyclicVector.
     *
     * @param args command line arguments: size and start index
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: CyclicVector size start");
            System.exit(0);
        }

        int size = Integer.parseInt(args[0]);
        int start = Integer.parseInt(args[1]);
        CyclicVector cyclicVector = new CyclicVector();

        for (int i = 0; i < size; i++) {
            // add an element and iterate through vector. If element is not yet
            // present we will get an exception
            cyclicVector.addElement("" + i);

            try {
                for (Enumeration<Object> e = cyclicVector.elements("" + start);
                        e.hasMoreElements();) {
                    System.out.println("obj>" + e.nextElement());
                }

                System.out.println("------------------");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        while (cyclicVector.size() > 0) {
            // remove elements from vector
            cyclicVector.removeElementAt(0);

            try {
                for (Enumeration<Object> e = cyclicVector.elements("" + start);
                        e.hasMoreElements();) {
                    System.out.println("obj>" + e.nextElement());
                }

                System.out.println("------------------");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}


/**
 * Helper class to provide cyclic enumeration over a Vector.
 */
class CyclicVectorEnumeration implements Enumeration<Object> {
    /** The vector to iterate over. */
    Vector<Object> vector;

    /** The index where the iteration started. */
    int startIndex;

    /** The current index in the iteration. */
    int currentIndex;

    /** Whether the full cycle has been completed. */
    boolean finished;

    /**
     * Creates a new CyclicVectorEnumeration object.
     *
     * @param vector     the vector to iterate over
     * @param startIndex the index to start from
     * @throws IllegalArgumentException if the startIndex is out of bounds
     */
    public CyclicVectorEnumeration(Vector<Object> vector, int startIndex)
        throws IllegalArgumentException {
        this.vector = vector;

        if ((startIndex < 0) || (startIndex > vector.size())) {
            throw new IllegalArgumentException("No such index: " + startIndex);
        }

        this.startIndex = startIndex;
        currentIndex = startIndex;
        finished = false;
    }

    /**
     * Returns true if the iteration has more elements.
     *
     * @return true if there are more elements
     */
    public boolean hasMoreElements() {
        return !finished;
    }

    /**
     * Returns the next element in the cyclic iteration.
     *
     * @return the next object
     *
     * @throws NoSuchElementException if the iteration is finished or the vector is empty
     */
    public Object nextElement() throws NoSuchElementException {
        if (finished) {
            throw new NoSuchElementException("finished");
        }

        if (vector.size() <= 0) {
            throw new NoSuchElementException("Empty vector");
        }

        Object object = vector.elementAt(currentIndex);
        currentIndex = (++currentIndex) % vector.size();

        if (!finished) {
            finished = (currentIndex == startIndex);
        }

        return object;
    }
}

