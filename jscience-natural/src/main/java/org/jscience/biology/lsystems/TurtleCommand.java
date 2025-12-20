/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.lsystems;

/**
 * Turtle graphics command for interpreting L-System strings.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class TurtleCommand {
    public enum Type {
        FORWARD, FORWARD_NO_DRAW,
        TURN_RIGHT, TURN_LEFT, // Yaw
        PITCH_UP, PITCH_DOWN, // Pitch
        ROLL_LEFT, ROLL_RIGHT, // Roll
        TURN_AROUND,
        PUSH, POP
    }

    public final Type type;
    public final double value;

    public TurtleCommand(Type type, double value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type + "(" + value + ")";
    }
}
