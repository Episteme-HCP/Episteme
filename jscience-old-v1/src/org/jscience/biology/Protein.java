package org.jscience.biology;

/**
 * A class representing Protein. This class accounts for peptides (small
 * proteins) and enzyms (catalyst proteins). Proteins are also known as
 * protides. Please note we do not represent these molecules as we should: we
 * do not develop the formula beyond the amino acids level, therefore there
 * are extraneous H20 molecules (as many as aminoacids minus one). Correct
 * terminations NH2 and COOH are however included (as normal aminoacids
 * terminations). For the same reason there is no internal binding such as
 * dissfulfur bridges.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is up to you to build out a real molecule out of this sequence
public class Protein extends Object implements Cloneable {
    /** The amino-acid array. */
    private AminoAcid[] aminoacids;

/**
     * Constructs a Protein molecule. The length of the mRNA array must be a
     * multiple of 3. Moreover, only the last codon (the last three elements
     * of the array) should contain the stop sequence (sequence TGA, TAG, TAA
     * or UGA, UAG, UAA, for the universal codons). This is a "pass all
     * coding" as arrays if length not dividable by 3 are also coded as well
     * as sequence that contain no or many stop codons (that are simply
     * ignored).
     *
     * @param mrna   DOCUMENT ME!
     * @param coding DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Protein(mRNA mrna, Alphabet coding) {
        int i;
        boolean termination;
        AminoAcid currentAminoAcid;

        if ((mrna != null) && (coding != null)) {
            i = 0;
            termination = false;
            aminoacids = new AminoAcid[(int) Math.floor(mrna.getBases().length / 3)];

            while ((i < Math.floor(mrna.getBases().length / 3)) &&
                    (!termination)) {
                currentAminoAcid = coding.getAminoAcid(mrna.getBases()[i * 3],
                        mrna.getBases()[(i * 3) + 1],
                        mrna.getBases()[(i * 3) + 2]);
                termination = (currentAminoAcid == null) ||
                    coding.isStopCodon(mrna.getBases()[i * 3],
                        mrna.getBases()[(i * 3) + 1],
                        mrna.getBases()[(i * 3) + 2]);

                if (!termination) {
                    aminoacids[i] = currentAminoAcid;
                    i++;
                }
            }
        } else {
            throw new IllegalArgumentException(
                "The Protein constructor can't have null arguments.");
        }
    }

    //defines the protein using the aminoacids sequence
    /**
     * Creates a new Protein object.
     *
     * @param aminoacids DOCUMENT ME!
     */
    public Protein(AminoAcid[] aminoacids) {
        if (aminoacids != null) {
            this.aminoacids = aminoacids;
        } else {
            throw new IllegalArgumentException(
                "The Protein constructor can't have null arguments.");
        }
    }

    //defines the protein using the aminoacids sequence letters
    /**
     * Creates a new Protein object.
     *
     * @param acids DOCUMENT ME!
     */
    public Protein(String acids) {
        AminoAcid aminoacid;

        if (acids != null) {
            aminoacids = new AminoAcid[acids.length()];

            for (int i = 0; i < acids.length(); i++) {
                aminoacid = AminoAcidFactory.getAminoAcid(acids.charAt(i));

                if (aminoacid != null) {
                    aminoacids[i] = aminoacid;
                } else {
                    throw new IllegalArgumentException(
                        "Amino-acid chain must contain only amino-acid symbols.");
                }
            }
        } else {
            throw new IllegalArgumentException(
                "The Protein constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AminoAcid[] getAminoAcids() {
        return aminoacids;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Protein clone() {
        Protein protein;

        protein = new Protein(getAminoAcids());

        //protein = (Protein) super.clone();
        //protein.aminoacids = getAminoAcids();
        return protein;
    }
}
