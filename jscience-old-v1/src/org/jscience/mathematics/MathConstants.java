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

package org.jscience.mathematics;

/**
 * A collection of useful numbers (stored to maximum precision).
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.1
 */

//also look at java.lang.Math for original PI and e values
//we can further add some more constants, see http://numbers.computation.free.fr/Constants/Miscellaneous/digits.html
//you may also want to give a chance to http://antwrp.gsfc.nasa.gov/htmltest/rjn_dig.html for e and some square roots
//and http://www.apropos-logic.com/nc/index.html for pi values and algorithms
//see http://numbers.computation.free.fr/Constants/constants.html for a general site
//for prime numbers, see http://www.prime-numbers.org/ and http://www.utm.edu/research/primes/
public final class MathConstants extends Object {
    /**
     * Square root of 2.
     */
    public final static double SQRT2 = 1.4142135623730950488016887242096980785696718753769;

    /**
     * Two times <img border=0 alt="pi" src="doc-files/pi.gif">.
     *
     * @planetmath Pi
     */
    public final static double TWO_PI = 6.2831853071795864769252867665590057683943387987502;

    /**
     * Square root of 2<img border=0 alt="pi" src="doc-files/pi.gif">.
     */
    public final static double SQRT2PI = 2.5066282746310005024157652848110452530069867406099;

    /**
     * Euler's gamma constant.
     *
     * @planetmath EulersConstant
     */
    public final static double GAMMA = 0.57721566490153286060651209008240243104215933593992;

    /**
     * Golden ratio.
     *
     * @planetmath GoldenRatio
     */
    public final static double GOLDEN_RATIO = 1.6180339887498948482045868343656381177203091798058;

    /**
     * Natural logarithm of 10.
     */
    public final static double LOG10 = 2.30258509299404568401799145468436420760110148862877;
}
