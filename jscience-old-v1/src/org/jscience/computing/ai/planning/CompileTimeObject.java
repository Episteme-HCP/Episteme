package org.jscience.computing.ai.planning;

/**
 * All the objects at compile time are instances of classes that are
 * derived from this abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class CompileTimeObject {
    /** The new line character in the platform Planner is running on. */
    final static String endl = System.getProperty("line.separator");

    /**
     * This abstract function produces the Java code needed to
     * implement this compile time element.
     *
     * @return the produced code as a <code>String</code>.
     */
    public abstract String toCode();
}
