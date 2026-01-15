package org.jscience.biology;

import org.jscience.medicine.Disease;


/**
 * A class representing a virus. Virus are not real "alive" cells. They
 * normally do not contain DNA but RNA although this may happen. They invade
 * cells and use them to reproduce.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should provide a field for the name of the virus although many virus don't have one (you can have such information in the disease name)
public class Virus extends Object implements Cloneable {
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int MOVING = 0; //between hosts or in a host

    /** DOCUMENT ME! */
    public final static int INVADING = 1; //finding a cell

    /** DOCUMENT ME! */
    public final static int REPRODUCING = 2; //using the cell

    /** DOCUMENT ME! */
    public final static int LEAVING = 3; //destroying the cell or the like

    /** DOCUMENT ME! */
    private Genome genome;

    /** DOCUMENT ME! */
    private Alphabet coding;

    /** DOCUMENT ME! */
    private int stage;

    /** DOCUMENT ME! */
    private Disease disease;

/**
     * Creates a new Virus object.
     *
     * @param genome DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Virus(Genome genome) {
        if (genome != null) {
            this.genome = genome;
            this.coding = Codons.STANDARD;
            this.stage = MOVING;
            this.disease = null;
        } else {
            throw new IllegalArgumentException(
                "The Virus constructor can't have null arguments.");
        }
    }

/**
     * Creates a new Virus object.
     *
     * @param genome DOCUMENT ME!
     * @param coding DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Virus(Genome genome, Alphabet coding) {
        if ((genome != null) && (coding != null)) {
            this.genome = genome;
            this.coding = coding;
            this.stage = MOVING;
            this.disease = null;
        } else {
            throw new IllegalArgumentException(
                "The Virus constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Genome getGenome() {
        return genome;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Alphabet getCoding() {
        return coding;
    }

    //may return null (unknown or unset disease)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Disease getDisease() {
        return disease;
    }

    //set null for unknown
    /**
     * DOCUMENT ME!
     *
     * @param disease DOCUMENT ME!
     */
    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    //the current stage of the virus
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStage() {
        return stage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stage DOCUMENT ME!
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    //goes to the next stage
    /**
     * DOCUMENT ME!
     */
    public void nextStage() {
        if (stage < LEAVING) {
            stage += 1;
        } else {
            stage = MOVING;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //shallow copy
    public Virus clone() {
        Virus virus;

        //virus = (Virus) super.clone();
        //virus.genome = getGenome();
        //virus.coding = getCoding();
        virus = new Virus(getGenome(), getCoding());
        virus.stage = getStage();
        virus.disease = getDisease();

        return virus;
    }

    //the cell kind is modified to ABNORMAL_CELL
    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Virus reproduce(Cell cell) {
        cell.setKind(Cell.ABNORMAL_CELL);

        return new Virus(getGenome(), getCoding());
    }
}
