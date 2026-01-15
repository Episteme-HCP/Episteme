package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * A goal of the agent in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  19 Jul 2000
 */
public class Goal implements Constants {
    /** The object associated with this goal. */
    private PhysicalObject object;

    /** The status of this goal. (ACTIVE, SATISFIED) */
    private int status = ACTIVE;

    /** The type of this goal (HOLD, AT, ON) */
    private int type;

    /** The destiny of this goal (if it's the case) */
    private Point to;

/**
     * Class constructor.
     *
     * @param type the type of this goal.
     */
    public Goal(int type) {
        this.type = type;
    }

    /**
     * Returns the object associated with this goal.
     *
     * @return the object associated with this goal.
     */
    public PhysicalObject getObject() {
        return object;
    }

    /**
     * Returns the destiny of this goal (if applicable)
     *
     * @return the destiny of this goal (if applicable)
     */
    public Point getTo() {
        return to;
    }

    /**
     * Returns the type of this goal, which can be one of (HOLD, AT,
     * ON).
     *
     * @return the type of this goal.
     */
    public int getType() {
        return type;
    }

    /**
     * Checks whether this goal is active.
     *
     * @return <code>true</code> if this goal is active; <code>false</code>
     *         otherwise.
     */
    public boolean isActive() {
        return (status == ACTIVE);
    }

    /**
     * Checks whether this goal is satisfied.
     *
     * @return <code>true</code> if this goal is satisfied; <code>false</code>
     *         otherwise.
     */
    public boolean isSatisfied() {
        return (status == SATISFIED);
    }

    /**
     * Sets the object associated with this goal.
     *
     * @param newObject the object associated with this goal.
     */
    public void setObject(PhysicalObject newObject) {
        object = newObject;
    }

    /**
     * Defines that this goal has been accomplished.
     */
    public void setSatisfied() {
        status = SATISFIED;
    }

    /**
     * Sets the destiny of this goal (if applicable).
     *
     * @param newTo the new destiny of this goal.
     */
    public void setTo(Point newTo) {
        to = newTo;
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<< ");

        switch (status) {
        case ACTIVE:
            sb.append("active ");

            break;

        case SATISFIED:
            sb.append("satisfied ");

            break;
        }

        switch (type) {
        case AT:
            sb.append("[at] ");

            break;

        case HOLD:
            sb.append("[hold] ");

            break;

        case ON:
            sb.append("[on] ");

            break;

        default:
            sb.append("[] ");

            break;
        }

        sb.append(String.valueOf(object));

        if (to != null) {
            sb.append(" to:(");
            sb.append(to.x);
            sb.append(',');
            sb.append(to.y);
            sb.append(")");
        }

        sb.append(" >>");

        return sb.toString();
    }
}
