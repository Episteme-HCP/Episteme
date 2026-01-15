package org.jscience.tests.computing.ai.cbr;

import org.jscience.computing.ai.cbr.*;

/**
 * This is the main routine. It takes items that matched a given target
 * <p/>
 * and displays the results to stdout. It's pretty crude and is really just a
 * <p/>
 * harness for testing the other routines. It's good for running
 * <p/>
 * tests, but you'd never use this class in the real world
 *
 * @author <small>baylor</small>
 */

public class DisplaySelectedItemsManager {

    protected MatchingItemsManager matchingItemsManager = new MatchingItemsManager();

    protected DisplayManager displayManager = new DisplayManager();

    /**

     *

     */

    public void start() {

        try {

            matchingItemsManager.load();

            TraitDescriptors traitDescriptors = matchingItemsManager.getTraitDescriptors();

            Items items = matchingItemsManager.getItems();

            SimilarItems similarItems = matchingItemsManager.getSimilarItems();

            displayManager.displayTraitDescriptors(traitDescriptors);

            displayManager.displayItems(traitDescriptors, items);

            displayManager.displaySimilarItems(traitDescriptors, similarItems);

        } catch (Exception e) {

            System.out.println("Error: " + e);

        }

    }

}  //--- DisplaySelectedItemsManager
