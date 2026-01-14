/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.biology.taxonomy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

//code from http://knb.ecoinformatics.org/software/

/**
 * Provides ITIS query
 * and lookup services for taxonomic information.  This includes the ability
 * to search the ITIS*ca database for a string match to a taxon name, which
 * returns the ITIS Taxonomic Serial Number (TSN) for that taxon.  One can
 * then use the TSN to query ITIS*ca and find the scientific name,
 * rank, author, synonym taxa, parent taxa, and children taxa
 * (e.g., species within a genus), among other information. See the
 * ItisTaxon class for more on the information that can be retrieved.
 * See: "http://www.cbif.gc.ca/pls/itisca/" for details on the ITIS database.
 * <p/>
 * <p>Here is a brief example of use of the programmatic API:</p>
 * <p><pre><code>
 * String searchPhrase = "Psychotria nervosa";
 * try {
 *   Itis itis = new Itis();
 *   long newTsn = itis.findTaxonTsn(searchPhrase);
 *   ItisTaxon newTaxon = itis.getTaxon(newTsn);
 *   System.out.println("            TSN: " + newTaxon.getTsn());
 *   System.out.println("Scientific Name: " + newTaxon.getScientificName());
 *   System.out.println("           Rank: " + newTaxon.getTaxonRank());
 *   long parentTsn = newTaxon.getParentTsn();
 *   ItisTaxon parentTaxon = itis.getTaxon(parentTsn);
 *   System.out.println("     Parent TSN: " + parentTaxon.getTsn());
 *   System.out.println("Scientific Name: " + parentTaxon.getScientificName());
 *   System.out.println("           Rank: " + parentTaxon.getTaxonRank());
 * <p/>
 *   Vector synonyms = itis.getSynonymTsnList(newTsn);
 *   for (int i=0; i < synonyms.size(); i++) {
 *     long synonymTsn = ((Long)synonyms.get(i)).longValue();
 *     ItisTaxon synonymTaxon = itis.getTaxon(synonymTsn);
 *     System.out.println("      Synonym TSN: " + synonymTaxon.getTsn());
 *     System.out.println("  Scientific Name: " +
 *                                      synonymTaxon.getScientificName());
 *     System.out.println("             Rank: " + synonymTaxon.getTaxonRank());
 *   }
 * <p/>
 *   Vector children = itis.getChildTsnList(newTsn);
 *   for (int i=0; i < children.size(); i++) {
 *     long childTsn = ((Long)children.get(i)).longValue();
 *     ItisTaxon childTaxon = itis.getTaxon(childTsn);
 *     System.out.println("        Child TSN: " + childTaxon.getTsn());
 *     System.out.println("  Scientific Name: " +
 *                                      childTaxon.getScientificName());
 *     System.out.println("             Rank: " + childTaxon.getTaxonRank());
 *   }
 * } catch (Exception ie) {
 *   System.out.println( ie.getMessage());
 * }
 * </code></pre></p>
 */
public class ItisSupport extends Object {
    /**
     * The base URL for Itis queries
     */
    private String itisBase;
    /**
     * String constant used to construct ITIS search query
     */
    private String itisSearchStub;
    /**
     * String constant used to construct ITIS tsn query
     */
    private String itisTaxaStub;
    /**
     * String constant used to construct ITIS tsn query
     */
    private String itisChildStub;
    /**
     * String constant used to construct ITIS tsn query
     */
    private String itisParentStub;
    /**
     * A cache of previously discovered taxa, indexed by TSN
     */
    private Hashtable tsnCache;
    /**
     * A cache of previously discovered taxa, indexed by scientificName
     */
    private Hashtable nameCache;

    /**
     * constructor
     */
    public ItisSupport() {
        // retrieve and configuration parameters we need
        loadConfigurationParameters();

        // Initialize the cache of taxa already found from Itis
        tsnCache = new Hashtable();
        nameCache = new Hashtable();
    }

