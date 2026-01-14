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

public class Jacobi
{
	public static final double num_flops(int M, int N, int num_iterations)
	{
		double Md = (double) M;
		double Nd = (double) N;
		double num_iterD = (double) num_iterations;

		return (Md-1)*(Nd-1)*num_iterD*6.0;
	}

	public static final void SOR(double omega, double G[][], int num_iterations)
	{
		int M = G.length;
		int N = G[0].length;

		double omega_over_four = omega * 0.25;
		double one_minus_omega = 1.0 - omega;

		// update interior points
		//
		int Mm1 = M-1;
		int Nm1 = N-1; 
		for (int p=0; p<num_iterations; p++)
		{
			for (int i=1; i<Mm1; i++)
			{
				double[] Gi = G[i];
				double[] Gim1 = G[i-1];
				double[] Gip1 = G[i+1];
				for (int j=1; j<Nm1; j++)
					Gi[j] = omega_over_four * (Gim1[j] + Gip1[j] + Gi[j-1] 
								+ Gi[j+1]) + one_minus_omega * Gi[j];
			}
		}
	}
}
			
