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

package org.jscience.computing.ai.automata;

/**
 * Interface for Cellular Automata.
 * <p>
 * A cellular automaton is a discrete model studied in computability theory,
 * mathematics, physics, and theoretical biology. It consists of a grid of
 * cells, each in one of a finite number of states, evolving through discrete
 * time steps according to local rules.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface CellularAutomaton<S> {

    /**
     * Advances the automaton by one generation.
     */
    void nextGeneration();

    /**
     * Gets the state of the cell at the specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the state at (x, y)
     */
    S getState(int x, int y);

    /**
     * Sets the state of the cell at the specified coordinates.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param state the new state
     */
    void setState(int x, int y, S state);

    /**
     * Gets the width of the grid.
     *
     * @return grid width
     */
    int getWidth();

    /**
     * Gets the height of the grid.
     *
     * @return grid height
     */
    int getHeight();

    /**
     * Returns the current generation number.
     *
     * @return generation count
     */
    default long getGeneration() {
        return 0;
    }

    /**
     * Resets the automaton to initial state.
     */
    default void reset() {
        // Default no-op
    }
}


