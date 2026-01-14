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

package org.jscience.benchmarking.scimark;

/**

	Provides a stopwatch to measure elapsed time.

<P>
<DL>
<DT><B>Example of use:</B></DT>
<DD>
<p>
<pre>
	Stopwatch Q = new Stopwatch;
<p>
	Q.start();
	//
	// code to be timed here ...
	//
	Q.stop();
	System.out.println("elapsed time was: " + Q.read() + " seconds.");
</pre>	

@author Roldan Pozo
@version 14 October 1997, revised 1999-04-24
*/

//code under public domain

public class StopWatch
{
    private boolean running;
    private double last_time;
	private double total;


/** 
	Return system time (in seconds)

*/
	public final static double seconds()
	{
		return (System.currentTimeMillis() * 0.001);
	}
		
/** 
	Return system time (in seconds)

*/
	public void reset() 
	{ 
		running = false; 
		last_time = 0.0; 
		total=0.0; 
	}
	
    public StopWatch()
	{
		reset();
	}
    

/** 
	Start (and reset) timer

*/
  	public  void start() 
	{ 
		if (!running)
		{
			running = true;
			total = 0.0;
			last_time = seconds(); 
		}
	}
   
/** 
	Resume timing, after stopping.  (Does not wipe out
		accumulated times.)

*/
  	public  void resume() 
	{ 
		if (!running)
		{
			last_time = seconds(); 
			running = true;
		}
	}
   
   
/** 
	Stop timer

*/
   public  double stop()  
	{ 
		if (running) 
        {
			total += seconds() - last_time; 
            running = false;
        }
        return total; 
    }
  
 
/** 
	Display the elapsed time (in seconds)

*/
   public  double read()   
	{  
		if (running) 
       	{
			total += seconds() - last_time;
			last_time = seconds();
		}
        return total;
	}
		
}

    

            
