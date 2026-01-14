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
