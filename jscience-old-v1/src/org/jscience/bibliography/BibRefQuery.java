// BibRefQuery.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * An interface defining functionality of a BibRefCollection (which is a main
 * entry point to the queryied bibliographic repositories).
 * <p/>
 * <p/>
 * A BibRefQuery represents a collection of {@link BibRef Bibliographic
 * references}, and can be used for refining the collection by asking queries,
 * and/or to retrive the contents of the collection.
 * </p>
 * <p/>
 * <p/>
 * <u>The implementations are advised to used the following constructor:</u>
 * <pre>
 *    public <em>NameOfAnImplementation</em> (String[] args, Hashtable props) {...}
 * </pre>
 * where both <tt>args</tt> and <tt>props</tt> contain implementation specific
 * parameters and properties. However, some properties are more probable to be
 * used - the suggested names for them are defined either in this interface or
 * in the "sister" interface {@link BibRefSupport}.
 * </p>
 * <p/>
 * <P></p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BibRefQuery.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 * @see BibRef
 */
public interface BibRefQuery {
    /**
     * A property name ("<b>excluded</b>") specifyies a list of attribute
     * names. The list is used to define attributes which are not returned in
     * the resulting citations. The type is <tt>String[]</tt>.
     *
     * @see #find
     * @see #query
     */
    static final String PROP_EXCLUDED_ATTRS = "excluded";

    /**
     * A property name ("<b>criterions</b>") specifying a list of searching and
     * ordering criteria names. The type is <tt>String[]</tt>.
     *
     * @see #find
     * @see #query
     */
    static final String PROP_CRITERIONS = "criterions";

    /**
     * Return an identification of the current collection.
     * <p/>
     * <p/>
     * At the beginning, the identification usually contain a bibliographic
     * repository name or its contents description. But later, usually after
     * {@link #connect} or after the first query, the identification may
     * contain information rich enough to be able to re-create the whole
     * collection (e.g. it can contain IDs of all records in the given
     * collection).
     * </p>
     * <p/>
     * <p/>
     * An implementation is not required to provide a persistent collection
     * identification. However, if it does provide, it should also be able to
     * accept the same identifier in the {@link #connect} method, and to use
     * it to re-create the same collection.
     * </p>
     * <p/>
     * <P></p>
     *
     * @return an identification of the current collection (may be null)
     */
    byte[] getCollectionId();

    /**
     * Make a connection to a repository, or/and make initialization steps
     * needed for further communication.
     * <p/>
     * <p/>
     * However, there should be no need to call this explicitly, the other
     * methods should do it automatically when they need something from the
     * repository.
     * </p>
     * <p/>
     * <P></p>
     *
     * @throws BQSException if the connection cannot be established
     */
    void connect() throws BQSException;

    /**
     * Make a connection to a repository, or/and make initialization steps
     * needed for further communication, and make the collection described by
     * 'collectionId' the current collection.
     * <p/>
     * <P></p>
     *
     * @param collectionId a (usually persistent) token allowing to re-create a
     *                     collection; the parameter is the same as an identifier returned
     *                     earlier by method {@link #getCollectionId}
     * @throws BQSException if the connection cannot be established, or if the
     *                      collection with the given ID cannot be re-created.
     */
    void connect(byte[] collectionId) throws BQSException;

    /**
     * Check if the repository is available. The semantic of
     * <em>available</em>depends on the implementation.
     *
     * @return DOCUMENT ME!
     */
    boolean isReady();

    /**
     * Disconnect from the repository. Implementations may choose to use this
     * method for freeing resources needed to acces the repository.
     */
    void disconnect();

