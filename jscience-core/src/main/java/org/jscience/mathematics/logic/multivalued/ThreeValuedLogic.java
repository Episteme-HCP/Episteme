/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.mathematics.logic.Logic;

/**
 * Three-valued logic system (True, False, Unknown).
 * <p>
 * Based on Kleene's strong logic of indeterminacy.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThreeValuedLogic implements Logic<ThreeValuedLogic.Value> {

    public enum Value {
        TRUE,
        FALSE,
        UNKNOWN
    }

    private static final ThreeValuedLogic INSTANCE = new ThreeValuedLogic();

    public static ThreeValuedLogic getInstance() {
        return INSTANCE;
    }

    private ThreeValuedLogic() {
    }

    @Override
    public Value trueValue() {
        return Value.TRUE;
    }

    @Override
    public Value falseValue() {
        return Value.FALSE;
    }

    @Override
    public Value and(Value a, Value b) {
        if (a == Value.FALSE || b == Value.FALSE)
            return Value.FALSE;
        if (a == Value.TRUE && b == Value.TRUE)
            return Value.TRUE;
        return Value.UNKNOWN;
    }

    @Override
    public Value or(Value a, Value b) {
        if (a == Value.TRUE || b == Value.TRUE)
            return Value.TRUE;
        if (a == Value.FALSE && b == Value.FALSE)
            return Value.FALSE;
        return Value.UNKNOWN;
    }

    @Override
    public Value not(Value a) {
        switch (a) {
            case TRUE:
                return Value.FALSE;
            case FALSE:
                return Value.TRUE;
            default:
                return Value.UNKNOWN;
        }
    }
}


