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

package org.jscience.computing.ai.casebasedreasoning;

/**
 * Now that i think about it, this class seems awfully silly. Shouldn't
 * <p/>
 * the calling program do all these (pretty simple) steps?
 *
 * @author <small>baylor</small>
 */
public class MatchingItemsManager {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private FilterEngine filterEngine = new FilterEngine();
    private QueryManager queryManager = new QueryManager();
    private SimilarityEngine similarityEngine = new SimilarityEngine();
    private Items matchedItems;
    private Items items;
    private SimilarItems similarItems;
    private SimilarityCriteria similarityCriteria;
    private SimilarityWeights similarityWeights;
    private TraitDescriptors traitDescriptors;

    /**
     * Creates a new MatchingItemsManager object.
     */
    public MatchingItemsManager() {
    }

    //-----------------------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------------------
    public Items getFilteredItems() {
        return matchedItems;
    } //--- getFilteredItems

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Items getItems() {
        return items;
    } //--- getItems

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public QueryManager getQueryManager() {
        return queryManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarItems getSimilarItems() {
        //--- Bummer, return has to be outside the try{} block
        return similarItems;
    } //--- getSimilarItems

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityCriteria getSimilarityCriteria() {
        return similarityCriteria;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityWeights getSimilarityWeights() {
        return similarityWeights;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitDescriptors getTraitDescriptors() {
        return traitDescriptors;
    } //--- getTraits

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    public void load() {
        try {
            //--- Is this dumb? It erases all variables and creates
            //---   new ones. What if this object is kept in memory
            //---   and lots of people/threads call it? Don't want it
            //---   to hose any class-level variables
            //--- Solution is to make all these local variables
            //---   but that makes the code so messy
            //--- Oh well, ignore for now since this is just
            //---   prototype code and better be cleaned up
            //---   before anyone uses this for something real
            ItemManager itemManager = new ItemManager();

            items = itemManager.getItems();

            traitDescriptors = itemManager.getTraitDescriptors();

            FilterCriteria filterCriteria = queryManager.getFilterCriteria();

            similarityCriteria = queryManager.getSimilarityCriteria();

            similarityWeights = queryManager.getSimilarityWeights();

            //--- Filter out the stuff we know we'd never want
            //--- This is basically like the WHERE clause in a SQL statement
            matchedItems = filterEngine.filterItems(items, filterCriteria);

            DataSetStatistics stats = new DataSetStatistics(items);

            //--- This is where we do that whole nearest neighbor thing
            //--- We'll get back a list of items with scores of how similar
            //---   each item is (%) to what we asked for
            similarItems = similarityEngine.computeSimilarity(matchedItems,
                    similarityCriteria, similarityWeights, stats);
        } //--- try
        catch (Exception e) {
            System.out.println("Error: " + e);
        } //--- catch
    } //--- load
} //--- MatchingItemsManager
