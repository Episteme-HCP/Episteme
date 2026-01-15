package org.jscience.engineering;

/**
 * A class representing common constants used in engineering.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class EngineeringConstants extends Object {
    /** kinds of transformation */
    public final static int UNKNOWN = 0;

    /** kinds of transformation */
    public final static int DISPLACEMENT = 1; //addition and suppression at the same time, thus shifting to something else

    /** kinds of transformation */
    public final static int SIMPLIFICATION = 2;

    /** kinds of transformation */
    public final static int COMPLEXIFICATION = 3; //generalization, inclusion
}
