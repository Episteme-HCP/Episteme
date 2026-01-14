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

//code under public domain

public class SparseCompRow
{
	/* multiple iterations used to make kernel have roughly
		same granulairty as other Scimark kernels. */

	public static double num_flops(int N, int nz, int num_iterations)
	{
		/* Note that if nz does not divide N evenly, then the
		   actual number of nonzeros used is adjusted slightly.
		*/
		int actual_nz = (nz/N) * N;
		return ((double)actual_nz) * 2.0 * ((double) num_iterations);
	}


	/* computes  a matrix-vector multiply with a sparse matrix
		held in compress-row format.  If the size of the matrix
		in MxN with nz nonzeros, then the val[] is the nz nonzeros,
		with its ith entry in column col[i].  The integer vector row[]
		is of size M+1 and row[i] points to the begining of the
		ith row in col[].  
	*/

	public static void matmult( double y[], double val[], int row[],
		int col[], double x[], int NUM_ITERATIONS)
	{
		int M = row.length - 1;

		for (int reps=0; reps<NUM_ITERATIONS; reps++)
		{
		
			for (int r=0; r<M; r++)
			{
				double sum = 0.0; 
				int rowR = row[r];
				int rowRp1 = row[r+1];
				for (int i=rowR; i<rowRp1; i++)
					sum += x[ col[i] ] * val[i];
				y[r] = sum;
			}
		}
	}

}
