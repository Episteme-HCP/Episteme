package org.jscience.bibliography;

/**
 * Represents a bibliographic citation.
 */
public interface Citation {
    /**
     * Returns a unique key for this citation (e.g., "CODATA2018").
     */
    String getKey();

    /**
     * Returns the title of the referenced work.
     */
    String getTitle();

    /**
     * Returns the author(s) of the work.
     */
    String getAuthor();

    /**
     * Returns the year of publication.
     */
    String getYear();

    /**
     * Returns the DOI (Digital Object Identifier) if available.
     */
    String getDOI();

    /**
     * Returns a BibTeX representation of this citation.
     */
    String toBibTeX();
}
