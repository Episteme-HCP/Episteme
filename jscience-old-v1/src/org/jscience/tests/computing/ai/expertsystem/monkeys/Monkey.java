/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
