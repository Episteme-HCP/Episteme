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

import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class Knight extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -2291606173030135074L;

    /**
     * Creates a new Knight object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Knight(int color, ChessBoard board, BoardPosition position)
        throws IllegalArgumentException {
        super(color, board, position);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<BoardPosition> possibleTargets() {
        Vector<BoardPosition> targets = new Vector<BoardPosition>();
        BoardPosition p;
        char c;
        int r;
        c = (char) (getPosition().getColumn() + 1);
        r = getPosition().getRow() + 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 2);
        r = getPosition().getRow() + 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 1);
        r = getPosition().getRow() - 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 2);
        r = getPosition().getRow() - 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 1);
        r = getPosition().getRow() + 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 1);
        r = getPosition().getRow() - 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 2);
        r = getPosition().getRow() + 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 2);
        r = getPosition().getRow() - 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        return targets;
    }
}
