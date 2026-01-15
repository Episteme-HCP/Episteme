package org.jscience.biology.taxonomy;

import java.util.Vector;

/**
 * An abstract implementation of Taxon.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

/**
 * <p>An abstract implementation of Taxon.</p>
 * <p/>
 * <p>It is left up to the impementor to provide methods for accessing the
 * parent and children. All other state is provided for here. A common pattern
 * would be to route any Taxon.getParent() call back via a method on the
 * TaxonFactory used to generate this instance.</p>
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public abstract class AbstractTaxon extends Object implements Taxon {
    private String commonName;
    private String scientificName;
    private double apparition;
    private double disparition;

    //accepts null instances
    public AbstractTaxon(String scientificName, String commonName) {
        if ((scientificName != null) && (scientificName.length() > 0) &&
                (commonName != null) && (commonName.length() > 0)) {
            this.scientificName = scientificName;
            this.commonName = commonName;
            this.apparition = 0;
            this.disparition = 0;
        } else {
            throw new IllegalArgumentException("The AbstractTaxon constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param commonName DOCUMENT ME!
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScientificName() {
        return scientificName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param scientificName DOCUMENT ME!
     */
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAppearance() {
        return apparition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param apparition DOCUMENT ME!
     */
    public void setAppearance(double apparition) {
        this.apparition = apparition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getExtinction() {
        return disparition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param disparition DOCUMENT ME!
     */
    public void setExtinction(double disparition) {
        this.disparition = disparition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getExtendedName() {
        Taxon element;
        Vector result;

        result = new Vector();
        element = this;

        while (element.getParent() != null) {
            element = element.getParent();
            result.insertElementAt(element.getScientificName(), 0);
        }

        result.insertElementAt(this.getScientificName(), 0);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Taxon t;

        if ((o != null) && (o instanceof Taxon)) {
            t = (Taxon) o;

            return (this == t) ||
                    (safeEq(this.getScientificName(), t.getScientificName()) &&
                            safeEq(this.getCommonName(), t.getCommonName()) &&
                            safeEq(this.getChildren(), t.getChildren()));
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        Taxon parent = getParent();
        String scientificName = getScientificName();

        if (parent != null) {
            return parent.toString() + " -> " + scientificName;
        } else {
            return scientificName;
        }
    }

    private boolean safeEq(Object a, Object b) {
        if ((a == null) && (b == null)) {
            return true;
        } else if ((a == null) || (b == null)) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}
