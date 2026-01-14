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
 Estimate Pi by approximating the area of a circle.

 How: generate N random numbers in the unit square, (0,0) to (1,1)
 and see how are within a radius of 1 or less, i.e.
 <pre>  

 sqrt(x^2 + y^2) < r

 </pre>
  since the radius is 1.0, we can square both sides
  and avoid a sqrt() computation:
  <pre>

    x^2 + y^2 <= 1.0

  </pre>
  this area under the curve is (Pi * r^2)/ 4.0,
  and the area of the unit of square is 1.0,
  so Pi can be approximated by 
  <pre>
		        # points with x^2+y^2 < 1
     Pi =~ 		--------------------------  * 4.0
		             total # points

  </pre>

*/

//code under public domain

public class MonteCarlo
{
	final static int SEED = 113;

	public static final double num_flops(int Num_samples)
	{
		// 3 flops in x^2+y^2 and 1 flop in random routine

		return ((double) Num_samples)* 4.0;

	}

	

	public static final double integrate(int Num_samples)
	{

		Random R = new Random(SEED);


		int under_curve = 0;
		for (int count=0; count<Num_samples; count++)
		{
			double x= R.nextDouble();
			double y= R.nextDouble();

			if ( x*x + y*y <= 1.0)
				 under_curve ++;
			
		}

		return ((double) under_curve / Num_samples) * 4.0;
	}


}
