/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
