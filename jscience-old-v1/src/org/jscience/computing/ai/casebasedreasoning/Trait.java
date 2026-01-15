package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class Trait {
    //--- Attributes
    /** DOCUMENT ME! */
    private String name = null;

    /** DOCUMENT ME! */
    private TraitValue value = null;

/**
     * Creates a new Trait object.
     *
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected Trait(String name, String value) {
        setName(name);

        setValue(value);
    } //--- constructor

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
    public TraitValue getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newName DOCUMENT ME!
     */
    protected void setName(String newName) {
        this.name = newName;
    } //--- setName

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    protected void setValue(String newValue) {
        this.value = new TraitValue(newValue);
    } //--- setValue
} //--- Trait
