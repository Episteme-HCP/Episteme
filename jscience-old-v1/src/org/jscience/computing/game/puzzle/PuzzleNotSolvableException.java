/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.GameException;
import org.jscience.computing.game.GamePlay;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
public class PuzzleNotSolvableException extends GameException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2136915923568305302L;

/**
     * Creates a new PuzzleNotSolvableException object.
     *
     * @param game DOCUMENT ME!
     * @param text DOCUMENT ME!
     */
    public PuzzleNotSolvableException(GamePlay game, String text) {
        super(game, text);
    }
}
