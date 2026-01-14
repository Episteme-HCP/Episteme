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

import org.generation5.bio.*;

import org.jscience.computing.ai.ca.CAAgent;
import org.jscience.computing.ai.ca.CellularAutomataLayered;

import java.awt.*;


/**
 * Implements Langton's Ant.
 *
 * @author James Matthews
 */
public class LangtonAntCA extends CellularAutomataLayered {
    /** Ant to move up */
    public final static int UP = 0;

    /** Ant to move right */
    public final static int RIGHT = 1;

    /** Ant to move down */
    public final static int DOWN = 2;

    /** Ant to move left */
    public final static int LEFT = 3;

/**
     * Default constructor
     */
    public LangtonAntCA() {
        this(0, 0);
    }

/**
     * Constructor with additional size information.
     *
     * @param size_x the x-size of the world.
     * @param size_y the y-size of the world.
     */
    public LangtonAntCA(int size_x, int size_y) {
        super(size_x, size_y);
    }

    /**
     * Advance the ants one timestep.
     */
    public void doStep() {
        for (int i = 0; i < getNumCAs(); i++) {
            CAAgent ca = getCA(i);
            int x = ca.getX();
            int y = ca.getY();
            int d = ca.getState();

            switch (d) {
            case UP:
                moveCARelative(i, 0, -1);

                break;

            case RIGHT:
                moveCARelative(i, 1, 0);

                break;

            case DOWN:
                moveCARelative(i, 0, 1);

                break;

            case LEFT:
                moveCARelative(i, -1, 0);

                break;
            }

            x = ca.getX();
            y = ca.getY();

            int p = getWorldAt(x, y);

            if (p == 1) {
                setWorldAt(x, y, 0);
                ca.setState((d + 1) % 4);

                continue;
            } else if (p == 0) {
                setWorldAt(x, y, 1);
                ca.setState((4 + (d - 1)) % 4);
            }
        }
    }

    /**
     * Initializes the world with one ant in the centre of the world.
     */
    public void init() {
        removeAll();
        clearWorld();

        setWorldColour(0, Color.white);
        setWorldColour(1, Color.black);

        addAutomaton(getSizeX() / 2, getSizeY() / 2, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args arguments to pass to iterateCA.
     *
     * @see org.jscience.computing.ai.ca.CellularAutomata#iterateCA(org.jscience.computing.ai.ca.CellularAutomata,
     *      String[])
     */
    public static void main(String[] args) {
        LangtonAntCA ants = new LangtonAntCA(640, 480);

        ants.init();
        ants.removeAll(); // remove default ant

        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 50; i++) {
            ants.addAutomaton(random.nextInt(640), random.nextInt(480),
                random.nextInt(4));
        }

        for (int i = 0; i < 100; i++) {
            ants.setWorldAt(random.nextInt(640), random.nextInt(480), 1);
        }

        iterateCA(ants, args);
    }
}
