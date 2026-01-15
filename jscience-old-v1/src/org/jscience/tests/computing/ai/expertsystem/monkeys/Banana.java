package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * The bananas in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 Jul 2000
 */
public class Banana extends PhysicalObject {
/**
     * Class constructor.
     */
    public Banana() {
        super("a banana", LIGHT);
    }

/**
     * Class constructor.
     *
     * @param at the position of this object
     */
    public Banana(Point at) {
        super("a banana", at, LIGHT);
    }
}