    /**
     * Look up a scientific name in ITIS and get its tsn (taxonomic
     * serial number).
     *
     * @param taxonName the name of the taxon to look up
     * @return the integer TSN associated with a taxon
     */
    public long findTaxonTsn(String taxonName) throws Exception {
        long tsn = 0;
        ItisTaxon taxon = null;

        if (nameCache.containsKey(taxonName)) {
            //Utility.debug(9, "Reading data from cache.");
            taxon = (ItisTaxon) nameCache.get(taxonName);
        } else {
            //Utility.debug(9, "Reading data from ITIS.");
            URL itisUrl = null;
            try {
                itisUrl = new URL(itisBase + itisSearchStub + taxonName);
            } catch (MalformedURLException mue) {
                //Utility.debug(1, "URL for ITIS is invalid.");
                throw (new Exception("URL for ITIS is invalid."));
            }

            try {
                InputStreamReader xmlReader =
                        new InputStreamReader(itisUrl.openStream());
                taxon = parseAndCacheTaxa(xmlReader);
            } catch (IOException ioe) {
                //Utility.debug(9, "Error reading from connection to ITIS.");
                throw (new Exception("Error reading from connection to ITIS."));
            }
        }
        if (taxon != null) {
            tsn = taxon.getTsn();
        }
        return tsn;
    }

    /**
     * Retrieve all of the taxonomic details about a particular taxon
     * identified by its taxonomic serial number.
     *
     * @param tsn integer TSN associated with a taxon
     * @return Taxon a detailed data structure describing a taxon
     */
    public ItisTaxon getTaxon(long tsn) throws Exception {
        ItisTaxon taxon = null;

        // First check our local cache and see if we already have it
        Long tsnNumber = new Long(tsn);
        if (tsnCache.containsKey(tsnNumber)) {
            //Utility.debug(9, "Reading data from cache.");
            taxon = (ItisTaxon) tsnCache.get(tsnNumber);
        }

        if (taxon == null || (!taxon.isDataComplete())) {
            // if not, look it up on ITIS
            //Utility.debug(9, "Reading data from ITIS.");
            URL itisUrl = null;
            try {
                itisUrl = new URL(itisBase + itisTaxaStub + tsn);
            } catch (MalformedURLException mue) {
                //Utility.debug(1, "URL for ITIS is invalid.");
                throw (new Exception("URL for ITIS is invalid."));
            }

            try {
                InputStreamReader xmlReader =
                        new InputStreamReader(itisUrl.openStream());
                taxon = parseAndCacheTaxa(xmlReader);

            } catch (IOException ioe) {
                //Utility.debug(9, "Error reading from connection to ITIS.");
                throw (new Exception("Error reading from connection to ITIS."));
            }
        }
        return taxon;
    }

    /**
     * Retrieve the name of a taxon based on its TSN.  This is a Utility method
     * that provides the same info as ItisTaxon.getScientificName().
     *
     * @param tsn integer TSN associated with a taxon
     * @return String the scientific name for this taxon
     */
    public String getScientificName(long tsn) throws Exception {
        String scientificName = null;
        ItisTaxon taxon = getTaxon(tsn);
        if (taxon != null) {
            scientificName = taxon.getScientificName();
        }
        return scientificName;
    }

    /**
     * Get the TSN number for the parent of a taxon which is identified by
     * its own taxonomic serial number.
     *
     * @param tsn integer TSN associated with a taxon
     * @return long the TSN of the parent of this taxa
     */
    public long getParentTsn(long tsn) throws Exception {
        long parentTsn = 0;
        ItisTaxon taxon = getTaxon(tsn);
        if (taxon != null) {
            parentTsn = taxon.getParentTsn();
        }
        return parentTsn;
    }

