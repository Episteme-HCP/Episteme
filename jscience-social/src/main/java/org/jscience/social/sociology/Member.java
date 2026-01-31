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

package org.jscience.social.sociology;

import org.jscience.natural.biology.Individual;
import org.jscience.social.economics.money.Money;
import java.util.Objects;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents an individual participant in a social ritual or celebration.
 * A member typically has a role and may be required to pay a fee or contribution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Member extends Role {

    /** The price or contribution required to participate. */
    @Attribute
    private Money price;

    /**
     * Creates a new Member for a specific celebration.
     *
     * @param individual the person who is a member
     * @param situation  the celebration situation
     * @throws NullPointerException if individual or situation is null
     */
    public Member(Individual individual, Celebration situation) {
        super(Objects.requireNonNull(individual, "Individual cannot be null"), 
              "Member", 
              Objects.requireNonNull(situation, "Situation cannot be null"), 
              Role.CLIENT);
    }

    /**
     * Returns the participation price.
     *
     * @return the price
     */
    public Money getPrice() {
        return price;
    }

    /**
     * Sets the participation price.
     *
     * @param price the new price
     * @throws NullPointerException if price is null
     */
    public void setPrice(Money price) {
        this.price = Objects.requireNonNull(price, "Price cannot be null");
    }
}

