package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class TraitValue {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private String value;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new TraitValue object.
     *
     * @param value DOCUMENT ME!
     */
    public TraitValue(String value) {
        this.value = value;
    }

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int toInteger() {
        Integer integerValue = new Integer(value);

        return integerValue.intValue();
    } //--- toInteger

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean toBoolean() {
        Boolean booleanValue = new Boolean(value);

        return booleanValue.booleanValue();
    } //--- toBoolean

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float toFloat() {
        Float floatValue = new Float(value);

        return floatValue.floatValue();
    } //--- toFloat

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value;
    } //--- toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String value() {
        return toString();
    }
} //--- TraitValue
