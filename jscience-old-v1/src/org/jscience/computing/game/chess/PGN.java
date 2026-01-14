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

import org.jscience.computing.game.GameMove;

import java.io.FileWriter;
import java.io.IOException;


/**
 * class PGN - for "Portable Game Notation" - provides functions to convert
 * ChessGame instances to PGN notation and vice versa. NOT FINISHED YET!
 * (begun: 7/9/2001)
 *
 * @author Holger Antelmann
 */
class PGN {
    /** DOCUMENT ME! */
    static String linebreak = System.getProperty("line.separator", "\n");

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param fileName DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void export(ChessGame game, String fileName)
        throws IOException {
        FileWriter file;
        System.out.println("exporting " + game.getGameName() + " .. ");

        //try {
        file = new FileWriter(fileName, true);
        file.write("[Event \"" + game.getGameName() + "\"]" + linebreak);
        file.write("[Site \"http://www.antelmann.com\"]" + linebreak);
        file.write("[Date \"" +
            (new java.util.Date(System.currentTimeMillis())).toString() +
            "\"]" + linebreak);
        file.write("[Round \"n/a\"]" + linebreak);

        //file.write("[White \"" + game.getPlayer(ChessBoard.WHITE).getPlayerName() + "\"]" + linebreak);
        //file.write("[Black \"" + game.getPlayer(ChessBoard.BLACK).getPlayerName() + "\"]" + linebreak);
        file.write("[Result \"*\"]" + linebreak);

        GameMove[] moves = game.getMoveHistory();
        String line = "";

        for (int i = 0; i < moves.length; i++) {
            line = line + moves[i] + " ";

            if (line.length() > 70) {
                file.write(line + linebreak);
                line = "";
            }
        }

        if (!line.equals("")) {
            file.write(line + linebreak);
        }

        file.flush();
        file.close();

        /*} catch (IOException e) {
            System.err.print("I/O Error in org.jscience.computing.game.chess.PGN.export with filename:");
            System.err.println(fileName + "; Details follow:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static ChessGame load(String fileName) throws IOException {
        ChessGame game = new ChessGame();
        throw (new UnsupportedOperationException(
            "function org.jscience.computing.game.chess.PGN.import() is not implemented yet"));
    }
}
