package org.jscience.medicine;

/**
 * A class representing an impairment or defficiency.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Impairment extends Pathology {
    //can be used as combination
    /** DOCUMENT ME! */
    public final static int PHYSICAL = 1; //missing body part, etc

    /** DOCUMENT ME! */
    public final static int PSYCHOLOGICAL = 2;

    //we could provide a stronger classification here
    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new Impairment object.
     *
     * @param name DOCUMENT ME!
     * @param kind DOCUMENT ME!
     */
    public Impairment(String name, int kind) {
        super(name);

        this.kind = kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }
}