    /**
     * Query the current collection and return another collection which is a
     * subset of the current collection containing results of the specified
     * query. Search for the keywords in the specified attributes - try to
     * find citations having all the keywords somewhere in the given
     * attributes.
     * <p/>
     * <p/>
     * The query result can be influenced by the additional properties:
     * <p/>
     * <ul>
     * <li>
     * Property {@link #PROP_EXCLUDED_ATTRS} is of type <tt>String[]</tt> and
     * contains list of attributes names which should be ignored and not
     * included in the results.
     * </li>
     * <li>
     * Property {@link #PROP_CRITERIONS} is also of type <tt>String[]</tt> and
     * contains list of criteria names. The caller specifies here what
     * criteria she wishes, and this method can change this property and
     * return here the criteria really used for the query.
     * </li>
     * </ul>
     * </p>
     * <p/>
     * <P></p>
     *
     * @param keywords   keyword or phrases that are being looked for
     * @param attrs      attributes names that should be searched
     * @param properties specify attributes excluded from the results and
     *                   requested criteria for the query
     * @return a new queryable collection
     * @throws BQSException if query failed (which can have many reasons :-))
     * @see #query
     */
    BibRefQuery find(String[] keywords, String[] attrs, Hashtable properties)
            throws BQSException;

    /**
     * Query the current collection and return another collection which is a
     * subset of the current collection containing results of the specified
     * query. For searching use a specific query language which is not defined
     * by this interface but must be defined by the implementation.
     * <p/>
     * <p/>
     * The query result can be influenced by the additional properties:
     * <p/>
     * <ul>
     * <li>
     * Property {@link #PROP_EXCLUDED_ATTRS} is of type <tt>String[]</tt> and
     * contains list of attributes names which should be ignored and not
     * included in the results.
     * </li>
     * <li>
     * Property {@link #PROP_CRITERIONS} is also of type <tt>String[]</tt> and
     * contains list of criteria names. The caller specifies here what
     * criteria she wishes, and this method can change this property and
     * return here the criteria really used for the query.
     * </li>
     * </ul>
     * </p>
     * <p/>
     * <P></p>
     *
     * @param query      an expression in a query language
     * @param properties specify attributes excluded from the results and
     *                   requested criteria for the query
     * @return a new queryable collection
     * @throws BQSException if query failed (which can have many reasons :-))
     * @see #find
     */
    BibRefQuery query(String query, Hashtable properties)
            throws BQSException;

    /**
     * Query the current collection and return another collection which is a
     * subset of the current collection containing results of the specified
     * query. Search only for non-empty attributes in the given author.
     * <p/>
     * <p/>
     * The query result can be influenced by the additional properties:
     * <p/>
     * <ul>
     * <li>
     * Property {@link #PROP_EXCLUDED_ATTRS} is of type <tt>String[]</tt> and
     * contains list of attributes names which should be ignored and not
     * included in the results.
     * </li>
     * <li>
     * Property {@link #PROP_CRITERIONS} is also of type <tt>String[]</tt> and
     * contains list of criteria names. The caller specifies here what
     * criteria she wishes, and this method can change this property and
     * return here the criteria really used for the query.
     * </li>
     * </ul>
     * </p>
     * <p/>
     * <P></p>
     *
     * @param author     specifies one or more attributes that are being search
     *                   for; for example, a search for citations written by authors with
     *                   surname "Doe" will use 'author' object filled only with
     *                   attribute 'surname'
     * @param properties specify attributes excluded from the results and
     *                   requested criteria for the query
     * @return a new queryable collection
     * @throws BQSException if query failed (which can have many reasons :-))
     * @see #find
     */
    BibRefQuery findByAuthor(BiblioProvider author, Hashtable properties)
            throws BQSException;

    /**
     * Query the current collection to find a citation with the given ID.
     * <p/>
     * <P></p>
     *
     * @param bibRefId an identifier of a citation that is being looked for
     * @return a found bibliographic reference (citation)
     * @throws BQSException if such citation was not found (or something else
     *                      bad happened)
     * @see #findById(String,String[])
     */
    BibRef findById(String bibRefId) throws BQSException;

