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

package org.jscience.computing.game;

import java.io.Serializable;


/**
 * GameBoardPosition implements a board position that can be used for
 * various board game implementations. It is also used in the GameBoardMove.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.GameBoardMove
 */
public class GameBoardPosition implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1175207030136507332L;

    /** DOCUMENT ME! */
    private int position;

/**
     * Creates a new GameBoardPosition object.
     *
     * @param position DOCUMENT ME!
     */
    public GameBoardPosition(int position) {
        this.position = position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int asInteger() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ("GameBoardPosition: " + position);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        //if (obj.getClass() != getClass()) return false;
        return (((GameBoardPosition) obj).position == position);

        /*try {
            return (((GameBoardPosition)obj).asInteger() == position);
        } catch (ClassCastException e) {
            return false;
        }*/
    }

    /*
    protected Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
    */
}
