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

/**
 * Pass it a bunch of data and the SimilarityEngine figures out
 * <p/>
 * how similar each item is (%) to a specified goal
 *
 * @author <small>baylor</small>
 */
public class SimilarityEngine {
    //-----------------------------------------------------------------------------
    // Private Constants
    //-----------------------------------------------------------------------------
    private static final String MAX_VAL_INDICATOR = "[max_val]";
    private static final String MIN_VAL_INDICATOR = "[min_val]";

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    private float computeDistance(SimilarityCriterionScores targetValues,
                                  SimilarityCriterionScores itemValues) {
        float sum = 0;

        Iterator targetValueList = targetValues.iterator();

        while (targetValueList.hasNext()) {
            //--- This is the score for one of the several criteria we measured
            SimilarityCriterionScore targetScore = (SimilarityCriterionScore) targetValueList.next();

            SimilarityCriterionScore itemScore = itemValues.get(targetScore.getID());

            float targetValue = targetScore.getWeightedValue();

            float itemValue = itemScore.getWeightedValue();

            float delta = (targetValue - itemValue);

            float squaredDelta = (delta * delta);

            sum += squaredDelta;
        } //--- while targetValueList.hasNext

        float distance = (float) Math.sqrt(sum);

        return distance;
    } //--- computeDistance (from one item to target)

    /**
     * DOCUMENT ME!
     *
     * @param items    DOCUMENT ME!
     * @param criteria DOCUMENT ME!
     * @param weights  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public SimilarItems computeSimilarity(Items items,
                                          SimilarityCriteria criteria, SimilarityWeights weights) {
        //--- Calculate the DataSetStatistics on the items passed in
        //--- If we are only being passed a subset of items
        //---   (ex. - the items have been filtered), the stats
        //---   will only be calculated on the subset of items
        DataSetStatistics stats = new DataSetStatistics(items);

        return computeSimilarity(items, criteria, weights, stats);
    } //--- computeSimilarity, all items, no passed-in data set stats

    /**
     * DOCUMENT ME!
     *
     * @param items      DOCUMENT ME!
     * @param criteria   DOCUMENT ME!
     * @param weights    DOCUMENT ME!
     * @param statistics DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public SimilarItems computeSimilarity(Items items,
                                          SimilarityCriteria criteria, SimilarityWeights weights,
                                          DataSetStatistics statistics) {
        //--- This is the item that we want to compare ourselves to
        //---   to see how similar we are
        SimilarityCriterionScores targetValues = getTargetValues(items.getTraitDescriptors(),
                criteria, weights, statistics);

        float maxDistance = getMaxDistance(criteria, weights);

        //--- Create a similarity descriptor for each item
        SimilarItems similarItems = new SimilarItems();

        Iterator itemList = items.iterator();

        while (itemList.hasNext()) {
            Item item = (Item) itemList.next();

            SimilarityDescription descriptor = new SimilarityDescription();

            descriptor.setItem(item);

            SimilarityCriterionScores itemValues = normalizeValues(item,
                    criteria, weights, statistics);

            float distance = computeDistance(targetValues, itemValues);

            float percentDifference = (distance / maxDistance);

            float percentSimilarity = (1 - percentDifference);

            descriptor.setPercentSimilarity(percentSimilarity);

            similarItems.add(descriptor);
        } //--- itemList.hasNext()

        //--- Now that we know how similar everyone is, let's go and
        //---   and rank them so that the caller has an easy way to sort
        //---   the items and so to make it easier to select the
        //---   k-best matches
        similarItems.rankItems();

        //--- OK, we're all done, hand the whole package back to the caller
        return similarItems;
    } //--- computeSimilarity (multiple items)

    /**
     * To compute how similar two items are, we first create
     * <p/>
     * a single-dimensional range from 0 to some max distance
     * <p/>
     * number. We then give each item a score that falls
     * <p/>
     * somewhere in that range
     * <p/>
     * The perfect item will score 0, meaning the difference
     * <p/>
     * between what we're looking for and this item is nothing
     * <p/>
     * The worst possible choice will score the max distance,
     * <p/>
     * meaning it is the opposite of everything we wanted
     * <p/>
     * We can give every item a score without knowing the max distance,
     * <p/>
     * but we need it if we want to compute percent similarity
     * <p/>
     * To get the max distance, figure out the number of criteria
     * <p/>
     * we were measuring to compute similarity. Multiply each
     * <p/>
     * by its weight. Square the weights, add them all together
     * <p/>
     * and then take the square root of that. In other words,
     * <p/>
     * do the old Pythagorean Theorem on the weights
     * <p/>
     * For those who don't remember 9th grade geometry (thank god!)
     * <p/>
     * the Pythagorean Theorem is that, for right triangles,
     * <p/>
     * the length of side c is (a^2 + b^2) = c^2,
     * <p/>
     * so the length of c = sqrt(a^2 + b^2)
     */
    private float getMaxDistance(SimilarityCriteria criteria,
                                 SimilarityWeights weights) {
        float sum = 0;

        //--- Square, sum, take square root
        Iterator criteriaList = criteria.iterator();

        while (criteriaList.hasNext()) {
            SimilarityCriterion criterion = (SimilarityCriterion) criteriaList.next();

            String fieldName = criterion.getFieldName();

            float weight = weights.get(fieldName);

            weight *= weight; //--- Isn't there a square operator in Java like ^?

            sum += weight;
        } //--- criteriaList.hasNext()

        float squareOfSummedDeltas = (float) Math.sqrt(sum);

        return squareOfSummedDeltas;
    } //--- getMaxDistance

