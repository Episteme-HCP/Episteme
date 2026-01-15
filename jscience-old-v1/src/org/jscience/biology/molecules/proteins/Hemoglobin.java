package org.jscience.biology.molecules.proteins;

import org.jscience.biology.Protein;


/**
 * A class representing the human Hemoglobin molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://us.expasy.org/cgi-bin/niceprot.pl?HBA_HUMAN
//HBA_HUMAN
//P01922
//Hemoglobin alpha chain
public final class Hemoglobin extends Protein {
    //formula should be C2952 H4664 N812 O832 S8 Fe4
    /**
     * Creates a new Hemoglobin object.
     */
    public Hemoglobin() {
        super(
            "VLSPADKTNVKAAWGKVGAHAGEYGAEALERMFLSFPTTKTYFPHFDLSHGSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKLRVDPVNFKLLSHCLLVTLAAHLPAEFTPAVHASLDKFLASVSTVLTSKYR");
    }
}
