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

import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DisplayManager {
    /**
     * DOCUMENT ME!
     *
     * @param filterCriteria DOCUMENT ME!
     */
    public void displayFilterCriteria(FilterCriteria filterCriteria) {
        System.out.println("");

        System.out.println("Filter Criteria (" + filterCriteria.size() + ")");

        System.out.println("---------------");

        Iterator cursor = filterCriteria.iterator(); //--- iterator on an ArrayList

        while (cursor.hasNext()) {
            FilterCriterion criterion = (FilterCriterion) cursor.next();

            System.out.print(criterion.getFieldName());

            System.out.print(" ");

            System.out.print(criterion.getOperatorAsString());

            System.out.print(" ");

            System.out.println(criterion.getValue());
        } //--- while hasNext
    } //--- displayFilterCriteria

    /**
     * DOCUMENT ME!
     *
     * @param traitDescriptors DOCUMENT ME!
     * @param items DOCUMENT ME!
     */
    public void displayItems(TraitDescriptors traitDescriptors, Items items) {
        System.out.println("");

        System.out.println("Items (" + items.size() + ")");

        System.out.println("-----");

        Iterator cursor = items.iterator(); //--- iterator on an ArrayList

        //---   filled with items
        //--- While we have more rows of data
        while (cursor.hasNext()) {
            //--- values is a HashMap that might look like
            //---   [string maker][compaq] [string model][proliant] etc.
            Item item = (Item) cursor.next();

            //--- Which trait should we print a value for?
            //--- Let's print all of them out in the same order as they're
            //---   stored in TraitDescriptors
            for (int i = 0; i < traitDescriptors.size(); i++) {
                //--- What trait do we want to see the value for?
                TraitDescriptor traitDescriptor = traitDescriptors.get(i);

                String traitName = traitDescriptor.getName();

                //--- Let's get that trait in the current item
                String value = item.getTraitValue(traitName).value();

                System.out.print(value);

                System.out.print("  ");
            } //--- for i=0 to fields.size

            //--- New line for formatting purposes
            System.out.println("");
        } //--- while hasNext
    } //--- displayItems

    /**
     * DOCUMENT ME!
     *
     * @param traitDescriptors DOCUMENT ME!
     * @param items DOCUMENT ME!
     */
    public void displaySimilarItems(TraitDescriptors traitDescriptors,
        SimilarItems items) {
        try {
            System.out.println("");

            System.out.println("Similar Items (" + items.size() + ")");

            System.out.println("-------------");

            Iterator cursor = items.iterator();

            while (cursor.hasNext()) {
                SimilarityDescription similarityDescription = (SimilarityDescription) cursor.next();

                int percentSimilarity = (int) (100 * similarityDescription.getPercentSimilarity());

                int rank = similarityDescription.getRank();

                Item item = similarityDescription.getItem();

                System.out.print(rank + ". ");

                System.out.print(percentSimilarity + "% ");

                //--- Which trait should we print a value for?
                //--- Let's print all of them out in the same order as they're
                //---   stored in TraitDescriptors
                for (int i = 0; i < traitDescriptors.size(); i++) {
                    //--- What trait do we want to see the value for?
                    TraitDescriptor traitDescriptor = traitDescriptors.get(i);

                    String traitName = traitDescriptor.getName();

                    //--- Let's get that trait in the current item
                    String value = item.getTraitValue(traitName).value();

                    System.out.print(value);

                    System.out.print("  ");
                } //--- for i=0 to fields.size

                //--- New line for formatting purposes
                System.out.println("");
            } //--- while hasNext
        } catch (Exception e) {
            System.out.println("exception: " + e);

            e.printStackTrace();
        } //--- catch
    } //--- displaySimilarItems

    /**
     * DOCUMENT ME!
     *
     * @param traitDescriptors DOCUMENT ME!
     */
    public void displayTraitDescriptors(TraitDescriptors traitDescriptors) {
        System.out.println("");

        System.out.println("Trait Descriptors (" + traitDescriptors.size() +
            ")");

        System.out.println("-----------------");

        Iterator cursor = traitDescriptors.iterator(); //--- iterator on an ArrayList

        while (cursor.hasNext()) {
            String field = cursor.next().toString();

            System.out.print(field);

            System.out.print("  ");
        } //--- while hasNext

        System.out.println("");
    } //--- displayTraitDescriptors

    /**
     * DOCUMENT ME!
     *
     * @param similarityCriteria DOCUMENT ME!
     */
    public void displaySimilarityCriteria(SimilarityCriteria similarityCriteria) {
        System.out.println("");

        System.out.println("SimilarityCriteria (" + similarityCriteria.size() +
            ")");

        System.out.println("------------------");

        Iterator cursor = similarityCriteria.iterator(); //--- iterator on an ArrayList

        while (cursor.hasNext()) {
            SimilarityCriterion criterion = (SimilarityCriterion) cursor.next();

            System.out.print(criterion.getFieldName());

            System.out.print(" ");

            System.out.print(criterion.getOperatorAsString());

            System.out.print(" ");

            System.out.println(criterion.getValue());
        } //--- while hasNext
    } //--- displaySimilarityCriteria

    /**
     * DOCUMENT ME!
     *
     * @param similarityWeights DOCUMENT ME!
     */
    public void displaySimilarityWeights(SimilarityWeights similarityWeights) {
        System.out.println("");

        System.out.println("SimilarityWeights (" + similarityWeights.size() +
            ")");

        System.out.println("-----------------");

        Iterator cursor = similarityWeights.mapIterator();

        while (cursor.hasNext()) {
            //--- We're using the mapIterator because the normal iterator
            //---   will just return us an Integer, and we want the trait name too
            Map.Entry weight = (Map.Entry) cursor.next();

            System.out.print(weight.getKey());

            System.out.print(" ");

            System.out.println(weight.getValue());
        } //--- while hasNext
    } //--- displaySimilarityWeights
} //--- DisplayManager
