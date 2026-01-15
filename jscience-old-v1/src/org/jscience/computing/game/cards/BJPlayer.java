/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.TemplatePlayer;


/**
 * adds AI to the game BlackJack
 *
 * @author Holger Antelmann
 *
 * @see BlackJack
 */
public class BJPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8446913838963307498L;

/**
     * Creates a new BJPlayer object.
     */
    public BJPlayer() {
        this("BlackJack default dealer");
    }

/**
     * Creates a new BJPlayer object.
     *
     * @param playerName DOCUMENT ME!
     */
    public BJPlayer(String playerName) {
        this(playerName, false);
    }

/**
     * Creates a new BJPlayer object.
     *
     * @param playerName      DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     */
    public BJPlayer(String playerName, boolean trackingEnabled) {
        super(playerName, 0, trackingEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof BlackJack);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        BlackJack g = (BlackJack) game;

        //BJHand u = g.getCurrentHand(move.getPlayer());
        BJHand u = g.getCurrentHand(role[0]);
        int v = g.getHandValue(u.cards);

        switch (((BJMove) move).getType()) {
        case BJMove.ACCEPT_INSURANCE:
            return -1;

        case BJMove.DECLINE_INSURANCE:
            return 1;

        case BJMove.SURRENDER:
            return -1;

        case BJMove.DOUBLE:

            if ((v == 11) || ((BlackJack.isSoft(u.cards)) && (v == 18))) {
                return 2;
            }

            return -1;

        case BJMove.STAY:

            if (v >= 17) {
                return 1;
            }

            return -1;

        case BJMove.HIT:

            //if ((v == 17) && (BlackJack.isSoft(u.cards))) return 2;
            if (v < 17) {
                return 1;
            }

            return -1;

        case BJMove.EVEN:
            return -1;

        case BJMove.SPLIT:

            if (BlackJack.isSoft(u.cards)) {
                return 3;
            }

            return -1;

        default:
            return -1;
        }
    }
}
