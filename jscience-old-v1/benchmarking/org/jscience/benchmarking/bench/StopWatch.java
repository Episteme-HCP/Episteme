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
   Provides a stopwatch to measure elapsed time.
<DL>
<DT><B>Example of use:</B></DT>
<DD>
<pre>
	Stopwatch Q = new Stopwatch;
	Q.start();
	//
	// code to be timed here ...
	//
	Q.stop();
	System.out.println("elapsed time was: " + Q.read() + " seconds.");
</pre>	

@author Roldan Pozo
@version 14 October 1997
*/

//code under public domain

public class StopWatch {
  private boolean running;
  private long last_time;
  private long total;

  public StopWatch() {
    reset(); }
		
  /** 
    * Return system time (in seconds)
    */
  public void reset() { 
    running = false; 
    last_time = 0; 
    total=0; }

  /** 
    * Resume timer.
    */
  public void resume() { 
    if (!running) { 
      last_time = System.currentTimeMillis(); 
      running = true; }}

  /** 
    * Start (and reset) timer
    */
  public void start() { 
    total=0;
    last_time = System.currentTimeMillis(); 
    running = true; }
   
  /** 
    * Stop timer
    */
  public double stop() { 
    if (running) {
      total += System.currentTimeMillis() - last_time; 
      running = false; }
    return total*0.001; }
 
  /** 
    * Return the elapsed time (in seconds)
    */
  public double read() {  
    if (running) {
      long now = System.currentTimeMillis();
      total += now - last_time;
      last_time = now; }
    return total*0.001; }
}

    

            