    /**
     * Get an array of the TSN numbers for children of a particular taxon
     * identified by its taxonomic serial number.
     *
     * @param tsn integer TSN associated with a taxon
     * @return Vector of Long values that represent the children of this taxon
     */
    public Vector getChildTsnList(long tsn) throws Exception {
        ItisTaxon taxon = null;

        // First check our local cache and see if we already have it
        Long tsnNumber = new Long(tsn);
        if (tsnCache.containsKey(tsnNumber)) {
            //Utility.debug(9, "Reading data from cache.");
            taxon = (ItisTaxon) tsnCache.get(tsnNumber);
        }

        if (taxon == null || (!taxon.hasChildList())) {
            //Utility.debug(9, "Reading child data from ITIS.");
            URL itisUrl = null;
            try {
                itisUrl = new URL(itisBase + itisChildStub + tsn);
            } catch (MalformedURLException mue) {
                //Utility.debug(1, "URL for ITIS is invalid.");
                throw (new Exception("URL for ITIS is invalid."));
            }

            try {
                InputStreamReader xmlReader =
                        new InputStreamReader(itisUrl.openStream());
                taxon = parseAndCacheTaxa(xmlReader);

            } catch (IOException ioe) {
                //Utility.debug(9, "Error reading from connection to ITIS.");
                throw (new Exception("Error reading from connection to ITIS."));
            }
        }

        Vector childTsnList = taxon.getChildTsn();
        return childTsnList;
    }

    /**
     * Get an array of the TSN numbers for synonyms of a particular taxon
     * identified by its taxonomic serial number.
     *
     * @param tsn integer TSN associated with a taxon
     * @return Vector of Long values that represent the synonyms of this taxon
     */
    public Vector getSynonymTsnList(long tsn) throws Exception {
        Vector synonymTsnList = null;
        ItisTaxon taxon = getTaxon(tsn);
        if (taxon != null) {
            synonymTsnList = taxon.getSynonymTsn();
        }
        return synonymTsnList;
    }

    /**
     * Parses the XML returned, and caches the taxa locally, and returns
     * the first taxon to the caller
     *
     * @param xmlReader the stream of xml returned from ITIS
     * @return Taxon the first taxon from the ITIS list
     */
    private ItisTaxon parseAndCacheTaxa(Reader xmlReader) throws Exception {
        // Parse the XML returned and get the Taxon list
        ItisXmlHandler itisParser = new ItisXmlHandler();
        Vector taxaList = itisParser.parseTaxa(xmlReader);

        ItisTaxon lastTaxon = null;

        // Store the list of taxa in the cache(s)
        for (int i = 0; i < taxaList.size(); i++) {
            ItisTaxon foundTaxon = (ItisTaxon) taxaList.get(i);
            if (foundTaxon != null) {
                long currentTsn = foundTaxon.getTsn();
                tsnCache.put(new Long(currentTsn), foundTaxon);
                nameCache.put(foundTaxon.getScientificName(), foundTaxon);

                //Utility.debug(20, "Taxa found: " + currentTsn + "(" + i + ")");
                // Remember the tsn of the last taxon so we can return it
                if (i == taxaList.size() - 1) {
                    lastTaxon = foundTaxon;
                }
            }
        }
        return lastTaxon;
    }

    /**
     * Load the configuration parameters that we need
     */
    private void loadConfigurationParameters() {
        itisBase = "http://www.cbif.gc.ca/pls/itisca";
        itisSearchStub = "/taxastep?hierarchy=no&king=every&p_action=exactly+for&p_format=xml&taxa=";
        itisTaxaStub = "/next?p_format=xml&v_tsn=";
        itisChildStub = "/taxa_xml.child?p_type=y&p_tsn=";
        itisParentStub = "/taxa_xml.upwards?p_type=y&p_tsn=";
    }

