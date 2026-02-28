/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.philosophy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a logic premise, consisting of a statement and its assigned 
 * truth value.
 * 
 * <p> Premises are used as the fundamental building blocks for arguments and 
 *     syllogisms within a logical framework.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Premise implements Serializable {


    private static final long serialVersionUID = 1L;

    private final String statement;
    private final boolean truthValue;

    /**
     * Creates a premise assumed to be True.
     * @param statement the textual claim of the premise
     */
    public Premise(String statement) {
        this(statement, true);
    }

    /**
     * Creates a premise with an explicit truth value.
     * @param statement  the textual claim
     * @param truthValue validity of the claim
     */
    public Premise(String statement, boolean truthValue) {
        this.statement = Objects.requireNonNull(statement, "Statement cannot be null");
        this.truthValue = truthValue;
    }

    public String getStatement() {
        return statement;
    }

    /**
     * Returns the boolean truth value of this premise.
     * @return true if the premise is considered true in the current context
     */
    public boolean isTrue() {
        return truthValue;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", truthValue ? "TRUE" : "FALSE", statement);
    }
}

