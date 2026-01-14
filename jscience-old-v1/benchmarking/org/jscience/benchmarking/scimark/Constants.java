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

public class Constants
{

	public static final double RESOLUTION_DEFAULT = 2.0;  /*secs*/
	public static final int RANDOM_SEED = 101010;

	// default: small (cache-contained) problem sizes
	//
	public static final int FFT_SIZE = 1024;  // must be a power of two
	public static final int SOR_SIZE =100; // NxN grid
	public static final int SPARSE_SIZE_M = 1000;
	public static final int SPARSE_SIZE_nz = 5000;
	public static final int LU_SIZE = 100;

	// large (out-of-cache) problem sizes
	//
	public static final int LG_FFT_SIZE = 1048576;  // must be a power of two
	public static final int LG_SOR_SIZE =1000; // NxN grid
	public static final int LG_SPARSE_SIZE_M = 100000;
	public static final int LG_SPARSE_SIZE_nz =1000000;
	public static final int LG_LU_SIZE = 1000;

	// tiny problem sizes (used to mainly to preload network classes 
	//                     for applet, so that network download times
	//                     are factored out of benchmark.)
	//
	public static final int TINY_FFT_SIZE = 16;  // must be a power of two
	public static final int TINY_SOR_SIZE =10; // NxN grid
	public static final int TINY_SPARSE_SIZE_M = 10;
	public static final int TINY_SPARSE_SIZE_N = 10;
	public static final int TINY_SPARSE_SIZE_nz = 50;
	public static final int TINY_LU_SIZE = 10;

}