    /**
     * Query the current collection to find a citation with the given ID.
     * <p/>
     * <p/>
     * The returned citation will contain at least attributes whose names are
     * specified by the parameter <tt>onlyAttrs</tt>. It is meant to provide
     * more lightweight citation. The implementation may provide more
     * attributes than specified in <tt>onlyAttrs</tt> (e.g. it may be always
     * good to include an attribute representing a unique identifier of this
     * citation even if it is not asked for).
     * </p>
     * <p/>
     * <p/>
     * Note that one can ask only for attributes that are available in the
     * current collection. If the collection was already created
     * <em>without</em> some attributes (using property {@link
     * #PROP_EXCLUDED_ATTRS}, e.g in method {@link #find}) one cannot expect
     * to get them even if they are asked for by the parameter
     * <tt>onlyAttrs</tt>.
     * </p>
     * <p/>
     * <P></p>
     *
     * @param bibRefId  an identifier of a citation that is being looked for
     * @param onlyAttrs DOCUMENT ME!
     * @return a found bibliographic reference (citation)
     * @throws BQSException if such citation was not found (or something else
     *                      bad happened)
     * @see #findById(String)
     */
    BibRef findById(String bibRefId, String[] onlyAttrs)
            throws BQSException;

    /**
     * Query the current collection to find a citation with the given ID, and
     * return it in an XML format.
     * <p/>
     * <P></p>
     *
     * @param bibRefId an identifier of a citation that is being looked for
     * @return a found bibliographic reference (citation)
     * @throws BQSException if such citation was not found (or something else
     *                      bad happened)
     */
    String findByIdAsXML(String bibRefId) throws BQSException;

    /**
     * Return the number of citation in the current collection.
     * <p/>
     * <P></p>
     *
     * @return the size of this collection
     * @throws BQSException if a connection with the repository is broken
     */
    int getBibRefCount() throws BQSException;

    /**
     * Sort the current collection and return another collection which is a
     * sorted copy of the current collection.
     * <p/>
     * <p/>
     * The sorting result can be influenced by an additional property {@link
     * #PROP_CRITERIONS} (of type <tt>String[]</tt>) containing a list of
     * sorting criteria names. The caller specifies here what criteria she
     * wishes, and this method can change this property and return here the
     * criteria really used for sorting.
     * </p>
     * <p/>
     * <P></p>
     *
     * @param orderedBy  a list of attributenames that the collection should be
     *                   sorted by
     * @param properties DOCUMENT ME!
     * @return a sorted collection
     * @throws BQSException if sorting failed (which may also happen when the
     *                      collection is too large)
     */
    BibRefQuery sort(String[] orderedBy, Hashtable properties)
            throws BQSException;

    /**
     * Return all citations from the current collection. Some attributes may be
     * missing (empty) if the property {@link #PROP_EXCLUDED_ATTRS} was used
     * for creating the current collection.
     * <p/>
     * <P></p>
     *
     * @return all citations
     * @throws BQSException if the collection is too large, or if the
     *                      connection to the repository is broken
     * @see #getAllBibRefs(String[])
     * @see #find
     * @see #query
     * @see #findByAuthor
     */
    BibRef[] getAllBibRefs() throws BQSException;

    /**
     * Return all citations from the current collection. The returned citations
     * will contain at least attributes whose names are specified by the
     * parameter <tt>onlyAttrs</tt>. It is meant to provide more lightweight
     * citations. The implementation may provide more attributes than
     * specified in <tt>onlyAttrs</tt> (e.g. it may be always good to include
     * an attribute representing a unique identifier of a citation even if it
     * is not asked for).
     * <p/>
     * <p/>
     * Note that one can ask only for attributes that are available in the
     * current collection. If the collection was already created
     * <em>without</em> some attributes (using property {@link
     * #PROP_EXCLUDED_ATTRS}, e.g in method {@link #find}) one cannot expect
     * to get them even if they are asked for by the parameter
     * <tt>onlyAttrs</tt>.
     * </p>
     * <p/>
     * <P></p>
     *
     * @param onlyAttrs DOCUMENT ME!
     * @return all citations
     * @throws BQSException if the collection is too large, or if the
     *                      connection to the repository is broken
     * @see #getAllBibRefs
     * @see #query
     * @see #findByAuthor
     */
    BibRef[] getAllBibRefs(String[] onlyAttrs) throws BQSException;

