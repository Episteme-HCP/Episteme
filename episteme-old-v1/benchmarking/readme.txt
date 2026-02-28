This directory contains the code from 
http://math.nist.gov/scimark2/
http://www.netlib.org/benchmark/linpackjava/

Scimark2 is Public domain apart from FFT code under GPL.
linpackjava is mBSD and compatible with GPL.


We plan to use this code as an inspiration to develop our own benchmark suite.


After about 10 years of Java computing, there is still no consensus as to wether java is a fast and efficient scientific language along to Fortran and C++.
Ok, there exist one or two articles and webpages. But none to benchmark java 1.6 or to compare java scientific packages between them.

The aim would not be to build a java MFLOPS raw performance measure (as compared to assembly code or C++) but to evaluate and compare scientific computations solutions.

Our benchmark should provide:
1. comparison with fortran and c++ for similar problems
2. comparison with other maths packages in java, namely : colt, jama, JTEM, (jsci), episteme official, episteme experimental
3. a system to report results, statistics, etc.
4. a dedicated website/page
5. a summary of results

This would not only help us to make advances in java scientific computing market penetration but also to optimize our libraries as to be better than the others.

There is only one existing scientific benchmark in Java: scimark. Simple but efficient. Yet it only solves case number 1.

This would be the work for someone interested in a short project, who has some knowledge in the 3 languages (fortran, java C++). Alternatively one could start with only C and Java or even by just writting some code to compare between java maths packages. 


Links:

website:
http://www.idiom.com/~zilla/Computer/javaCbenchmark.html

benchmarks:
http://www.netlib.org/benchmark/linpackjava/
http://math.nist.gov/scimark2/

papers:
http://www.ukhec.ac.uk/events/javahec/pozo.pdf
http://www.philippsen.com/JGI2001/finalpapers/18500097.pdf
