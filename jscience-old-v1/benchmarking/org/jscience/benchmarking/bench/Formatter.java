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
 An EXTREMELY Lame & Minimal Number formatter.
 Only what is essential for the Benchmarker...
 I'll evolve it if necessary.

 Need to hack in some robustness !!!!

@author Bruce R. Miller (bruce.miller@nist.gov)
@author Contribution of the National Institute of Standards and Technology,
@author not subject to copyright.
*/

//code under public domain

public class Formatter {

  static final int MAXDEC=15;

  /** Create a string of `padding' consisting of n copies of c.*/
  public static String pad(char c, int n){
    if (n <= 0) return "";
    char s[]=new char[n];
    for(int i=0; i<n; i++) s[i]=c;
    return new String(s); }

  /** Simple number Formatter, formats num with ndecs decimal places. */
  public static String format(double num, int ndecs) {
    try {
      if (Math.abs(ndecs) > MAXDEC) return Double.toString(num); // punt!
      double shifted = num*Math.pow(10.0,ndecs);
      if (Math.abs(shifted) > Long.MAX_VALUE) return Double.toString(num); // punt!
      String s = Long.toString((long) shifted);
      StringBuffer sb = new StringBuffer();
      sb.append(pad('0',ndecs-s.length()+1)); // need Padding on left.
      sb.append(s);
      if (ndecs > 0)
	sb.insert(sb.length()-ndecs,'.');
      else
	sb.append(pad('0',-ndecs));
      return sb.toString();
    } catch(Exception e) {
      return Double.toString(num); }} // punt!

  /** Format an array of numbers, returning an array of strings.*/
  public static String[] format(double nums[], int ndecs){
    String vals[]=new String[nums.length];
    for(int i=0; i<nums.length; i++)
      vals[i]=format(nums[i],ndecs); 
    return vals; }

  /** Concatenate two arrays of strings returning a single longer array.*/
  public static String[] conc(String a[], String b[]){
    String c[]=new String[a.length+b.length];
    System.arraycopy(a,0,c,0,a.length);
    System.arraycopy(b,0,c,a.length,b.length);
    return c; }

  /** Append an array of columns to another array of strings, aligning the new
    * strings on left (justify=0) or right (justify=1). */
  public static void addColumn(String table[], String column[], int justify){
    int l0=0, l1=0, ll;
    for(int i=0; i<table.length; i++){
      if((ll=table[i].length())>l0) l0=ll;
      if((ll=column[i].length())>l1) l1=ll; }
    for(int i=0; i<table.length; i++)
      table[i] += pad(' ',(l0-table[i].length())+1+(l1-column[i].length())*justify) +
	column[i];
  }
}
