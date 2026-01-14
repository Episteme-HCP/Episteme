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

package org.jscience.benchmarking.bench;

/** 
Interface for a Benchmark Target.
Code to be measured by the Bench framework should provide a class implementing
this interface.  Place the code to be measured in the execute method.

@author Bruce R. Miller (bruce.miller@nist.gov)
@author Contribution of the National Institute of Standards and Technology,
@author not subject to copyright.
*/

//code under public domain

public interface Target {
  /** The code to be measured is placed in this method.
    * @return null lets org.jscience.benchmarking.bench.Bench handle the timings.
    * Otherwise, return an array containing the one or more measured values.
    * @see org.jscience.benchmarking.bench.Bench [start|stop|reset]Timer methods for measurement tools.
    */    
  public double[] execute(Bench bench) throws Exception;
}
