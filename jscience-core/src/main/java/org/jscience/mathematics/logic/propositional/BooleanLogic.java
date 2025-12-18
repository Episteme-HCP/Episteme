package org.jscience.mathematics.logic.propositional;

import org.jscience.mathematics.logic.Logic;

/**
 * Classical two-valued Boolean logic.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BooleanLogic implements Logic<Boolean> {

    private static final BooleanLogic INSTANCE = new BooleanLogic();

    public static BooleanLogic getInstance() {
        return INSTANCE;
    }

    private BooleanLogic() {
    }

    @Override
    public Boolean trueValue() {
        return Boolean.TRUE;
    }

    @Override
    public Boolean falseValue() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    @Override
    public Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    @Override
    public Boolean not(Boolean a) {
        return !a;
    }
}
