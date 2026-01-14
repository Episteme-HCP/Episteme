/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.computing.ai.cellularautomaton;

/**
 * Implements a simple CA agent to be used with {@link
 * org.jscience.computing.ai.cellularautomaton.CellularAutomataLayered}, where
 * the CA agent is an embodied entity within the CA world.
 *
 * @author James Matthews
 * @version 0.5
 */
public class CAAgent {
    /** An integer to be used at programmer's discretion. */
    public int dataInteger;

    /** The x-position. */
    protected int pos_x;

    /** The y-position. */
    protected int pos_y;

    /** The agent state. */
    protected int state;

/**
     * Default constructor. Positional and state variables are initialized to
     * -1.
     */
    public CAAgent() {
        this(-1, -1, -1);
    }

/**
     * Creates a CA2DInfo instance with positional information.
     *
     * @param x X-position.
     * @param y Y-position.
     */
    public CAAgent(int x, int y) {
        this(x, y, -1);
    }

/**
     * Creates a CA2DInfo instance with positional and state information.
     *
     * @param x X-position.
     * @param y Y-position.
     * @param s cell state.
     */
    public CAAgent(int x, int y, int s) {
        pos_x = x;
        pos_y = y;
        state = s;
    }

    /**
     * Set the position of this CA.
     *
     * @param x X-position to set.
     * @param y Y-position to set.
     */
    public void setPosition(int x, int y) {
        pos_x = x;
        pos_y = y;
    }

    /**
     * Set the state of this CA.
     *
     * @param s this cell state.
     */
    public void setState(int s) {
        state = s;
    }

    /**
     * Set only the X-position.
     *
     * @param x this cell's new X-position.
     */
    public void setX(int x) {
        pos_x = x;
    }

    /**
     * Set only the Y-position.
     *
     * @param y this cell's new Y-position.
     */
    public void setY(int y) {
        pos_y = y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return retrieve the X-position of the cell.
     */
    public int getX() {
        return pos_x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return retrieve the Y-position of the cell.
     */
    public int getY() {
        return pos_y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return retrieve the state of the cell.
     */
    public int getState() {
        return state;
    }
}
