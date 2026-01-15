/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import org.jscience.computing.game.MoveTemplate;


/**
 * The following siimplification is made: if a move is a double down or BJ
 * insurance, the bet is assumed to be using the maximum allowance, meaning
 * full double bet for double down and half the initial bet for insurance.
 *
 * @author Holger Antelmann
 */
class BJMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 9006165155211489494L;

    /** DOCUMENT ME! */
    public static final byte SURRENDER = 0;

    /** DOCUMENT ME! */
    public static final byte SPLIT = 1;

    /** DOCUMENT ME! */
    public static final byte DOUBLE = 2;

    /** DOCUMENT ME! */
    public static final byte STAY = 3;

    /** DOCUMENT ME! */
    public static final byte HIT = 4;

    /** DOCUMENT ME! */
    public static final byte ACCEPT_INSURANCE = 5;

    /** DOCUMENT ME! */
    public static final byte DECLINE_INSURANCE = 6;

    /** DOCUMENT ME! */
    public static final byte EVEN = 7;

    /** DOCUMENT ME! */
    private byte type;

/**
     * Creates a new BJMove object.
     *
     * @param player DOCUMENT ME!
     * @param type   DOCUMENT ME!
     */
    BJMove(int player, byte type) {
        super(player);

        if ((type < 0) || (type > 8)) {
            throw (new IllegalArgumentException("type is invalid"));
        }

        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte getType() {
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
        if (BJMove.class != obj.getClass()) {
            return false;
        }

        BJMove m = (BJMove) obj;

        if (player != m.player) {
            return false;
        }

        if (type != m.type) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "player: " + player + "; ";

        switch (type) {
        case BJMove.SURRENDER:
            s += "surrender";

            break;

        case BJMove.SPLIT:
            s += "split";

            break;

        case BJMove.DOUBLE:
            s += "double";

            break;

        case BJMove.STAY:
            s += "stay";

            break;

        case BJMove.HIT:
            s += "hit";

            break;

        case BJMove.ACCEPT_INSURANCE:
            s += "accept insurance";

            break;

        case BJMove.DECLINE_INSURANCE:
            s += "decline insurance";

            break;

        case BJMove.EVEN:
            s += "even money";

            break;

        default: // nothing

        }

        return s;
    }
}
