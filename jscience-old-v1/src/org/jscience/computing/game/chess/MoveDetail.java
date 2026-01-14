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

package org.jscience.computing.game.chess;

import java.io.Serializable;


/**
 * MoveDetail is a 'read-only' class, none of its members can be changed
 * after instanciation. This is to ensure that there is no potential problem
 * when a ChessMove is cloned, which will not clone the MoveDetail ojbect.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.chess.ChessMove
 */
class MoveDetail implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 498690297597150543L;

    /** DOCUMENT ME! */
    private ChessPiece captured;

    /** DOCUMENT ME! */
    private ChessPiece promoted;

/**
     * Creates a new MoveDetail object.
     *
     * @param captured DOCUMENT ME!
     */
    MoveDetail(ChessPiece captured) {
        this.captured = captured;
        this.promoted = null;
    }

/**
     * Creates a new MoveDetail object.
     *
     * @param captured DOCUMENT ME!
     * @param promoted DOCUMENT ME!
     */
    MoveDetail(ChessPiece captured, ChessPiece promoted) {
        this.captured = captured;
        this.promoted = promoted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessPiece getCaptured() {
        return captured;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessPiece getPromotion() {
        return promoted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "[org.jscience.computing.game.chess.MoveDetail:]";
        s += (" captured: " + captured);
        s += (", promoted: " + promoted);
        s += " [MoveDetail end]";

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        MoveDetail d = (MoveDetail) super.clone();

        if (captured != null) {
            d.captured = (ChessPiece) captured.clone();
        }

        if (promoted != null) {
            d.promoted = (ChessPiece) promoted.clone();
        }

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        MoveDetail other = null;

        try {
            other = (MoveDetail) obj;
        } catch (ClassCastException e) {
            return false;
        }

        if (captured == null) {
            if (other.captured != null) {
                return false;
            }
        } else {
            if (other.captured == null) {
                return false;
            }

            if (!other.captured.equals(captured)) {
                return false;
            }
        }

        if (promoted == null) {
            if (other.promoted != null) {
                return false;
            }
        } else {
            if (other.promoted == null) {
                return false;
            }

            if (!other.promoted.equals(promoted)) {
                return false;
            }
        }

        return true;
    }
}
