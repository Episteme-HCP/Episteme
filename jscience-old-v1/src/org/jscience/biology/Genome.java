package org.jscience.biology;

/**
 * A class representing the whole genome of an individual. For example see
 * ftp://ftp.ncbi.nih.gov/genomes/H_sapiens/ for the canonical human genome
 * sequence
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://www.ncbi.nlm.nih.gov/genome/seq/
//there is a small issue here as we expect to encode the information for both living cells and viruses
//you should encode plasmids here
public class Genome extends Object {
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int HAPLOID = 1;

    /** DOCUMENT ME! */
    public final static int DIPLOID = 2;

    /** DOCUMENT ME! */
    public final static int POLYPLOID = 3;

    /** DOCUMENT ME! */
    public final static int ANEUPLOID = 4;

    /**
     * The array of Chains (DNA, RNA) strains which defines without
     * error any individual from any specie.
     */
    private Chain[] chains;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Constructs a genome. For diploid (or higher) genomes (which length
     * should be dividable by two), please get sure that the second half of
     * the array contains the homologous genes in the same order than the
     * first half, as this information is used in cell meiosis.
     *
     * @param chains DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Genome(Chain[] chains) {
        if (chains != null) {
            this.chains = chains;
            this.kind = UNKNOWN;
        } else {
            throw new IllegalArgumentException(
                "The Genome constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Chain[] getChains() {
        return chains;
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

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //we don't test equality of the kind 
    public boolean equals(Object o) {
        Genome value;
        int i;
        boolean result;

        if ((o != null) && (o instanceof Genome)) {
            value = (Genome) o;
            i = 0;
            result = (getChains().length == value.getChains().length);

            while ((i < getChains().length) && result) {
                result = getChains()[i].equals(value.getChains()[i]);
                i++;
            }

            return result;
        } else {
            return false;
        }
    }
}
