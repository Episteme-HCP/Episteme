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

package org.jscience.mathematics.logic.multivalued;

/**
 * Linear logic - resource-aware logic.
 * <p>
 * Linear logic treats propositions as resources that are consumed when used.
 * Key connectives:
 * - Ã¢Å â€” (tensor): multiplicative conjunction
 * - Ã¢â€¦â€¹ (par): multiplicative disjunction
 * - Ã¢Å â€¢ (plus): additive disjunction
 * - & (with): additive conjunction
 * - ! (bang): unlimited resource
 * - ? (whimper): unlimited consumption
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LinearLogic {

    /**
     * Represents a linear logic proposition with resource tracking.
     */
    public static class Proposition {
        private final String name;
        private int uses; // Number of times this resource can be used

        public Proposition(String name, int uses) {
            this.name = name;
            this.uses = uses;
        }

        public boolean canUse() {
            return uses > 0;
        }

        public void use() {
            if (uses > 0) {
                uses--;
            } else {
                throw new IllegalStateException("Resource exhausted: " + name);
            }
        }

        public int getUses() {
            return uses;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + (uses == Integer.MAX_VALUE ? "!" : "[" + uses + "]");
        }
    }

    /**
     * Creates a linear proposition (single use).
     * 
     * @param name the name
     * @return the proposition
     */
    public static Proposition linear(String name) {
        return new Proposition(name, 1);
    }

    /**
     * Creates an unlimited proposition (bang modality).
     * 
     * @param name the name
     * @return the proposition
     */
    public static Proposition unlimited(String name) {
        return new Proposition(name, Integer.MAX_VALUE);
    }

    /**
     * Tensor product (Ã¢Å â€”) - multiplicative conjunction.
     * Both resources must be available.
     * 
     * @param a first proposition
     * @param b second proposition
     * @return combined proposition
     */
    public static Proposition tensor(Proposition a, Proposition b) {
        if (!a.canUse() || !b.canUse()) {
            throw new IllegalStateException("Resources not available for tensor");
        }
        a.use();
        b.use();
        return new Proposition(a.getName() + " Ã¢Å â€” " + b.getName(), 1);
    }

    /**
     * Plus (Ã¢Å â€¢) - additive disjunction.
     * Choose one of the resources.
     * 
     * @param a           first proposition
     * @param b           second proposition
     * @param chooseFirst true to choose first, false for second
     * @return chosen proposition
     */
    public static Proposition plus(Proposition a, Proposition b, boolean chooseFirst) {
        if (chooseFirst) {
            if (!a.canUse()) {
                throw new IllegalStateException("First resource not available");
            }
            a.use();
            return new Proposition(a.getName(), 1);
        } else {
            if (!b.canUse()) {
                throw new IllegalStateException("Second resource not available");
            }
            b.use();
            return new Proposition(b.getName(), 1);
        }
    }

    /**
     * With (&) - additive conjunction.
     * Both must be available, but only one is used.
     * 
     * @param a first proposition
     * @param b second proposition
     * @return combined proposition
     */
    public static Proposition with(Proposition a, Proposition b) {
        if (!a.canUse() || !b.canUse()) {
            throw new IllegalStateException("Resources not available for with");
        }
        return new Proposition(a.getName() + " & " + b.getName(),
                Math.min(a.getUses(), b.getUses()));
    }

    /**
     * Bang (!) modality - makes a resource unlimited.
     * 
     * @param p the proposition
     * @return unlimited version
     */
    public static Proposition bang(Proposition p) {
        return new Proposition("!" + p.getName(), Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "Linear Logic";
    }
}

