package org.jscience.biology;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a tissue (a group of connected cells).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a group of connected cells (that usually behave the same way)
//we could add support for neighbors (as tissues are part of organs)
public class Tissue extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set cells;

    /** DOCUMENT ME! */
    private Set neighbors;

/**
     * Creates a new Tissue object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Tissue(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.cells = Collections.EMPTY_SET;
            this.neighbors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Tissue constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCells() {
        return cells;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cells DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //we don't check that cells are actually all connected neighbors although it should
    public void setCells(Set cells) {
        Iterator iterator;
        boolean valid;

        if (cells != null) {
            iterator = cells.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Cell;
            }

            if (valid) {
                this.cells = cells;
            } else {
                throw new IllegalArgumentException(
                    "The Set should contain only cells.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of cells can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     */
    public void addCell(Cell cell) {
        cells.add(cell);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     */
    public void removeCell(Cell cell) {
        cells.remove(cell);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getNeighbors() {
        return neighbors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addNeighbor(Tissue neighbor) {
        if (neighbor != null) {
            neighbors.add(neighbor);
            neighbor.addRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException("You can't add a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void addRemoteNeighbor(Tissue neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeNeighbor(Tissue neighbor) {
        if (neighbor != null) {
            neighbors.remove(neighbor);
            neighbor.removeRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException(
                "You can't remove a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void removeRemoteNeighbor(Tissue neighbor) {
        neighbors.remove(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setNeighbors(Set neighbors) {
        Iterator iterator;
        boolean valid;

        if (neighbors != null) {
            iterator = neighbors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Tissue;
            }

            if (valid) {
                this.neighbors = neighbors;
                iterator = neighbors.iterator();

                while (iterator.hasNext()) {
                    ((Tissue) iterator.next()).addRemoteNeighbor(this);
                }
            } else {
                throw new IllegalArgumentException(
                    "The neighbors Set must contain only Tissue.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null neighbors set.");
        }
    }
}
