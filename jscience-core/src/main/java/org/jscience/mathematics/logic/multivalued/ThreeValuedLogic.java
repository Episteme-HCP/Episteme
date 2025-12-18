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
