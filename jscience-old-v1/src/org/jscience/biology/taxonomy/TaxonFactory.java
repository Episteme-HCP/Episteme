package org.jscience.biology.taxonomy;

import org.jscience.util.CircularReferenceException;

/**
 * Factory for handling a particular implementation of a Taxon.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public interface TaxonFactory {
    /**
     * <p>Name for this TaxonFactory.</p>
     *
     * @return the name of this TaxonFactory
     */
    public String getName();

    /**
     * <p>Import a Taxon and all its children into the implementation
     * provided by this factory.</p>
     * <p/>
     * <p>The return value of this method should be .equals() and
     * .hasCode() compatable with the taxon parameter. It may not be
     * implemented by the same underlying implementation.</p>
     *
     * @param source the Taxon to copy
     * @return a new Taxon
     */
    public Taxon importTaxon(Taxon source);

    /**
     * <p>Retrieve the root upon which all rooted Taxon that this
     * factory knows about are rooted.</p>
     *
     * @return the 'root' Taxon
     */
    public Taxon getRoot();

    /**
     * <p>Retrieve a Taxon that matches some ID.</p>
     * <p/>
     * <p>This method is here out of desperation. It's nasty and should
     * be replaced by some propper querying API. Without having
     * different methods for every TaxonFactory I don't know what to
     * do. All ideas appreciated.</p>
     *
     * @param id the Object identifying a Taxon
     * @return the Taxon matching the ID, or null if none match
     */
    public Taxon search(Object id);

    /**
     * <p>Create a new orphan Taxon with a given scientific and common
     * name.</p>
     *
     * @param scientificName the scientificName to give the Taxon
     * @param commonName     the common name to give the Taxon
     * @return a new Taxon with no parent and no children
     */
    public Taxon createTaxon(String scientificName, String commonName);

    /**
     * <p>Add a taxon as a child to a parent.</p>
     * <p/>
     * <p>The TaxonFactory may chose to add the child directly, or make
     * a new object which is .equals() compatable with child. The actual
     * Taxon instance inserted into the child set is returned by the add
     * method.</p>
     *
     * @param parent the parent Taxon to add the child to
     * @param child  the Taxon to add as a child
     * @return the Taxon object actualy present as the child
     * @throws CircularReferenceException if child is this Taxon or any
     *                                    of its parents
     */
    public Taxon addChild(Taxon parent, Taxon child)
            throws CircularReferenceException;

    /**
     * <p>Remove a Taxon as a child to this one.</p>
     * <p/>
     * <p>This Taxon should attempt to remove a child that is .equals()
     * compatable with child. If it is sucessful, it should return the
     * Taxon instance that was removed. If not, it should return
     * null.</p>
     *
     * @param parent the parent Taxon to remove the child from
     * @param child  the Taxon to remove as a child
     * @return the actual Taxon removed, or null if none were removed
     */
    public Taxon removeChild(Taxon parent, Taxon child);
}
