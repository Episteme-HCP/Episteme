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
 * Pluggable compute backend abstraction layer.
 * <p>
 * This package provides a Service Provider Interface (SPI) for implementing
 * compute backends (CPU, GPU, quantum, etc.) while hiding implementation
 * details
 * from users.
 * </p>
 * <p>
 * <b>Key interfaces:</b>
 * <ul>
 * <li>{@link org.jscience.technicals.backend.ComputeBackend} - SPI for backend
 * implementations</li>
 * <li>{@link org.jscience.technicals.backend.ExecutionContext} - Context for
 * running
 * operations</li>
 * <li>{@link org.jscience.technicals.backend.Operation} - Functional interface
 * for
 * computations</li>
 * </ul>
 * </p>
 * <p>
 * <b>Usage example:</b>
 * 
 * <pre>
 * // Use default backend (transparent)
 * ComputeBackend backend = BackendManager.getDefault();
 * try (ExecutionContext ctx = backend.createContext()) {
 *     Double result = ctx.execute(context -> Math.sqrt(2.0));
 * }
 * 
 * // Or select specific backend
 * ComputeBackend gpu = BackendManager.select("GPU");
 * </pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
package org.jscience.technical.backend;
