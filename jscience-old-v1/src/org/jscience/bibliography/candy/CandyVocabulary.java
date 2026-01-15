// CandyVocabulary.java
//
//    senger@ebi.ac.uk
//    February 2001
//
package org.jscience.bibliography.candy;

import org.jscience.util.NestedException;

import java.beans.PropertyChangeListener;
import java.util.Enumeration;

/**
 * <p/>
 * This interface defines functionality of a controlled vocabulary.
 * The implementation is supposed to behave as a Java bean
 * (regarding accessing vocabulary properties).
 * </p>
 * <p/>
 * <p/>
 * Each vocabulary consists of (usually many) vocabulary entries
 * which are represented by {@link CandyEntry CandyEntries}.
 * </p>
 *
 * @author Martin Senger
 * @author Matthew Pocock
 * @version $Id: CandyVocabulary.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public interface CandyVocabulary extends PropertyChangeListener {
    /*************************************************************************
     *
     *    P r o p e r t i e s
     *
     *************************************************************************/

    //
    // Property names
    //

    /**
     * A property name. Its value is a name of this vocabulary.
     */
    static final String PROP_VOCAB_NAME = "vocab_name";

    /**
     * A property name. Its value is a short description of the whole vocabulary.
     */
    static final String PROP_VOCAB_DESC = "vocab_description";

    /**
     * A property name. Its value contains a version of this vocabulary.
     */
    static final String PROP_VOCAB_VERSION = "vocab_version";

    /**
     * A property name. Its boolean value is <tt>true</tt>
     * if the vocabulary entries names should be considered as case-sensitive.
     */
    static final String PROP_CASE_SENSITIVE = "vocab_case_sensitive";

    /**
     * A property name. Its value is a number of vocabulary entries
     * in this vocabulary.
     */
    static final String PROP_ENTRY_COUNT = "entry_count";

    /**
     * A property name. Its type is {@link CandyVocabulary} and
     * it can be used to set an entire vocabulary.
     * An implementation may use it together with an empty
     * constructor.
     */
    static final String PROP_VOCABULARY = "vocabulary";

    /**
     * <p/>
     * A property name.
     * </p>
     * <p/>
     * <p/>
     * An implementation may use this boolean property to make sure that
     * returned vocabulary entries are in the same order as they were
     * read from its original source.
     * </p>
     */
    static final String CANDIES_NOT_SORTED = "candies_not_sorted";

    /**
     * **********************************************************************
     * It checks if a given entry exists in this vocabulary.
     *
     * @param name of a vocabulary entry to be checked
     * @return true if the given entry exists in this vocabulary
     * @throws NestedException if the vocabulary is suddenly not available
     *                         ***********************************************************************
     */
    boolean contains(String name) throws NestedException;

    /**
     * **********************************************************************
     * It returns a selected vocabulary entry.
     *
     * @param name a name of a vocabulary entry to be looked up
     * @return a vocabulary entry
     * @throws NestedException when the given vocabulary entry does not exist
     *                         ***********************************************************************
     * @see #getAllEntries getAllEntries
     */
    CandyEntry getEntryByName(String name) throws NestedException;

    /**
     * **********************************************************************
     * It returns all available vocabulary entries.
     *
     * @return an Enumeration object containing all available entries
     * @throws NestedException if the vocabulary is suddenly not available
     *                         ***********************************************************************
     * @see #getEntryByName getEntryByName
     */
    Enumeration getAllEntries() throws NestedException;

    /**
     * **********************************************************************
     * It return all names (entry identifiers) available in this vocabulary.
     *
     * @return an Enumeration object containing all available names
     * @throws NestedException if the vocabulary is suddenly not available
     *                         ***********************************************************************
     */
    Enumeration getAllNames() throws NestedException;

    /**
     * **********************************************************************
     * It frees all resources related to this vocabulary.
     *
     * @throws NestedException if the vocabulary is suddenly not available
     *                         ***********************************************************************
     */
    void destroy() throws NestedException;

    //
    // Property access methods
    //

    /**
     * It returns a name of this vocabulary.
     * The name should be unique within a {@link CandyFinder}
     * instance who delivers this vocabulary.
     */
    String getName() throws NestedException;

    /**
     * It returns a description of this vocabulary.
     */
    String getDescription() throws NestedException;

    /**
     * It returns a vesrion of this vocabulary.
     */
    String getVersion() throws NestedException;

    /**
     * It returns a number of entries contained in this vocabulary.
     */
    int getCount() throws NestedException;

    /**
     * It returns <tt>true</tt> if the vocabulary entries should
     * be considered as case-sensitive.
     */
    boolean isCaseSensitive() throws NestedException;
}
