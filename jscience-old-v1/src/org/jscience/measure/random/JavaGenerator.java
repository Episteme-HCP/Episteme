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

//
// RngPack 1.1a by Paul Houle
// http://www.honeylocust.com/~houle/RngPack/
//

/**
 * RandomJava is a class wrapper for the <CODE>Math.random()</CODE>
 * generator that comes with Java.  I know nothing about the
 * quality of <CODE>Math.random()</CODE>, but I will warn the
 * reader that system-supplied
 * RNGs have a bad reputation;  <TT>RandomJava</TT> is <B>NOT</B>
 * reccomended for general use,  it has only been included as a
 * straightforward example of how to
 * build a <CODE>RandomElement</CODE> wrapper for an existing RNG.
 * The <TT>RANMAR</TT>, <TT>RANECU</TT> and <TT>RANLUX</TT>
 * generators included in this package all appear to be faster than
 * Math.random();  all three are well-studied,  portable and
 * proven in use.
 * <p/>
 * <p/>
 * <A HREF="/RngPack/src/edu/cornell/lassp/houle/RngPack/RandomJava.java">
 * Source code </A> is available.
 *
 * @author <A HREF="http://www.honeylocust.com/~houle/RngPack/"> Paul Houle </A> (E-mail: <A HREF="mailto:paul@honeylocust.com">paul@honeylocust.com</A>)
 * @version 1.1a
 * @see Ranmar
 * @see Ranlux
 * @see Ranecu
 */
public class JavaGenerator extends RandomElement {

    /**
     * Wrapper for <CODE>Math.random().</CODE>
     *
     * @see RandomElement#nextDouble
     */
    public double nextDouble() {
        return Math.random();
    }

}