    private SimilarityCriterionScores getTargetValues(TraitDescriptors traitDescriptors, SimilarityCriteria criteria,
                                                      SimilarityWeights weights, DataSetStatistics statistics) {
        SimilarityCriterionScores normalizedValues = new SimilarityCriterionScores();

        //--- Compute the normalized and weighted values of each trait we're
        //---   measuring.
        //--- The normalized value will be between 0 and 1
        //---   where 1 means you had the max value that existed in the data set,
        //---   0 means you had the minimum value and any other number is basically
        //---   how close you were to the min and max
        Iterator criteriaList = criteria.iterator();

        while (criteriaList.hasNext()) {
            SimilarityCriterion criterion = (SimilarityCriterion) criteriaList.next();

            String criterionID = criterion.getID();

            String traitName = criterion.getFieldName();

            int traitDataType = traitDescriptors.getDataType(traitName);

            SimilarityCriterionScore score
                    = new SimilarityCriterionScore(criterionID);

            normalizedValues.add(score);

            float position = 0;

            if ((traitDataType != TraitDescriptor.TYPE_FLOAT) &&
                    (traitDataType != TraitDescriptor.TYPE_INTEGER)) {
                switch (criterion.getOperator()) {
                    case SimilarityCriterion.OPERATOR_SIMILAR:
                        position = 1;

                        break;

                    case SimilarityCriterion.OPERATOR_NOT_SIMILAR:
                        position = 0;

                        break;

                    default:
                        position = 0;

                        break;
                } //--- switch operator
            } else {
                //--- We have numbers, so we want to calculate where this
                //---   trait for this item falls on the continuum
                //---   from min value to max value
                //--- We also want it to normalized to a percentage
                //---   format, which means a float between 0 and 1
                //--- Ex. - Say we have an SAT score of 1,000
                //---   The max score on the SAT is 1600, min=400
                //---   Our 1000 is half way between 400 and 1600
                //---   We'd normalize this value to 0.5 (50%) by doing
                //---   (score - min) / range
                //---   = (1000 - 400) / (1600 - 400)
                //---   = 600 / 1200
                //---   = 0.5
                TraitStatistics stats = statistics.get(traitName);

                float max = stats.getMaximumValue();

                float min = stats.getMinimumValue();

                float range = stats.getRange();

                //--- Although numeric data in the data set should always be numeric,
                //---   in the user's query, numeric data can also be the special
                //---   String keywords [MAX_VAL] or [MIN_VAL]
                TraitValue traitValue = criterion.getValue();

                float value = 0;

                if (traitValue.toString().equals(MAX_VAL_INDICATOR)) {
                    value = max;
                } else if (traitValue.toString().equals(MIN_VAL_INDICATOR)) {
                    value = min;
                } else {
                    value = traitValue.toFloat();
                }

                //--- 0=min value, 1=max value, in between means % of max
                position = (value - min) / range;
            } //--- if dataType = ...

            score.setNormalizedValue(position);

            float weight = weights.get(traitName);

            float weightedValue = (position * weight);

            score.setWeightedValue(weightedValue);
        } //--- criteriaList.hasNext()

        return normalizedValues;
    } //--- getTargetValues

    private SimilarityCriterionScores normalizeValues(Item item,
                                                      SimilarityCriteria criteria, SimilarityWeights weights,
                                                      DataSetStatistics statistics) {
        SimilarityCriterionScores normalizedValues = new SimilarityCriterionScores();

        //--- Compute the normalized and weighted values of each trait we're
        //---   measuring.
        //--- The normalized value will be between 0 and 1
        //---   where 1 means you had the max value that existed in the data set,
        //---   0 means you had the minimum value and any other number is basically
        //---   how close you were to the min and max
        Iterator criteriaList = criteria.iterator();

        while (criteriaList.hasNext()) {
            SimilarityCriterion criterion = (SimilarityCriterion) criteriaList.next();

            String traitName = criterion.getFieldName();

            int traitDataType = item.getTraitDataType(traitName);

            String criterionID = criterion.getID();

            SimilarityCriterionScore score
                    = new SimilarityCriterionScore(criterionID);

            normalizedValues.add(score);

            float position = 0;

            if ((traitDataType != TraitDescriptor.TYPE_FLOAT) &&
                    (traitDataType != TraitDescriptor.TYPE_INTEGER)) {
                //--- We have a string or boolean
                //--- We only do "=" on those, so see if they're equal
                String value = item.getTraitValue(traitName).toString();

                String targetValue = criterion.getValue().toString();

                if (value.equals(targetValue)) {
                    position = 1;
                } else {
                    position = 0;
                }
            } else {
                //--- We have numbers, so we want to calculate where this
                //---   trait for this item falls on the continuum
                //---   from min value to max value
                //--- We also want it to normalized to a percentage
                //---   format, which means a float between 0 and 1
                //--- Ex. - Say we have an SAT score of 1,000
                //---   The max score on the SAT is 1600, min=400
                //---   Our 1000 is half way between 400 and 1600
                //---   We'd normalize this value to 0.5 (50%) by doing
                //---   (score - min) / range
                //---   = (1000 - 400) / (1600 - 400)
                //---   = 600 / 1200
                //---   = 0.5
                float itemValue = item.getTraitValue(traitName).toFloat();

                TraitStatistics stats = statistics.get(traitName);

                float min = stats.getMinimumValue();

                float range = stats.getRange();

                position = (itemValue - min) / range;
            } //--- if dataType = ...

            score.setNormalizedValue(position);

            float weightedValue = (position * weights.get(traitName));

            score.setWeightedValue(weightedValue);
        } //--- criteriaList.hasNext()

        return normalizedValues;
    } //--- normalizeValues
} //--- SimilarityEngine
