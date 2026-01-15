package org.jscience.biology.molecules.proteins;

import org.jscience.biology.Protein;


/**
 * A class representing the human Insulin (precursor) molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://us.expasy.org/cgi-bin/niceprot.pl?P01308
//INS_HUMAN
//P01308
//Insulin [Precursor]
//Insulin is a hormone secreted by cells in the pancreas. The major function of this hormone is to allow glucose to enter the body cells in order to be burned as fuel. If the body ceases to produce this hormone the result is a serious disease known as diabetes.
//CAS Number 9004-10-8
public final class Insulin extends Protein {
    //formula should be C257 H383 N65 O77 S6
    /**
     * Creates a new Insulin object.
     */
    public Insulin() {
        super(
            "MALWMRLLPLLALLALWGPDPAAAFVNQHLCGSHLVEALYLVCGERGFFYTPKTRREAEDLQVGQVELGGGPGAGSLQPLALEGSLQKRGIVEQCCTSICSLYQLENYCN");
    }
}
