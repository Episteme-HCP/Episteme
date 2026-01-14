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

package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.structures.rings.Semiring;
import org.jscience.mathematics.structures.sets.FiniteSet;

/**
 * Boolean algebra - a special algebraic structure for logic operations.
 * <p>
 * A Boolean algebra is a complemented distributive lattice. It provides
 * the mathematical foundation for logic, digital circuits, and set operations.
 * </p>
 * 
 * <h2>Mathematical Definition</h2>
 * <p>
 * A Boolean algebra (B, Ã¢Ë†Â§, Ã¢Ë†Â¨, Ã‚Â¬, 0, 1) consists of:
 * <ul>
 * <li>A set B (typically {false, true} or {0, 1})</li>
 * <li>Binary operations: Ã¢Ë†Â§ (AND), Ã¢Ë†Â¨ (OR)</li>
 * <li>Unary operation: Ã‚Â¬ (NOT)</li>
 * <li>Constants: 0 (false), 1 (true)</li>
 * </ul>
 * </p>
 * 
 * <h2>Boolean Algebra Laws</h2>
 * 
 * <pre>
 * Commutative:    a Ã¢Ë†Â§ b = b Ã¢Ë†Â§ a,  a Ã¢Ë†Â¨ b = b Ã¢Ë†Â¨ a
 * Associative:    (a Ã¢Ë†Â§ b) Ã¢Ë†Â§ c = a Ã¢Ë†Â§ (b Ã¢Ë†Â§ c)
 * Distributive:   a Ã¢Ë†Â§ (b Ã¢Ë†Â¨ c) = (a Ã¢Ë†Â§ b) Ã¢Ë†Â¨ (a Ã¢Ë†Â§ c)
 * Identity:       a Ã¢Ë†Â§ 1 = a,  a Ã¢Ë†Â¨ 0 = a
 * Complement:     a Ã¢Ë†Â§ Ã‚Â¬a = 0,  a Ã¢Ë†Â¨ Ã‚Â¬a = 1
 * De Morgan:      Ã‚Â¬(a Ã¢Ë†Â§ b) = Ã‚Â¬a Ã¢Ë†Â¨ Ã‚Â¬b
 * </pre>
 * 
 * <h2>Applications</h2>
 * <ul>
 * <li><strong>Digital Logic</strong>: Circuit design, gates</li>
 * <li><strong>Programming</strong>: Conditional logic, bitwise operations</li>
 * <li><strong>Set Theory</strong>: Ã¢Ë†Â© (AND), Ã¢Ë†Âª (OR), complement (NOT)</li>
 * <li><strong>Database Queries</strong>: SQL WHERE clauses</li>
 * <li><strong>AI/Logic</strong>: Propositional logic, inference</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * 
 * <pre>{@code
 * BooleanAlgebra bool = BooleanAlgebra.getInstance();
 * 
 * // Logic operations
 * Boolean result = bool.and(true, false); // false
 * Boolean result2 = bool.or(true, false); // true
 * Boolean result3 = bool.not(true); // false
 * Boolean result4 = bool.xor(true, true); // false
 * 
 * // De Morgan's laws
 * Boolean a = true, b = false;
 * assert bool.not(bool.and(a, b)).equals(
 *         bool.or(bool.not(a), bool.not(b)));
 * 
 * // Digital circuit simulation
 * Boolean nandGate = bool.not(bool.and(input1, input2));
 * }</pre>
 * 
 * <h2>Relation to Other Structures</h2>
 * <ul>
 * <li>Boolean algebra is NOT a field (no meaningful addition)</li>
 * <li>It's a special case of a <strong>lattice</strong></li>
 * <li>Two-element Boolean algebra: {0, 1} or {false, true}</li>
 * <li>Can have larger Boolean algebras (power sets)</li>
 * </ul>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>George Boole, "The Mathematical Analysis of Logic", Cambridge: Macmillan,
 * Barclay, and Macmillan, 1847</li>
 * <li>Claude E. Shannon, "A Symbolic Analysis of Relay and Switching Circuits",
 * Master's Thesis, MIT, 1937</li>
 * <li>Alfred North Whitehead and Bertrand Russell, "Principia Mathematica",
 * Cambridge University Press, 1910-1913</li>
 * </ul>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class BooleanAlgebra implements Semiring<Boolean>, FiniteSet<Boolean> {

    /** Singleton instance for standard Boolean algebra */
    private static final BooleanAlgebra INSTANCE = new BooleanAlgebra();

    /**
     * Returns the singleton instance.
     * 
     * @return the Boolean algebra instance
     */
    public static BooleanAlgebra getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private BooleanAlgebra() {
    }

    // --- Semiring Implementation ---

    // --- Semiring Implementation (explicit, not override) ---

    public Boolean operate(Boolean a, Boolean b) {
        return add(a, b); // Boolean operation is OR
    }

    @Override
    public Boolean add(Boolean a, Boolean b) {
        return or(a, b);
    }

    @Override
    public Boolean zero() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean multiply(Boolean a, Boolean b) {
        return and(a, b);
    }

    @Override
    public Boolean one() {
        return Boolean.TRUE;
    }

    public boolean isMultiplicationCommutative() {
        return true;
    }

    // --- FiniteSet Implementation ---

    // --- FiniteSet Implementation (explicit, not override) ---

    public long size() {
        return 2;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Boolean element) {
        return element != null;
    }

    public java.util.Iterator<Boolean> iterator() {
        return java.util.List.of(Boolean.FALSE, Boolean.TRUE).iterator();
    }

    public java.util.stream.Stream<Boolean> stream() {
        return java.util.stream.Stream.of(Boolean.FALSE, Boolean.TRUE);
    }

    public String description() {
        return "{false, true}";
    }

    // --- Boolean Algebra Operations ---

    /**
     * Logical AND (conjunction, meet, Ã¢Ë†Â§).
     * <p>
     * Truth table:
     * 
     * <pre>
     * a     b     a Ã¢Ë†Â§ b
     * false false false
     * false true  false
     * true  false false
     * true  true  true
     * </pre>
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return a Ã¢Ë†Â§ b
     */
    public Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    /**
     * Logical OR (disjunction, join, Ã¢Ë†Â¨).
     * <p>
     * Truth table:
     * 
     * <pre>
     * a     b     a Ã¢Ë†Â¨ b
     * false false false
     * false true  true
     * true  false true
     * true  true  true
     * </pre>
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return a Ã¢Ë†Â¨ b
     */
    public Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    /**
     * Logical NOT (complement, negation, Ã‚Â¬).
     * 
     * @param a the operand
     * @return Ã‚Â¬a
     */
    public Boolean not(Boolean a) {
        return !a;
    }

    /**
     * Logical XOR (exclusive or, Ã¢Å â€¢).
     * <p>
     * Returns true if operands differ.
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return a Ã¢Å â€¢ b
     */
    public Boolean xor(Boolean a, Boolean b) {
        return a ^ b;
    }

    /**
     * Logical NAND (NOT AND).
     * <p>
     * Universal gate - can construct any logic circuit.
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return Ã‚Â¬(a Ã¢Ë†Â§ b)
     */
    public Boolean nand(Boolean a, Boolean b) {
        return not(and(a, b));
    }

    /**
     * Logical NOR (NOT OR).
     * <p>
     * Universal gate - can construct any logic circuit.
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return Ã‚Â¬(a Ã¢Ë†Â¨ b)
     */
    public Boolean nor(Boolean a, Boolean b) {
        return not(or(a, b));
    }

    /**
     * Logical implication (Ã¢â€ â€™).
     * <p>
     * a Ã¢â€ â€™ b is equivalent to Ã‚Â¬a Ã¢Ë†Â¨ b.
     * False implies anything; true implies only true.
     * </p>
     * 
     * @param a premise
     * @param b conclusion
     * @return a Ã¢â€ â€™ b
     */
    public Boolean implies(Boolean a, Boolean b) {
        return or(not(a), b);
    }

    /**
     * Logical equivalence (biconditional, Ã¢â€ â€).
     * <p>
     * True if both operands have the same value.
     * </p>
     * 
     * @param a first operand
     * @param b second operand
     * @return a Ã¢â€ â€ b
     */
    public Boolean equivalent(Boolean a, Boolean b) {
        return a.equals(b);
    }

    @Override
    public String toString() {
        return "BooleanAlgebra";
    }
}