    /**
     * Return only IDs of all citations in the current collection.
     * <p/>
     * <P></p>
     *
     * @return a list of all identifiers
     * @throws BQSException if the collection is too large, or if the
     *                      connection to the repository is broken
     */
    String[] getAllIDs() throws BQSException;

    /**
     * Return an enumeration of all citations from the current collection. The
     * type of elements in the enumeration is <tt>BibRef</tt>. Some attributes
     * may be missing (empty) if the property {@link #PROP_EXCLUDED_ATTRS} was
     * used for creating the current collection.
     * <p/>
     * <P></p>
     *
     * @return an iterator over all citations
     * @throws BQSException if the connection to the repository is broken
     * @see #getAllBibRefs
     * @see #getBibRefs(String[])
     */
    Enumeration getBibRefs() throws BQSException;

    /**
     * Return an enumeration of all citations from the current collection. The
     * type of elements in the enumeration is <tt>BibRef</tt>.
     * <p/>
     * <p/>
     * The citations available through the enumeration will contain at least
     * attributes whose names are specified by the parameter
     * <tt>onlyAttrs</tt>. It is meant to provide more lightweight citations.
     * The implementation may provide more attributes than specified in
     * <tt>onlyAttrs</tt> (e.g. it may be always good to include an attribute
     * representing a unique identifier of a citation even if it is not asked
     * for).
     * </p>
     * <p/>
     * <p/>
     * Note that one can ask only for attributes that are available in the
     * current collection. If the collection was already created
     * <em>without</em> some attributes (using property {@link
     * #PROP_EXCLUDED_ATTRS}, e.g in method {@link #find}) one cannot expect
     * to get them even if they are asked for by the parameter
     * <tt>onlyAttrs</tt>.
     * </p>
     * <p/>
     * <P></p>
     *
     * @param onlyAttrs DOCUMENT ME!
     * @return an iterator over all citations
     * @throws BQSException if the connection to the repository is broken
     * @see #getAllBibRefs
     * @see #getBibRefs
     */
    Enumeration getBibRefs(String[] onlyAttrs) throws BQSException;

    /**
     * Return all citations from the current collection as an XML stream. Some
     * attributes may be missing (empty) if the property {@link
     * #PROP_EXCLUDED_ATTRS} was used for creating the current collection.
     * <p/>
     * <P></p>
     *
     * @return an XML data stream containing all citations from the current
     *         collection
     * @throws BQSException if the collection is too large, or if the
     *                      connection to the repository is broken
     * @see #getAllBibRefs
     */
    InputStream getAllBibRefsAsXML() throws BQSException;

    /**
     * Return an enumeration of all citations from the current collection. The
     * type of elements in the enumeration is <tt>String</tt>. Each element
     * represent one citation as an XML string. Some attributes may be missing
     * (empty) if the property {@link #PROP_EXCLUDED_ATTRS} was used for
     * creating the current collection.
     * <p/>
     * <p/>
     * Note that it is not the same as with {@link #getAllBibRefsAsXML} where
     * the return type is an InputStream (and not the String).
     * </p>
     * <p/>
     * <P></p>
     *
     * @return an iterator over all citations
     * @throws BQSException if the connection to the repository is broken
     * @see #getBibRefs
     * @see #getAllBibRefsAsXML
     */
    Enumeration getBibRefsAsXML() throws BQSException;

    /**
     * Return an XML representation of the given citation.  The XML format
     * depends on the repository where the citation comes from.
     * <p/>
     * <P></p>
     *
     * @param bibRef a citation being converted into an XML format
     * @return an XML data about 'bibRef' citation
     * @throws BQSException if the implementation needs it :-)
     */
    String getBibRefAsXML(BibRef bibRef) throws BQSException;

    /**
     * Free all resources related to this collection.
     * <p/>
     * <P></p>
     *
     * @throws BQSException if the connection to the repository is broken
     */
    void destroy() throws BQSException;
}
