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

package org.jscience.computing.game.fourwins;

import org.jscience.computing.game.MoveTemplate;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class FourWinsMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3738931563012339301L;

    /** DOCUMENT ME! */
    private int column;

    /** DOCUMENT ME! */
    private double heuristic;

/**
     * Creates a new FourWinsMove object.
     *
     * @param player DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    FourWinsMove(int player, int column) {
        super(player);
        this.column = column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";

        switch (getPlayer()) {
        case 0:
            s += "X: ";

            break;

        case 1:
            s += "O: ";

            break;

        default:
            s += "none";
        }

        s += (column + 1);

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FourWinsMove)) {
            return false;
        }

        FourWinsMove o = (FourWinsMove) obj;

        if (player != o.player) {
            return false;
        }

        if (column != o.column) {
            return false;
        }

        return true;
    }
}
