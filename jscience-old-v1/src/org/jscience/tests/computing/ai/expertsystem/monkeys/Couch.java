package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * A couch in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 Jul 2000
 */
public class Couch extends PhysicalObject {
/**
     * Class constructor.
     */
    public Couch() {
        super("a couch", HEAVY);
    }

/**
     * Class constructor.
     *
     * @param at the position of this object
     */
    public Couch(Point at) {
        super("a couch", at, HEAVY);
    }
}
