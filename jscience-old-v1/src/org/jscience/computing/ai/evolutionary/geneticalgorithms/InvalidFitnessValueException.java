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

package org.jscience.computing.ai.evolutionary.geneticalgorithms;

/**
 * Exception thrown when an invalid fitness value is tried to be used
 * <p/>
 * Default implementation throws this exception when the fitness value has
 * a negative value in these cases. You should correct your fitness function
 * for returning positive values.
 * <p/>
 * Custom implementations of genetic algorithms may use other (problem spceific)
 * criterias to throw this exception.
 * <p/>
 * If fitness function returns negative values, then selectChromosome()
 * method may return a negative index for individual. To prevent
 * ArrayIndexOutOfBounds runtime exception, this exception is implemented.
 * Other solutions are welcome, if you have.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class InvalidFitnessValueException extends Exception {
    /**
     * Constructs a new InvalidFitnessValueException instance with the
     * given error message.
     *
     * @param message an error message describing the reason of this exception
     */
    public InvalidFitnessValueException(String message) {
        super(message);
    }
}
