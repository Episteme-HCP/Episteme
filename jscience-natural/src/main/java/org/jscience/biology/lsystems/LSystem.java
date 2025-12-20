/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.lsystems;

import java.util.*;

/**
 * Lindenmayer System (L-System) for generating fractal and plant-like
 * structures.
 * <p>
 * L-systems are parallel rewriting systems used to model the growth of plants
 * and other biological structures. They consist of:
 * <ul>
 * <li>Alphabet - set of symbols</li>
 * <li>Axiom - initial state</li>
 * <li>Production rules - symbol replacement rules</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class LSystem {

    private final String axiom;
    private final Map<Character, String> rules;
    private final double angle; // turning angle in degrees

    /**
     * Creates a new L-System.
     *
     * @param axiom the initial string
     * @param angle the turning angle in degrees
     */
    public LSystem(String axiom, double angle) {
        this.axiom = axiom;
        this.rules = new HashMap<>();
        this.angle = angle;
    }

    /**
     * Adds a production rule.
     *
     * @param symbol      the symbol to replace
     * @param replacement the replacement string
     */
    public LSystem addRule(char symbol, String replacement) {
        rules.put(symbol, replacement);
        return this;
    }

    /**
     * Generates the L-system string after n iterations.
     *
     * @param iterations number of iterations
     * @return the generated string
     */
    public String generate(int iterations) {
        String current = axiom;
        for (int i = 0; i < iterations; i++) {
            current = iterate(current);
        }
        return current;
    }

    private String iterate(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(rules.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public String getAxiom() {
        return axiom;
    }

    public double getAngle() {
        return angle;
    }

    public Map<Character, String> getRules() {
        return Collections.unmodifiableMap(rules);
    }

    /**
     * Interprets the L-system string as turtle graphics commands.
     * <p>
     * Standard symbols:
     * <ul>
     * <li>F - move forward and draw</li>
     * <li>f - move forward without drawing</li>
     * <li>+ - turn right by angle</li>
     * <li>- - turn left by angle</li>
     * <li>[ - push state (position, angle)</li>
     * <li>] - pop state</li>
     * </ul>
     * </p>
     */
    public List<TurtleCommand> interpret(String lstring) {
        List<TurtleCommand> commands = new ArrayList<>();
        for (char c : lstring.toCharArray()) {
            switch (c) {
                case 'F' -> commands.add(new TurtleCommand(TurtleCommand.Type.FORWARD, 1.0));
                case 'f' -> commands.add(new TurtleCommand(TurtleCommand.Type.FORWARD_NO_DRAW, 1.0));
                case '+' -> commands.add(new TurtleCommand(TurtleCommand.Type.TURN_RIGHT, angle));
                case '-' -> commands.add(new TurtleCommand(TurtleCommand.Type.TURN_LEFT, angle));
                case '[' -> commands.add(new TurtleCommand(TurtleCommand.Type.PUSH, 0));
                case ']' -> commands.add(new TurtleCommand(TurtleCommand.Type.POP, 0));
            }
        }
        return commands;
    }

    /**
     * Turtle graphics command.
     */
    public static class TurtleCommand {
        public enum Type {
            FORWARD, FORWARD_NO_DRAW, TURN_RIGHT, TURN_LEFT, PUSH, POP
        }

        public final Type type;
        public final double value;

        public TurtleCommand(Type type, double value) {
            this.type = type;
            this.value = value;
        }
    }

    // ========== Common L-Systems ==========

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
     * Hilbert Curve (space-filling).
     */
    public static LSystem hilbertCurve() {
        return new LSystem("A", 90)
                .addRule('A', "-BF+AFA+FB-")
                .addRule('B', "+AF-BFB-FA+");
    }
}
