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

import java.util.Properties;

/**
	SciMark2: A Java numerical benchmark measuring performance
	of computational kernels for FFTs, Monte Carlo simulation,
	sparse matrix computations, Jacobi SOR, and dense LU matrix
	factorizations.  


*/

//code under public domain

public class CommandLine
{

  /* Benchmark 5 kernels with individual Mflops.
	 "results[0]" has the average Mflop rate.

  */


	public static void main(String args[])
	{
		// default to the (small) cache-contained version

		double min_time = Constants.RESOLUTION_DEFAULT;

		int FFT_size = Constants.FFT_SIZE;
		int SOR_size =  Constants.SOR_SIZE;
		int Sparse_size_M = Constants.SPARSE_SIZE_M;
		int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
		int LU_size = Constants.LU_SIZE;

		// look for runtime options

        if (args.length > 0)
        {

			if (args[0].equalsIgnoreCase("-h") || 
						args[0].equalsIgnoreCase("-help"))
			{
				System.out.println("Usage: [-large] [minimum_time]");
				return;
			}

			int current_arg = 0;
			if (args[current_arg].equalsIgnoreCase("-large"))
			{
				FFT_size = Constants.LG_FFT_SIZE;
				SOR_size =  Constants.LG_SOR_SIZE;
				Sparse_size_M = Constants.LG_SPARSE_SIZE_M;
				Sparse_size_nz = Constants.LG_SPARSE_SIZE_nz;
				LU_size = Constants.LG_LU_SIZE;

				current_arg++;
			}

			if (args.length > current_arg)
        		min_time = Double.valueOf(args[current_arg]).doubleValue();
        }
        

		// run the benchmark

		double res[] = new double[6];
		Random R = new Random(Constants.RANDOM_SEED);

		res[1] = Kernel.measureFFT( FFT_size, min_time, R);
		res[2] = Kernel.measureSOR( SOR_size, min_time, R);
		res[3] = Kernel.measureMonteCarlo(min_time, R);
		res[4] = Kernel.measureSparseMatmult( Sparse_size_M,
					Sparse_size_nz, min_time, R);
		res[5] = Kernel.measureLU( LU_size, min_time, R);


		res[0] = (res[1] + res[2] + res[3] + res[4] + res[5]) / 5;


	    // print out results

		System.out.println();
		System.out.println("SciMark 2.0a");
		System.out.println();
		System.out.println("Composite Score: " + res[0]);
		System.out.print("FFT ("+FFT_size+"): ");
		if (res[1]==0.0)
			System.out.println(" ERROR, INVALID NUMERICAL RESULT!");
		else
			System.out.println(res[1]);

		System.out.println("SOR ("+SOR_size+"x"+ SOR_size+"): "
				+ "  " + res[2]);
		System.out.println("Monte Carlo : " + res[3]);
		System.out.println("Sparse matmult (N="+ Sparse_size_M+ 
				", nz=" + Sparse_size_nz + "): " + res[4]);
		System.out.print("LU (" + LU_size + "x" + LU_size + "): ");
		if (res[5]==0.0)
			System.out.println(" ERROR, INVALID NUMERICAL RESULT!");
		else
			System.out.println(res[5]);

		// print out System info
		System.out.println();
		System.out.println("java.vendor: " + 
				System.getProperty("java.vendor"));
		System.out.println("java.version: " + 
				System.getProperty("java.version"));
		System.out.println("os.arch: " +
				System.getProperty("os.arch"));
		System.out.println("os.name: " +
				System.getProperty("os.name"));
		System.out.println("os.version: " +
				System.getProperty("os.version"));


	}
  
}
