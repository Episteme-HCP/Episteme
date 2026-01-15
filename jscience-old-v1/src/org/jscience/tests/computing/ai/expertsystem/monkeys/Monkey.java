package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;


/**
 * An agent in the monkey and bananas world.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  19 Jul 2000
 */
public class Monkey extends PhysicalObject {
    /** The object this monkey holds. */
    private PhysicalObject objectHeld;

/**
     * Class constructor.
     */
    public Monkey() {
        super("monkey", LIGHT);
    }

/**
     * Class constructor.
     *
     * @param at the position of this object
     */
    public Monkey(Point at) {
        super("monkey", at, LIGHT);
    }

/**
     * Class constructor.
     *
     * @param name the name of this monkey
     * @param at   the position of this object
     */
    public Monkey(String name, Point at) {
        super(name, at, LIGHT);
    }

    /**
     * Brings an object to somewhere.
     *
     * @param obj the object to be brought.
     * @param position the new position of this monkey and the given object.
     */
    public void bring(PhysicalObject obj, Point position) {
        System.out.println(getDescription() + " brings " + obj + " to " +
            position);
        setAt(position);
        obj.setAt(position);
    }

    /**
     * Climbs on some object.
     *
     * @param obj the object this monkey is climbing on.
     */
    public void climbOn(PhysicalObject obj) {
        System.out.println(getDescription() + " climbs on " + obj);
        setOn(obj);
    }

    /**
     * Drops the object this monkey is holding.
     */
    public void drop() {
        System.out.println(getDescription() + " drops " + objectHeld);
        objectHeld.setOn(FLOOR);
        setObjectHeld(null);
    }

    /**
     * Returns the object this monkey holds.
     *
     * @return the object this monkey holds.
     */
    public PhysicalObject getObjectHeld() {
        return objectHeld;
    }

    /**
     * Go somewhere.
     *
     * @param position the new position of this monkey.
     */
    public void goTo(Point position) {
        System.out.println(getDescription() + " goes to " + position);
        setAt(position);
    }

    /**
     * Checks whether this monkey holds something.
     *
     * @return <code>true</code> if this monkey holds nothing;
     *         <code>false</code> otherwise.
     */
    public boolean holdsNothing() {
        return (objectHeld == null);
    }

    /**
     * Checks whether this monkey holds something.
     *
     * @return <code>true</code> if this monkey holds something;
     *         <code>false</code> otherwise.
     */
    public boolean holdsSomething() {
        return (objectHeld != null);
    }

    /**
     * Checks whether this monkey holds a given object.
     *
     * @param obj the object to be checked.
     *
     * @return <code>true</code> if this monkey holds the given object;
     *         <code>false</code> otherwise.
     */
    public boolean isHolding(PhysicalObject obj) {
        return obj.equals(objectHeld);
    }

    /**
     * Jumps on some object.
     *
     * @param obj the object this monkey is jumping on.
     */
    public void jumpOn(PhysicalObject obj) {
        System.out.println(getDescription() + " jumps on " + obj);
        setOn(obj);
    }

    /**
     * Defines the object this monkey holds.
     *
     * @param value the object this monkey holds.
     */
    public void setObjectHeld(PhysicalObject value) {
        this.objectHeld = value;
    }

    /**
     * Takes an object.
     *
     * @param obj the object this monkey is taking.
     */
    public void take(PhysicalObject obj) {
        System.out.println(getDescription() + " takes " + obj);
        setObjectHeld(obj);
        obj.setOn(null);
    }
}
