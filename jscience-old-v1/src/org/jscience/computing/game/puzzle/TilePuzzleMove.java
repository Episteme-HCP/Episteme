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

package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.MoveTemplate;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class TilePuzzleMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3188510788619077146L;

    // values are picked so that the sum of two moves
    // that negate each other is zero
    // (this feature is used in the TilePuzzlePlayer heuristic)
    /** DOCUMENT ME! */
    public static final int UP = 1;

    /** DOCUMENT ME! */
    public static final int DOWN = -1;

    /** DOCUMENT ME! */
    public static final int LEFT = 2;

    /** DOCUMENT ME! */
    public static final int RIGHT = -2;

    /** DOCUMENT ME! */
    private int type;

/**
     * Creates a new TilePuzzleMove object.
     *
     * @param type DOCUMENT ME!
     */
    public TilePuzzleMove(int type) {
        super(0);
        this.type = type;

        switch (type) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            break;

        default:
            throw new IllegalArgumentException("type not supported");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (TilePuzzleMove.class != obj.getClass()) {
            return false;
        }

        if (getType() != ((TilePuzzleMove) obj).getType()) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public String toString() {
        switch (type) {
        case UP:
            return "up";

        case DOWN:
            return "down";

        case LEFT:
            return "left";

        case RIGHT:
            return "right";

        default:
            throw new Error();
        }
    }
}
