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

package org.jscience.tests.computing.ai.demos;

import org.jscience.computing.ai.cellularautomaton.CellularAutomata;

import java.awt.*;


/**
 * This class implements Conway's Life using the CellularAutomata classes
 * and double-buffering for speed.
 *
 * @author James Matthews
 */
public class ConwaysLifeCA extends CellularAutomata {
/**
     * Default constructor
     */
    public ConwaysLifeCA() {
        this(0, 0);
    }

/**
     * Constructor with size initializors.
     *
     * @param size_x the x-size of the world.
     * @param size_y the y-size of the world.
     */
    public ConwaysLifeCA(int size_x, int size_y) {
        super(size_x, size_y, DOUBLE_BUFFERING);
    }

    /**
     * Advance the world one timestep.
     */
    public void doStep() {
        int neighbours = 0;

        for (int i = 0; i < caWorld_x; i++) {
            for (int j = 0; j < caWorld_y; j++) {
                neighbours += getWorldAt(i - 1, j - 1);
                neighbours += getWorldAt(i, j - 1);
                neighbours += getWorldAt(i + 1, j - 1);
                neighbours += getWorldAt(i - 1, j);
                neighbours += getWorldAt(i + 1, j);
                neighbours += getWorldAt(i - 1, j + 1);
                neighbours += getWorldAt(i, j + 1);
                neighbours += getWorldAt(i + 1, j + 1);

                if (((getWorldAt(i, j) == 1) && (neighbours == 2)) ||
                        (neighbours == 3)) {
                    setWorldAt(i, j, 1);
                } else {
                    setWorldAt(i, j, 0);
                }

                neighbours = 0;
            }
        }

        flipBuffer();
    }

    /**
     * Randomly initializes the world.
     */
    public void init() {
        // Default colours
        setWorldColour(0, Color.white);
        setWorldColour(1, Color.black);

        // Default geometry
        setGeometry(TORODIAL);

        // Create an initial random grid
        for (int i = 0; i < caWorld_x; i++) {
            for (int j = 0; j < caWorld_y; j++) {
                if (Math.random() < 0.2) {
                    setWorldAt(i, j, 1);
                } else {
                    setWorldAt(i, j, 0);
                }
            }
        }

        flipBuffer();
    }

    /**
     * Standard test function using iterateCA.
     *
     * @param args the arguments to be passed on to iterateCA.
     *
     * @see CellularAutomata#iterateCA(CellularAutomata,String[])
     */
    public static void main(String[] args) {
        ConwaysLifeCA life = new ConwaysLifeCA(150, 150);

        life.init();
        iterateCA(life, args);
    }
}
