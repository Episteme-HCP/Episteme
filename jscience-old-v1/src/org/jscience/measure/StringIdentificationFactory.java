package org.jscience.measure;

/**
 * A class representing a basic identification system as String
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class StringIdentificationFactory extends Object
    implements IdentificationFactory {
    //should be different each time
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification generateIdentification() {
        return new StringIdentification(Double.toString(Math.random()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification generateIdentification(String identification) {
        return new StringIdentification(identification);
    }

    //given an identification in a scheme, generates the corresponding identification in the corresponding scheme
    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification convertIdentification(Identification identification) {
        if (identification != null) {
            if (identification instanceof StringIdentification) {
                return identification;
            } else {
                return new StringIdentification(identification.toString());
            }
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    private class StringIdentification extends Identification {
        /** DOCUMENT ME! */
        String identification;

/**
         * Creates a new StringIdentification object.
         *
         * @param identification DOCUMENT ME!
         */
        public StringIdentification(String identification) {
            if ((identification != null) && (identification.length() > 0)) {
                this.identification = identification;
            } else {
                throw new IllegalArgumentException(
                    "The StringIdentification constructor can't have null or empty arguments.");
            }
        }

        //a String that describes the common name for that identification scheme
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getDescription() {
            return new String("Generic String Identification System");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return identification;
        }

        /**
         * DOCUMENT ME!
         *
         * @param object DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object object) {
            StringIdentification stringIdentification;

            if ((object != null) && (object instanceof StringIdentification)) {
                stringIdentification = (StringIdentification) object;

                return stringIdentification.toString()
                                           .equals(identification.toString());
            } else {
                return false;
            }
        }
    }
}
