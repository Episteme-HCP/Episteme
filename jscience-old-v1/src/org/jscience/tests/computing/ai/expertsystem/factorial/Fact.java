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

package org.jscience.tests.computing.ai.expertsystem.factorial;


/*
 * org.jscience.tests.computing.ai.expertsystem - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */
/**
 * Class used to solve the factorial problem (N!) with
 * org.jscience.tests.computing.ai.expertsystem.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 */
public class Fact {
    /** The number whose factorial we are trying to find. */
    private int n;

    /** Flag indicating whether this problem is already solved. */
    private boolean ok = false;

    /** The factorial of n. */
    private int result;

    /** The factorial of (n - 1). */
    private Fact subProblem;

/**
     * Class constructor.
     *
     * @param n the number whose factorial we are trying to find.
     */
    public Fact(int n) {
        this.n = n;
    }

    /**
     * Returns the number whose factorial we are trying to find.
     *
     * @return the number whose factorial we are trying to find.
     */
    public int getN() {
        return n;
    }

    /**
     * Returns the factorial of n.
     *
     * @return the factorial of n.
     */
    public int getResult() {
        return result;
    }

    /**
     * Returns the factorial of (n - 1).
     *
     * @return the factorial of (n - 1).
     */
    public Fact getSubProblem() {
        return subProblem;
    }

    /**
     * Checks whether this problem is already solved.
     *
     * @return <code>true</code> if this problem is already solved;
     *         <code>false</code> otherwise.
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Defines the factorial of n.
     *
     * @param value the factorial of n.
     */
    public void setResult(int value) {
        this.result = value;
        this.ok = true;
    }

    /**
     * Defines the factorial of (n - 1).
     *
     * @param value the factorial of (n - 1).
     */
    public void setSubProblem(Fact value) {
        this.subProblem = value;
    }

    /**
     * Returns a String representation of this problem. Useful for
     * debugging.
     *
     * @return a String representation of this problem.
     */
    public String toString() {
        return ("Fact[n=" + n + ",result=" + result + "]");
    }
}
