package org.jscience.tests.computing.ai.expertsystem.hanoi;

/*
 * org.jscience.tests.computing.ai.expertsystem - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */

/**
 * This class models an encapsulation for a solution for the Towers
 * of Hanoi problem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  15 Mar 1998
 */
public class Hanoi {

    /**
     * The number of discs in this problem.
     */
    private int discs;

    /**
     * The pin from where the discs come.
     */
    private int source;

    /**
     * The pin the disks have to be moved to.
     */
    private int destination;

    /**
     * The first subproblem used to solve this problem.
     */
    private Hanoi sub1;

    /**
     * The second subproblem used to solve this problem.
     */
    private Hanoi sub2;

    /**
     * Flag which indicates whether this problem is solved.
     */
    private boolean ok;

    /**
     * The problem solution, made up by a Strings in the
     * form "Disk moved from <pin1> to <pin2>".
     */
    private String solution = "";

    /**
     * Class constructor.
     *
     * @param numDiscs    the number of the discs for this instance.
     * @param source      the source pin
     * @param destination the destination pin
     */
    public Hanoi(int numDiscs, int source, int destination) {
        this.discs = numDiscs;
        this.source = source;
        this.destination = destination;
    }

    /**
     * Adds a movement to the solution.
     *
     * @param from the pin from where the disc is moved.
     * @param to   the pin to where the disc is moved.
     */
    public void addMove(int from, int to) {
        solution = "Disk moved from " + from + " to " + to;
    }

    /**
     * Prints the tree for this instance of the Hanoi problem. Useful
     * for debugging.
     */
    public void printSolution() {
        dump(0);
    }

    /**
     * Prints the tree for this instance of the Hanoi problem. Useful
     * for debugging.
     *
     * @param spaces the identation for the printed output.
     */
    private void dump(int spaces) {
        if (sub1 != null) {
            sub1.dump(spaces + 2);
        }
        System.out.println(solution);
        if (sub2 != null) {
            sub2.dump(spaces);
        }
    }

    /**
     * Returns the destination pin for this problem.
     *
     * @return the destination pin for this problem.
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Returns the number of discs of this problem.
     *
     * @return the number of discs of this problem.
     */
    public int getDiscs() {
        return discs;
    }

    /**
     * Returns the intermediate pin form this problem.
     *
     * @return the intermediate pin form this problem.
     */
    public int getIntermediate() {
        return (6 - source - destination);
    }

    /**
     * Returns the state of this problem.
     *
     * @return <code>true</code> if this problem has already been
     *         solved; <code>false</code> otherwise.
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Returns the source pin for this problem.
     *
     * @return the source pin for this problem.
     */
    public int getSource() {
        return source;
    }

    /**
     * Returns the first subproblem for this problem.
     *
     * @return the first subproblem for this problem.
     */
    public Hanoi getSub1() {
        return sub1;
    }

    /**
     * Returns the second subproblem for this problem.
     *
     * @return the second subproblem for this problem.
     */
    public Hanoi getSub2() {
        return sub2;
    }

    /**
     * Determines whether this problem has already been solved.
     *
     * @param newValue the new value for the state of this problem.
     */
    public void setOk(boolean newValue) {
        this.ok = newValue;
    }

    /**
     * Determines the first subproblem for this problem.
     *
     * @param sub1 the new value for the first subproblem of this one.
     */
    public void setSub1(Hanoi sub1) {
        this.sub1 = sub1;
    }

    /**
     * Determines the second subproblem for this problem.
     *
     * @param sub1 the new value for the second subproblem of this one.
     */
    public void setSub2(Hanoi sub2) {
        this.sub2 = sub2;
    }

    /**
     * Returns a string representation of this object. Useful
     * for debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return ("Hanoi[discs=" + discs + ",from " + source + " to " + destination + ",sub1=" + sub1 + ",sub2=" + sub2 + "]");
    }
}
