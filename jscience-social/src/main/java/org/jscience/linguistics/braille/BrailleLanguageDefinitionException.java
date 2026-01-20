package org.jscience.linguistics.braille;

/**
 * Indicates an error in the creation of a new BrailleLanguageUnicode object.  This
 * is thrown when one of the BrailleLanguageUnicode <CODE>set</CODE> methods is used
 * to add data (character rules, translation rules, state descriptions and so
 * on) to a new BrailleLanguageUnicode (created by <CODE>new BrailleLanguageUnicode()</CODE>
 * but the methods are used with invalid parameters or in the incorrect order.
 * <p/>
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 * @author Alasdair King, alasdairking@yahoo.co.uk
 * @version 1.0 09/01/2001
 */
public class BrailleLanguageDefinitionException extends Exception {
    /**
     * Creates a new default BrailleLanguageDefinitionException
     */
    public BrailleLanguageDefinitionException() {
        super();
    }

    /**
     * Creates a new BrailleLanguageDefinitionException with the error text held
     * in <CODE>description</CODE>.
     *
     * @param description <CODE>String</CODE> containing description of error.
     */
    public BrailleLanguageDefinitionException(String description) {
        super(description);
    }
}
