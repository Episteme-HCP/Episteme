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

import org.jscience.computing.ai.ca.CellularAutomata;


/**
 * This implements the simple cellular automata, Wire World. Wire World is
 * an excellent sample CA since you can easily demonstrate its universal
 * computing properties.
 *
 * @author James Matthews
 */
public class WireWorldCA extends CellularAutomata {
/**
     * Creates a new instance of WireWorldCA
     */
    public WireWorldCA() {
        this(0, 0);
    }

/**
     * Creates a new instance of WireWorldCA with dimensional information.
     *
     * @param size_x the x-size of the CA world.
     * @param size_y the y-size of the CA world.
     */
    public WireWorldCA(int size_x, int size_y) {
        super(size_x, size_y, DOUBLE_BUFFERING);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WireWorldCA wireWorld = new WireWorldCA(100, 100);

        wireWorld.init();
        wireWorld.setWorldAtEx(5, 5,
            "0,0,0,0,0,3,3;1,3,3,3,3,0,0,3;0,0,0,0,0,0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3;1,3,3,3,3,0,0,3;0,0,0,0,0,3,3");
        wireWorld.setWorldAtEx(5, 20,
            "0,0,0,0,0,0,0,0,0,0,3,3;0,0,0,0,0,0,0,0,0,3,0,0,3;1,3,3,3,3,3,3,3,3,0,0,3,3,3,3;0,0,0,0,0,0,0,0,0,0,0,3,0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3;1,3,3,3,3,3,3,3,3,0,0,3,3,3,3;0,0,0,0,0,0,0,0,0,3,0,0,3;0,0,0,0,0,0,0,0,0,0,3,3");
        wireWorld.setWorldAtEx(5, 40,
            "0,0,0,0,0,0,0,0,0,0,3,3;0,0,0,0,0,0,0,0,0,3,0,0,3;1,3,3,3,3,3,3,3,3,0,0,3,3,3,3;0,0,0,0,0,0,0,0,0,0,0,3,0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3;3,3,3,3,3,3,3,3,3,0,0,3,3,3,3;0,0,0,0,0,0,0,0,0,3,0,0,3;0,0,0,0,0,0,0,0,0,0,3,3");

        wireWorld.flipBuffer();

        iterateCA(wireWorld, args);
    }

    /**
     * Iterate the CA one time-step.
     */
    public void doStep() {
        //
        int caAt = 0;
        int u = 0;

        for (int j = 0; j < caWorld_y; j++) {
            for (int i = 0; i < caWorld_x; i++) {
                caAt = getWorldAt(i, j);

                if (caAt == 1) {
                    setWorldAt(i, j, 2);
                } else if (caAt == 2) {
                    setWorldAt(i, j, 3);
                } else if (caAt == 3) {
                    u = 0;
                    u += ((getWorldAt(i - 1, j - 1) == 1) ? 1 : 0);
                    u += ((getWorldAt(i, j - 1) == 1) ? 1 : 0);
                    u += ((getWorldAt(i + 1, j - 1) == 1) ? 1 : 0);
                    u += ((getWorldAt(i - 1, j) == 1) ? 1 : 0);
                    u += ((getWorldAt(i + 1, j) == 1) ? 1 : 0);
                    u += ((getWorldAt(i - 1, j + 1) == 1) ? 1 : 0);
                    u += ((getWorldAt(i, j + 1) == 1) ? 1 : 0);
                    u += ((getWorldAt(i + 1, j + 1) == 1) ? 1 : 0);

                    if ((u == 1) || (u == 2)) {
                        setWorldAt(i, j, 1);
                    } else {
                        setWorldAt(i, j, 3);
                    }
                }
            }
        }

        flipBuffer();
    }

    /**
     * Initialize WireWorld.
     */
    public void init() {
        clearWorld();

        setWorldColour(0, java.awt.Color.WHITE);
        setWorldColour(1, java.awt.Color.YELLOW);
        setWorldColour(2, java.awt.Color.RED);
        setWorldColour(3, java.awt.Color.BLACK);
    }
}
