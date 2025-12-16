package org.jscience.bibliography;

/**
 * Represents a standardization body or publication (e.g., CODATA, IUPAC).
 */
public interface Standard {
    /**
     * Returns the name of the standard (e.g., "CODATA").
     */
    String getName();

    /**
     * Returns the version or year of the standard (e.g., "2018").
     */
    String getVersion();

    /**
     * Returns the citation for this standard.
     */
    Citation getCitation();
}
