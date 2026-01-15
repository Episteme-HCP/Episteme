package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public class LogicalParameter implements Logical {
    /** DOCUMENT ME! */
    private static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    private boolean value;

    /** DOCUMENT ME! */
    private String name;

/**
     * Creates a new LogicalParameter object.
     *
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public LogicalParameter(String name, boolean value) {
        this.name = name;
        this.value = value;
    } // constructor

/**
     * Creates a new LogicalParameter object.
     *
     * @param name DOCUMENT ME!
     */
    public LogicalParameter(String name) {
        this(name, true);
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue() {
        return value;
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical replace(Expression a, Expression b) {
        return this;
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return false;
    } // contains

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical optimize() {
        return this;
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return name;
    } // toJave

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<logicalparameter> " + name + " </logicalparameter>";
    } // toXML
} // LogicalParameter
