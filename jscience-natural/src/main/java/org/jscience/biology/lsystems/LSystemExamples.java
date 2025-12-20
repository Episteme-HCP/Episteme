/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.lsystems;

/**
 * Common L-Systems examples and fractals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public final class LSystemExamples {

    private LSystemExamples() {
    } // Utility class

    /**
     * Koch Snowflake curve.
     */
    public static LSystem kochSnowflake() {
        return new LSystem("F--F--F", 60)
                .addRule('F', "F+F--F+F");
    }

    /**
     * Sierpinski Triangle.
     */
    public static LSystem sierpinskiTriangle() {
        return new LSystem("F-G-G", 120)
                .addRule('F', "F-G+F+G-F")
                .addRule('G', "GG");
    }

    /**
     * Dragon Curve.
     */
    public static LSystem dragonCurve() {
        return new LSystem("FX", 90)
                .addRule('X', "X+YF+")
                .addRule('Y', "-FX-Y");
    }

    /**
     * Fractal Plant (bush-like).
     */
    public static LSystem fractalPlant() {
        return new LSystem("X", 25)
                .addRule('X', "F+[[X]-X]-F[-FX]+X")
                .addRule('F', "FF");
    }

    /**
     * Simple Tree.
     */
    public static LSystem simpleTree() {
        return new LSystem("F", 25.7)
                .addRule('F', "F[+F]F[-F]F");
    }

    /**
     * Algae growth (original L-system by Lindenmayer).
     */
    public static LSystem algae() {
        return new LSystem("A", 0)
                .addRule('A', "AB")
                .addRule('B', "A");
    }

    /**
     * Hilbert Curve (2D Space-filling).
     */
    public static LSystem hilbertCurve() {
        return new LSystem("A", 90)
                .addRule('A', "-BF+AFA+FB-")
                .addRule('B', "+AF-BFB-FA+");
    }

    /**
     * 3D Hilbert Curve.
     */
    public static LSystem hilbertCurve3D() {
        return new LSystem("A", 90)
                .addRule('A', "B-F+CFC+F-D&F^D-F+&&CFC+F+B//")
                .addRule('B', "A&F^CFB^F^D^^-F-D^|F^B|FC^F^A//")
                .addRule('C', "|D^|F^B-F+C^F^A&&FA&F^C+F+B^F^D//")
                .addRule('D', "|CFB-F+B|FA&F^A&&FB-F+B|FC//");
    }
}
