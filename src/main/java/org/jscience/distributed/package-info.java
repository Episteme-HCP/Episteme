/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Distributed computing support for JScience.
 * <p>
 * This package provides integration with Apache Spark and Apache Flink for
 * large-scale scientific computing and stream processing.
 * </p>
 * 
 * <h2>Apache Spark Integration</h2>
 * <p>
 * Distributed matrix and vector operations using RDDs:
 * </p>
 * <ul>
 * <li>{@code DistributedMatrix<T>}: Matrix backed by Spark RDD</li>
 * <li>{@code DistributedVector<T>}: Vector backed by Spark RDD</li>
 * <li>Distributed linear algebra (SVD, PCA, etc.)</li>
 * </ul>
 * 
 * <h2>Apache Flink Integration</h2>
 * <p>
 * Real-time stream processing for scientific data:
 * </p>
 * <ul>
 * <li>{@code StreamQuantity
 * <Q>}: Physical quantity streams</li>
 * <li>Time-series analysis on streaming data</li>
 * <li>Event-driven statistical computation</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
package org.jscience.distributed;

