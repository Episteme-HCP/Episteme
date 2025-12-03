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
package org.jscience.mathematics.logic;

import org.jscience.mathematics.logic.propositional.Proposition;

/**
 * Modal logic for reasoning about necessity and possibility.
 * <p>
 * Modal logic extends classical logic with modal operators:
 * <ul>
 * <li>□ (Box/Necessarily) - true in all possible worlds</li>
 * <li>◇ (Diamond/Possibly) - true in at least one possible world</li>
 * </ul>
 * </p>
 * <p>
 * Common modal logic systems:
 * <ul>
 * <li><strong>K</strong>: Basic modal logic</li>
 * <li><strong>T</strong>: K + reflexivity (□φ → φ)</li>
 * <li><strong>S4</strong>: T + transitivity (□φ → □□φ)</li>
 * <li><strong>S5</strong>: S4 + symmetry (◇φ → □◇φ)</li>
 * </ul>
 * </p>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>Saul Kripke, "Semantical Considerations on Modal Logic",
 * Acta Philosophica Fennica, Vol. 16, 1963, pp. 83-94</li>
 * <li>C. I. Lewis and C. H. Langford, "Symbolic Logic",
 * Century Company, 1932 (foundational work)</li>
 * </ul>
 * 
 * @param <T> truth value type
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ModalLogic<T> extends Logic<T> {

    /**
     * Modal operator: □φ (Necessarily).
     * True if φ is true in all accessible possible worlds.
     * 
     * @param proposition the proposition
     * @return □φ
     */
    T necessarily(Proposition<T> proposition);

    /**
     * Modal operator: ◇φ (Possibly).
     * True if φ is true in at least one accessible possible world.
     * 
     * @param proposition the proposition
     * @return ◇φ
     */
    T possibly(Proposition<T> proposition);

    /**
     * Returns the modal logic system (K, T, S4, S5, etc.).
     * 
     * @return the system name
     */
    default String getSystem() {
        return "K"; // Basic modal logic
    }
}
