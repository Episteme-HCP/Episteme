package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * An object in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 Jul 2000
 */
public class PhysicalObject implements Constants {
    /** The position of this object. */
    private Point at;

    /** The object over which this one is on. */
    private PhysicalObject on;

    /** A description of this object. */
    private String description;

    /** The weight of this object. */
    private int weight;

/**
     * Class constructor.
     *
     * @param description a description of this object.
     * @param weight      the weight of this object.
     */
    public PhysicalObject(String description, int weight) {
        this.description = description;
        this.weight = weight;
    }

/**
     * Class constructor.
     *
     * @param description a description of this object.
     * @param at          the position of this object
     * @param weight      the weight of this object.
     */
    public PhysicalObject(String description, Point at, int weight) {
        this.description = description;
        this.at = at;
        this.weight = weight;
    }

    /**
     * Returns the position of this object.
     *
     * @return the position of this object.
     */
    public Point getAt() {
        return at;
    }

    /**
     * Returns a description of this object.
     *
     * @return a description of this object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the object over which this one is on.
     *
     * @return the object over which this one is on.
     */
    public PhysicalObject getOn() {
        return on;
    }

    /**
     * Returns the weight of this object.
     *
     * @return the weight of this object.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Checks whether this object is at a given position.
     *
     * @param p the position being checked.
     *
     * @return <code>true</code> if the object is at the given position;
     *         <code>false</code> otherwise.
     */
    public boolean isAt(Point p) {
        return at.equals(p);
    }

    /**
     * Checks whether this object is on a given object.
     *
     * @param o the object being checked.
     *
     * @return <code>true</code> if the object is on the given object;
     *         <code>false</code> otherwise.
     */
    public boolean isOn(Object o) {
        return o.equals(on);
    }

    /**
     * Defines the position of this object.
     *
     * @param newAt the new position of this object.
     */
    public void setAt(Point newAt) {
        at = newAt;
    }

    /**
     * Defines the object over which this one is on.
     *
     * @param value the object over which this one is on.
     */
    public void setOn(PhysicalObject value) {
        this.on = value;
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(description);

        if (at != null) {
            sb.append(" at (");
            sb.append(at.x);
            sb.append(',');
            sb.append(at.y);
            sb.append(')');
        }

        if (on != null) {
            sb.append(" on <");
            sb.append(on);
            sb.append('>');
        }

        return sb.toString();
    }
}
