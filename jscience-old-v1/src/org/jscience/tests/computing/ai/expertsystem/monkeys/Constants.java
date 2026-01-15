package org.jscience.tests.computing.ai.expertsystem.monkeys;

/**
 * Useful constants for the monkey and bananas problem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  19 Jul 2000
 */
public interface Constants {
    /** Goal status = satisfied. */
    public static final int SATISFIED = 0;

    /** Goal status = active. */
    public static final int ACTIVE = 1;

    /** Goal type: to be AT somewhere. */
    public static final int AT = 0;

    /** Goal type: to HOLD something. */
    public static final int HOLD = 1;

    /** Goal type: to be ON something. */
    public static final int ON = 2;

    /** Weight constants: light. */
    public static final int LIGHT = 0;

    /** Weight constants: heavy. */
    public static final int HEAVY = 1;

    /** Common objects: the floor. */
    public static final PhysicalObject FLOOR = new PhysicalObject("floor", HEAVY);

    /** Common objects: the ceiling. */
    public static final PhysicalObject CEILING = new PhysicalObject("ceiling",
            HEAVY);
}
