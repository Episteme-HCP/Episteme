/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.logic;

/**
 * NAND Logic Gate.
 */
public class NandGate extends LogicGate {
    @Override
    public LogicState evaluate(LogicState... inputs) {
        if (inputs.length < 1) return LogicState.UNKNOWN;
        LogicState res = inputs[0];
        for (int i = 1; i < inputs.length; i++) {
            res = res.and(inputs[i]);
        }
        return res.not();
    }
}
