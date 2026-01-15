package org.jscience.biology;

/**
 * A class representing an Enzyme. An enzyme is a protein used as catalyst
 * in some biological reactions. (Some enzymes which ARE NOT proteins have
 * recently been discovered, though. We do not consider this by now as they do
 * not fit with the common definition.).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is up to you to build out a real molecule out of this sequence
public class Enzyme extends Protein {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = -1;

    /** DOCUMENT ME! */
    public final static int OXYDO_REDUCTASE = 0;

    /** DOCUMENT ME! */
    public final static int TRANSFERASE = 1;

    /** DOCUMENT ME! */
    public final static int HYDROLASE = 2;

    /** DOCUMENT ME! */
    public final static int LYASE = 3;

    /** DOCUMENT ME! */
    public final static int ISOMERASE = 4;

    /** DOCUMENT ME! */
    public final static int LIGASE = 5;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new Enzyme object.
     *
     * @param mrna   DOCUMENT ME!
     * @param coding DOCUMENT ME!
     */
    public Enzyme(mRNA mrna, Alphabet coding) {
        super(mrna, coding);
        this.kind = UNKNOWN;
    }

/**
     * Creates a new Enzyme object.
     *
     * @param aminoacids DOCUMENT ME!
     */
    public Enzyme(AminoAcid[] aminoacids) {
        super(aminoacids);
        this.kind = UNKNOWN;
    }

/**
     * Creates a new Enzyme object.
     *
     * @param acids DOCUMENT ME!
     */
    public Enzyme(String acids) {
        super(acids);
        this.kind = UNKNOWN;
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
