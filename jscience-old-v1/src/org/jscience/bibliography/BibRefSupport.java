// BibRefSupport.java
//
//    senger@ebi.ac.uk
//    April 2001
//
package org.jscience.bibliography;

import embl.ebi.CandyShare.CandyFinder;
import embl.ebi.CandyShare.CandyVocabulary;

import java.util.Hashtable;

/**
 * An interface defining supporting utilities for working with
 * bibliographic repositories, mainly how to access controlled vocabularies.
 * It is modelled according to the BQS CORBA interface <tt>BibRefUtilities</tt>.
 * <p/>
 * <u>The implementations are advised to used the following constructor:</u>
 * <pre>
 *    public NameOfAnImplementation (String[] args, Hashtable props) {...}
 * </pre>
 * where both <tt>args</tt> and <tt>props</tt> contain implementation
 * specific parameters and properties. However, some properties are
 * more probable to be used - the suggested names for them are defined
 * also in this interface (e.g. {@link #INIT_PROP_LOG}).
 * <p/>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BibRefSupport.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public interface BibRefSupport {
    //
    // names for global vocabulary names
    //
    static final String RESOURCE_TYPES = "resource_types";
    static final String REPOSITORY_SUBSETS = "repository_subsets";
    static final String SUBJECT_HEADINGS = "subject_headings";
    static final String LANGUAGES = "languages";
    static final String JOURNAL_TITLES = "journal_titles";
    static final String JOURNAL_ABBREV = "journal_abbreviations";
    static final String ENTRY_PROPERTIES = "entry_properties";

    //
    // names for (some) bibliographic resource types
    //
    static final String TYPE_BOOK = "Book";
    static final String TYPE_ARTICLE = "Article";
    static final String TYPE_BOOK_ARTICLE = "BookArticle";
    static final String TYPE_JOURNAL_ARTICLE = "JournalArticle";
    static final String TYPE_PATENT = "Patent";
    static final String TYPE_THESIS = "Thesis";
    static final String TYPE_PROCEEDING = "Proceeding";
    static final String TYPE_TECH_REPORT = "TechReport";
    static final String TYPE_WEB_RESOURCE = "WebResource";

    //
    // names for (some) other corners of a bibliographic repository
    //
    static final String PROVIDER_PERSON = "Person";
    static final String PROVIDER_ORGANISATION = "Organisation";
    static final String PROVIDER_SERVICE = "Service";
    static final String GENERIC_PROVIDER = "Provider";

    //
    // names for (some) attribute names
    //
    static final String ATTR_PROPERTIES = "properties";
    static final String ATTR_SCOPE = "scope";
    static final String ATTR_FORMAT = "format";

    //
    // names characterizing attributes
    //
    static final String ROLE_ATTR_QUERYABLE = "queryable";
    static final String ROLE_ATTR_RETRIEVABLE = "retrievable";

    /**
     * A property name ("<b>log</b>").
     * Used by various classes to pass an instance of a
     * <a href="http://industry.ebi.ac.uk/~senger/tools/API/embl/ebi/tools/Log.html">Log</a> class.
     */
    static final String INIT_PROP_LOG = "log";

    /**
     * A property name ("<b>bibrefsupport</b>").
     * Used by various classes to pass an instance of a class implementing this
     * interface.
     */
    static final String INIT_PROP_SUPPORT = "bibrefsupport";

    /**
     * ***********************************************************************
     * Make a connection to an object providing the supporting utilities,
     * or/and make initialization steps needed to further communication.
     * <p/>
     *
     * @throw BQSException if the connection/initialization cannot be established
     * ***********************************************************************
     */
    void connect() throws BQSException;

    /**
     * ***********************************************************************
     * Check if a utility object is available. The semantic of
     * <em>available</em>depends on the implementation.
     * ***********************************************************************
     */
    boolean isReady();

    /**
     * ***********************************************************************
     * Disconnect from the utility object. Implementations may choose to
     * use this method for freeing resources needed to acces the repository.
     * ***********************************************************************
     */
    void disconnect();

    /**
     * ***********************************************************************
     * Return an object representing a central place where all controlled
     * vocabularies can be received from and shared by all users.
     * <p/>
     * The controlled vocabularies are used for finding names of all available
     * attributes of the given bibliographic repository, for finding all
     * possible values of some attributes, and for specifying availability of
     * the ordering and searching criteria.
     * <p/>
     *
     * @return an instance implementing a CandyFinder interface
     * @throws BQSException if the cocabulary finder cannot be found
     *                      ***********************************************************************
     */
    CandyFinder getVocabularyFinder() throws BQSException;

    /**
     * ***********************************************************************
     * Return a controlled vocabulary containing all possible values of the
     * attribute 'attrName' in the context of 'resourceType'. It's up to the
     * implementation to define the context.
     * <p/>
     * Specifically, for 'attrName' equals to {@link #ATTR_PROPERTIES} it
     * returns a vocabulary containing attribute names available for the
     * given citation type (also called 'dynamic properties').
     * <p/>
     *
     * @param resourceType is usually a name of a citation type (e.g. "Book",
     *                     "JournalArticle"), see {@link #TYPE_BOOK}, etc., but can define
     *                     other contexts as well (e.g. "Person")
     * @param attrName     a name of an attribute whose values should be
     *                     available from the returned vocabulary
     * @return a controlled vocabulary
     * @throw BQSException if there is no such vocabulary available, or
     * something else wrong happened
     * ***********************************************************************
     */
    CandyVocabulary getSupportedValues(String resourceType, String attrName)
            throws BQSException;

    /**
     * ***********************************************************************
     * Return all supported searching and sorting criteria in the given
     * repository subset.
     * ***********************************************************************
     */
    BiblioCriterion[] getSupportedCriteria(String repositorySubset)
            throws BQSException;

    /**
     * ***********************************************************************
     * Return all supported searching and sorting criteria for the whole
     * bibliographic repository.
     * ***********************************************************************
     */
    BiblioCriterion[] getSupportedCriteria() throws BQSException;

    /**
     * **********************************************************************
     * Merge all given collections together. The result should eliminate
     * redundancy - which usually means removing the same citations.
     * <p/>
     * The merging process can be influenced by specifying some properties
     * (but they are not defined by this interface, they depend on the
     * implementation).
     * <p/>
     * <em>
     * Note that the merging is independent on the repository, or repositories
     * where the collections come from. The main raison d'etre is actually
     * to allow to merge collections from different repositories. But it
     * opens the question what to do with the resulting collection (how to
     * query it, for example, if it is a "virtual" collection). So it can
     * be quite difficult to implement this method :-(. Perhaps in the future
     * we may decide to move it to {@link BibRefQuery}?
     * <em>
     * <p/>
     *
     * @param collections to be merged together
     * @param properties  define features how to do merging
     * @return a merged collection
     * @throws BQSException if merging failed (which may also happen when
     *                      any of the collection is too large)
     *                      ***********************************************************************
     */
    BibRefQuery union(BibRefQuery[] collections, Hashtable properties)
            throws BQSException;
}
