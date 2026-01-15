/*
 * SCFType.java
 *
 * Created on August 5, 2004, 10:58 PM
 */
package org.jscience.chemistry.quantum;

/**
 * SCF Type enumeration.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class SCFType {
    /** The Hartree Fock method */
    public static final SCFType HARTREE_FOCK = new SCFType(1);

    /** The Moller Plesset method */
    public static final SCFType MOLLER_PLESSET = new SCFType(2);

    /** Holds value of property type. */
    private int type;

/**
     * Creates a new instance of SCFType
     *
     * @param type DOCUMENT ME!
     */
    private SCFType(int type) {
        this.type = type;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Returns a description of the SCF type
     *
     * @return a string indicating SCF type
     */
    public String toString() {
        String description = "";

        if (this.equals(HARTREE_FOCK)) {
            description = "Hartree Fock Method";
        } else if (this.equals(MOLLER_PLESSET)) {
            description = "Moller Plesset Method";
        } else {
            description = "No description available";
        } // end if

        return description;
    }

    /**
     * The method to check the equality of two objects of SCFType
     * class.
     *
     * @param obj DOCUMENT ME!
     *
     * @return true/ false specifying the equality or inequality
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return ((obj != null) && (obj instanceof SCFType) &&
        (this.type == ((SCFType) obj).type));
    }
} // end of class SCFType