    /**
     * Main routine for testing
     */
    static public void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Wrong number of arguments!!!");
            System.err.println("USAGE: java Itis <phrase>");
            return;
        } else {
            StringBuffer searchBuffer = new StringBuffer();
            for (int i = 0; i < args.length; i++) {
                searchBuffer.append(args[i]);
                if (i < args.length - 1) {
                    searchBuffer.append("+");
                }
            }
            String searchPhrase = searchBuffer.toString();

            try {
                ItisSupport itis = new ItisSupport();
                //Utility.debug(8, " ");
                //Utility.debug(8, "Searching for: " + searchPhrase);
                long newTsn = itis.findTaxonTsn(searchPhrase);
                //Utility.debug(8, "TSN found is: " + newTsn);
/*
        //Utility.debug(8, " ");
        //Utility.debug(8, "Searching again for: " + searchPhrase);
        newTsn = itis.findTaxonTsn(searchPhrase);
        //Utility.debug(8, "TSN found is: " + newTsn);
*/
                //Utility.debug(8, " ");
                //Utility.debug(8, "Getting taxon...");
                ItisTaxon newTaxon = itis.getTaxon(newTsn);
                //Utility.debug(8,"            TSN: " + newTaxon.getTsn());
                //Utility.debug(8,"Scientific Name: " + newTaxon.getScientificName());
                //Utility.debug(8,"           Rank: " + newTaxon.getTaxonRank());

                long parentTsn = newTaxon.getParentTsn();
                //Utility.debug(8, " ");
                //Utility.debug(8, "Getting parent (" + parentTsn + ") ...");
                ItisTaxon parentTaxon = itis.getTaxon(parentTsn);
                //Utility.debug(8,"            TSN: " + parentTaxon.getTsn());
                //Utility.debug(8,"Scientific Name: " + parentTaxon.getScientificName());
                //Utility.debug(8,"           Rank: " + parentTaxon.getTaxonRank());
/*
        //Utility.debug(8, " ");
        //Utility.debug(8, "Getting parent (" + parentTsn + ") again...");
        parentTaxon = itis.getTaxon(parentTsn);
        //Utility.debug(8,"            TSN: " + parentTaxon.getTsn());
        //Utility.debug(8,"Scientific Name: " + parentTaxon.getScientificName());
        //Utility.debug(8,"           Rank: " + parentTaxon.getTaxonRank());
*/
                //Utility.debug(8, " ");
                //Utility.debug(8, "Getting synonym taxa for " + newTsn + " ...");
                Vector synonyms = itis.getSynonymTsnList(newTsn);
                for (int i = 0; i < synonyms.size(); i++) {
                    long synonymTsn = ((Long) synonyms.get(i)).longValue();
                    //Utility.debug(8, " ");
                    //Utility.debug(8,"      Synonym TSN: " + synonymTsn);
                    ItisTaxon synonymTaxon = itis.getTaxon(synonymTsn);
                    //Utility.debug(8,"              TSN: " + synonymTaxon.getTsn());
                    //Utility.debug(8,"  Scientific Name: " + synonymTaxon.getScientificName());
                    //Utility.debug(8,"             Rank: " + synonymTaxon.getTaxonRank());
                }

                //Utility.debug(8, " ");
                //Utility.debug(8, "Getting child taxa for " + newTsn + " ...");
                Vector children = itis.getChildTsnList(newTsn);
                for (int i = 0; i < children.size(); i++) {
                    long childTsn = ((Long) children.get(i)).longValue();
                    //Utility.debug(8, " ");
                    //Utility.debug(8,"      Child TSN: " + childTsn);
                    ItisTaxon childTaxon = itis.getTaxon(childTsn);
                    //Utility.debug(8,"              TSN: " + childTaxon.getTsn());
                    //Utility.debug(8,"  Scientific Name: " + childTaxon.getScientificName());
                    //Utility.debug(8,"             Rank: " + childTaxon.getTaxonRank());
                }
            } catch (Exception ie) {
                //Utility.debug(8, "Error generated while querying Itis");
                //Utility.debug(8, ie.getMessage());
            }
        }
    }
}
