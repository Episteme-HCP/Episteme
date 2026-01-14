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

package org.jscience.measure.random;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//
// RngPack 1.1a by Paul Houle
// http://www.honeylocust.com/RngPack/
//

/**
 * RandomSynchronized is a wrapper class that makes a random number generator
 * safe for multi-threaded operation by serializing access in time.  For
 * high-performance applications,  it is better for each thread to have it's
 * own random number generator.
 * <p/>
 * Note this class is declared serializable,  but serialization won't be
 * successful if it's wrapping a non-serializable generator.
 * <p/>
 * <A HREF="/RngPack/src/edu/cornell/lassp/houle/RngPack/RandomSynchronized.java">
 * Source code </A> is available.
 *
 * @author <A HREF="http://www.honeylocust.com/"> Paul Houle </A> (E-mail: <A HREF="mailto:paul@honeylocust.com">paul@honeylocust.com</A>)
 * @version 1.1a
 * @see RandomElement
 */
public abstract class RandomSynchronized extends RandomElement
        implements Serializable {
    private RandomElement rng;

    /**
     * Creates a new RandomSynchronized object.
     *
     * @param rng DOCUMENT ME!
     */
    public RandomSynchronized(RandomElement rng) {
        this.rng = rng;
    }

    /**
     * Synchronized the raw() method,  which is generally not threadsafe.
     *
     * @return a random double in the range [0,1]
     * @see RandomJava
     */
    synchronized public double nextDouble() {
        return rng.nextDouble();
    }

    /**
     * This method probably isn't threadsafe in implementations,  so it's
     * synchronized
     *
     * @param d array to be filled with doubles
     * @param n number of doubles to generate
     */
    synchronized public void nextDouble(double[] d, int n) {
        rng.nextDouble(d, n);
    }

    /**
     * Wrapped so generators can override.
     *
     * @param lo lower limit of range
     * @param hi upper limit of range
     * @return a random integer in the range <STRONG>lo</STRONG>, <STRONG>lo</STRONG>+1, ... ,<STRONG>hi</STRONG>
     */
    synchronized public int choose(int lo, int hi) {
        return rng.choose(lo, hi);
    }

    /**
     * Must be synchronized because state is stored in BMoutput
     *
     * @return a random real with a gaussian distribution,  standard deviation
     */
    synchronized public double gaussian() {
        return rng.gaussian();
    }

    /**
     * We wouldn't want some sneaky person to serialize this in the middle
     * of generating a number
     */
    private synchronized void writeObject(final ObjectOutputStream out)
            throws IOException {
        // just so we're synchronized.
        out.defaultWriteObject();
    }

    private synchronized void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        // just so we're synchronized.
        in.defaultReadObject();
    }

}
