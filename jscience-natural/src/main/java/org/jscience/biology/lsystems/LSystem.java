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
 * @since 1.0
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
     * <li>+ - turn left by angle (Yaw)</li>
     * <li>- - turn right by angle (Yaw)</li>
     * <li>& - pitch down by angle</li>
     * <li>^ - pitch up by angle</li>
     * <li>/ - roll right by angle</li>
     * <li>\ - roll left by angle</li>
     * <li>| - turn around (180 degrees)</li>
     * <li>[ - push state (position, orientation)</li>
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
                case '+' -> commands.add(new TurtleCommand(TurtleCommand.Type.TURN_LEFT, angle)); // Standard: + is left
                case '-' -> commands.add(new TurtleCommand(TurtleCommand.Type.TURN_RIGHT, angle)); // Standard: - is
                                                                                                   // right
                case '&' -> commands.add(new TurtleCommand(TurtleCommand.Type.PITCH_DOWN, angle));
                case '^' -> commands.add(new TurtleCommand(TurtleCommand.Type.PITCH_UP, angle));
                case '/' -> commands.add(new TurtleCommand(TurtleCommand.Type.ROLL_RIGHT, angle));
                case '\\' -> commands.add(new TurtleCommand(TurtleCommand.Type.ROLL_LEFT, angle));
                case '|' -> commands.add(new TurtleCommand(TurtleCommand.Type.TURN_AROUND, 180.0));
                case '[' -> commands.add(new TurtleCommand(TurtleCommand.Type.PUSH, 0));
                case ']' -> commands.add(new TurtleCommand(TurtleCommand.Type.POP, 0));
            }
        }
        return commands;
    }

}


