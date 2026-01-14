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

import org.jscience.computing.game.GameDriver;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.Player;

import org.jscience.util.Monitor;
import org.jscience.util.Stopwatch;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class TilePuzzleTest {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        tryAndError();
    }

    /**
     * DOCUMENT ME!
     */
    static void bestFirstSearch() {
        TilePuzzle game = getGame();
        Player player = new TilePuzzlePlayer();
        Monitor monitor = new Monitor();

        // todo
    }

    /**
     * DOCUMENT ME!
     */
    static void tryAndError() {
        TilePuzzle game = getGame();
        Player player = new TilePuzzlePlayer();
        GameDriver play = new GameDriver(game, new Player[] { player }, 3);
        System.out.print("trying to solve the following puzzle with Player AI:");
        System.out.println(game);
        System.out.print("tiles out of place: " +
            TilePuzzlePlayer.outOfPlace(game));
        System.out.println(", manhattan distance: " +
            TilePuzzlePlayer.manhattanDistance(game));
        System.out.println("start time: " + new java.util.Date());

        Stopwatch t = new Stopwatch();
        GameMove move = null;

        do {
            move = play.autoMove();
            System.out.print(move + ", ");
        } while (move != null);

        long ms = t.stop();
        System.out.print("\n#of moves: " + game.getMoveHistory().length);
        System.out.println(", time taken: " + Stopwatch.timeAsString(ms));

        try {
            game.isSolved();
        } catch (PuzzleNotSolvableException e) {
            System.out.println("** puzzle is not solvable");
        }

        System.out.println(game);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static TilePuzzle getGame() {
        Integer[][] puzzle = new Integer[3][3];
        puzzle[0][0] = new Integer(5);
        puzzle[0][1] = new Integer(6);
        puzzle[0][2] = new Integer(7);
        puzzle[1][0] = new Integer(4);
        puzzle[1][1] = null;
        puzzle[1][2] = new Integer(8);
        puzzle[2][0] = new Integer(3);
        puzzle[2][1] = new Integer(2);
        puzzle[2][2] = new Integer(1);

        TilePuzzle game = new TilePuzzle("AI class puzzle", puzzle,
                TilePuzzleSamples.getAIClassPuzzle());
        game.setReverseMoveDisabled(true);
        game.setEndWhenSolved(true);

        return game;
    }
}
