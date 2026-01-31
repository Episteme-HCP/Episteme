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

package org.jscience.core.mathematics.logic.temporal;

import org.jscience.core.mathematics.logic.Logic;

import org.jscience.core.mathematics.logic.propositional.Proposition;

/**
 * Temporal logic for reasoning about time and sequence of events.
 * <p>
 * Linear Temporal Logic (LTL) extends propositional logic with temporal
 * operators:
 * <ul>
 * <li>Ã¢â€“Â¡ (Always/Globally) - true at all future times</li>
 * <li>Ã¢â€”â€¡ (Eventually/Finally) - true at some future time</li>
 * <li>Ã¢â€”â€¹ (Next) - true in the next state</li>
 * <li>U (Until) - p U q means p holds until q becomes true</li>
 * </ul>
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Amir Pnueli, "The Temporal Logic of Programs",
 * 18th Annual Symposium on Foundations of Computer Science, IEEE, 1977, pp.
 * 46-57</li>
 * <li>Edmund M. Clarke, E. Allen Emerson, and A. Prasad Sistla,
 * "Automatic Verification of Finite-State Concurrent Systems Using Temporal
 * Logic Specifications",
 * ACM Transactions on Programming Languages and Systems, Vol. 8, No. 2, 1986,
 * pp. 244-263</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TemporalLogic<T> extends Logic<T> {

    /**
     * Temporal operator: Ã¢â€“Â¡Ãâ€  (Always/Globally).
     * True if Ãâ€  is true at all future time points.
     * 
     * @param proposition the proposition
     * @return Ã¢â€“Â¡Ãâ€ 
     */
    T always(Proposition<T> proposition);

    /**
     * Temporal operator: Ã¢â€”â€¡Ãâ€  (Eventually/Finally).
     * True if Ãâ€  is true at some future time point.
     * 
     * @param proposition the proposition
     * @return Ã¢â€”â€¡Ãâ€ 
     */
    T eventually(Proposition<T> proposition);

    /**
     * Temporal operator: Ã¢â€”â€¹Ãâ€  (Next).
     * True if Ãâ€  is true in the next state.
     * 
     * @param proposition the proposition
     * @return Ã¢â€”â€¹Ãâ€ 
     */
    T next(Proposition<T> proposition);

    /**
     * Temporal operator: Ãâ€  U ÃË† (Until).
     * True if Ãâ€  holds until ÃË† becomes true.
     * 
     * @param p first proposition
     * @param q second proposition
     * @return p U q
     */
    T until(Proposition<T> p, Proposition<T> q);
}


