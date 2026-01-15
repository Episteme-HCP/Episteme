package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * A blanket in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 Jul 2000
 */
public class Blanket extends PhysicalObject {
/**
     * Class constructor.
     */
    public Blanket() {
        super("a blanket", LIGHT);
    }

/**
     * Class constructor.
     *
     * @param at the position of this object
     */
    public Blanket(Point at) {
        super("a blanket", at, LIGHT);
    }
}
