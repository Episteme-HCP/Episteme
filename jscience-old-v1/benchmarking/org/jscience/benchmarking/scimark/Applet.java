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

/*****************************************************************************
 Main benchmark driver
 *****************************************************************************/

//code under public domain

public class Applet implements org.jscience.benchmarking.bench.Target
{

  public double[] execute(org.jscience.benchmarking.bench.Bench b)
  {
		double res[] = new double[6];
		Random R = new Random(Constants.RANDOM_SEED);
		double resolution_time =  Constants.RESOLUTION_DEFAULT;


		// preload benchmark components to minimize network download
		// time.  (These sizes are not use for benchmarking,)

		double TINY_RES_TIME = 0.00001;	
		b.noteStatus("Downloading FFT");
		res[1] = Kernel.measureFFT( Constants.TINY_FFT_SIZE, TINY_RES_TIME, R);
		Thread.yield();

		b.noteStatus("Downloading SOR");
		res[2] = Kernel.measureSOR( Constants.TINY_SOR_SIZE, TINY_RES_TIME, R);
		Thread.yield();

		b.noteStatus("Downloading Monte Carlo");
		res[3] = Kernel.measureMonteCarlo(TINY_RES_TIME, R);
		Thread.yield();


		b.noteStatus("Downloading Sparse Matrix Multplication");
		res[4] = Kernel.measureSparseMatmult( Constants.TINY_SPARSE_SIZE_M,
						Constants.TINY_SPARSE_SIZE_nz, 
						TINY_RES_TIME, R);
		Thread.yield();

		b.noteStatus("Downloading LU factorization");
		res[5] = Kernel.measureLU( Constants.TINY_LU_SIZE, TINY_RES_TIME, R);
		Thread.yield();


		// now begin benchmarking

		b.noteStatus("benchmarking FFT");
		res[1] = Kernel.measureFFT( Constants.FFT_SIZE, resolution_time, R);

		b.noteStatus("benchmarking SOR");
		res[2] = Kernel.measureSOR( Constants.SOR_SIZE, resolution_time, R);

		b.noteStatus("benchmarking Monte Carlo");
		res[3] = Kernel.measureMonteCarlo(resolution_time, R);


		b.noteStatus("benchmarking Sparse Matrix Multplication");
		res[4] = Kernel.measureSparseMatmult( Constants.SPARSE_SIZE_M,
						Constants.SPARSE_SIZE_nz, 
						resolution_time, R);

		b.noteStatus("benchmarking LU factorization");
		res[5] = Kernel.measureLU( Constants.LU_SIZE, resolution_time, R);

		res[0] = (res[1] + res[2] + res[3] + res[4] + res[5]) / 5;


		
		// if either the FFT or LU did not validate, then we have
		// an invalid JVM (i.e. one with an incorrect floating point
		// model).  By returning 0.0, this avoids posting artificically
		// high SciMark scores for systems that may be cheating.
		//
		if (res[1] == 0.0 || res[5] == 0.0)
		{
			res[0] = res[1] = res[2] = res[3] = res[4] = res[5] = 0.0;
		}

		return res;
	}
}

