package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * A ladder in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 Jul 2000
 */
public class Ladder extends PhysicalObject {
/**
     * Class constructor.
     */
    public Ladder() {
        super("a ladder", LIGHT);
    }

/**
     * Class constructor.
     *
     * @param at the position of this object
     */
    public Ladder(Point at) {
        super("a ladder", at, LIGHT);
    }
}
