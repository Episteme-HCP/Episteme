/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.linguistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.episteme.natural.earth.Place;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for dialect mapping and isogloss analysis. It facilitates 
 * the study of spatial linguistic variation by mapping features to geographical 
 * locations and drawing boundaries between dialect regions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class DialectMapper {

    private DialectMapper() {}

    /**
     * Represents a specific linguistic variable (phonological, lexical, etc.).
     */
    public record LinguisticFeature(
        String name,
        String category,  // e.g., "phonological", "lexical"
        String description
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Data point from a linguistic survey at a specific geographical location.
     */
    public record DialectDataPoint(
        Place location,
        List<LinguisticFeature> features,
        String dialectName,
        int respondentCount
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents a line on a map marking the geographical boundary of a 
     * linguistic feature.
     */
    public record Isogloss(
        String name,
        LinguisticFeature dividingFeature,
        List<double[]> boundaryPoints,  // [latitude, longitude] pairs
        String description
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Generates an isogloss boundary based on a collection of survey data points 
     * for a specific linguistic feature.
     * 
     * @param data list of survey points
     * @param feature the feature to map
     * @param name name for the generated isogloss
     * @return an Isogloss representing the boundary for the feature
     */
    public static Isogloss createIsogloss(List<DialectDataPoint> data, 
            LinguisticFeature feature, String name) {
        
        List<DialectDataPoint> withFeature = new ArrayList<>();
        List<DialectDataPoint> withoutFeature = new ArrayList<>();
        
        for (DialectDataPoint point : data) {
            if (point.features().contains(feature)) {
                withFeature.add(point);
            } else {
                withoutFeature.add(point);
            }
        }
        
        List<double[]> boundary = new ArrayList<>();
        for (DialectDataPoint with : withFeature) {
            DialectDataPoint closest = findClosest(with, withoutFeature);
            if (closest != null) {
                double midLat = (getLatitude(with) + getLatitude(closest)) / 2;
                double midLon = (getLongitude(with) + getLongitude(closest)) / 2;
                boundary.add(new double[]{midLat, midLon});
            }
        }
        
        return new Isogloss(name, feature, boundary, 
            "Boundary for feature: " + feature.name());
    }

    /**
     * Calculates the linguistic distance (using Jaccard similarity inverse) 
     * between two survey points.
     * 
     * @param a first survey point
     * @param b second survey point
     * @return distance metric (0.0 to 1.0) as a Real
     */
    public static Real linguisticDistance(DialectDataPoint a, DialectDataPoint b) {
        Set<LinguisticFeature> featuresA = new HashSet<>(a.features());
        Set<LinguisticFeature> featuresB = new HashSet<>(b.features());
        
        Set<LinguisticFeature> union = new HashSet<>(featuresA);
        union.addAll(featuresB);
        
        Set<LinguisticFeature> intersection = new HashSet<>(featuresA);
        intersection.retainAll(featuresB);
        
        double jaccard = union.isEmpty() ? 0 : 
            1.0 - (double) intersection.size() / union.size();
        
        return Real.of(jaccard);
    }

    /**
     * Groups dialect points into clusters based on shared linguistic features 
     * using an iterative similarity-based algorithm.
     * 
     * @param data survey points to cluster
     * @param numClusters target number of clusters
     * @return map of cluster index to list of points
     */
    public static Map<Integer, List<DialectDataPoint>> clusterDialects(
            List<DialectDataPoint> data, int numClusters) {
        
        Random random = new Random(42);
        List<DialectDataPoint> centers = new ArrayList<>();
        List<DialectDataPoint> shuffled = new ArrayList<>(data);
        Collections.shuffle(shuffled, random);
        for (int i = 0; i < numClusters && i < shuffled.size(); i++) {
            centers.add(shuffled.get(i));
        }
        
        Map<Integer, List<DialectDataPoint>> clusters = new HashMap<>();
        for (int i = 0; i < numClusters; i++) {
            clusters.put(i, new ArrayList<>());
        }
        
        for (DialectDataPoint point : data) {
            int bestCluster = 0;
            double minDist = Double.MAX_VALUE;
            
            for (int i = 0; i < centers.size(); i++) {
                double dist = linguisticDistance(point, centers.get(i)).doubleValue();
                if (dist < minDist) {
                    minDist = dist;
                    bestCluster = i;
                }
            }
            clusters.get(bestCluster).add(point);
        }
        return clusters;
    }

    /**
     * Generates a structural summary of the dialectal map and identified isoglosses.
     * 
     * @param data the survey dataset
     * @param isoglosses the identified boundaries
     * @return a human-readable summary
     */
    public static String generateMapSummary(List<DialectDataPoint> data, List<Isogloss> isoglosses) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Dialect Map Summary ===\n\n");
        sb.append(String.format("Total data points: %d\n", data.size()));
        
        Set<String> dialects = new HashSet<>();
        for (DialectDataPoint point : data) dialects.add(point.dialectName());
        sb.append(String.format("Distinct dialects: %d\n\n", dialects.size()));
        
        sb.append("Isoglosses:\n");
        for (Isogloss iso : isoglosses) {
            sb.append(String.format("  - %s: %s (%d boundary points)\n",
                iso.name(), iso.dividingFeature().name(), iso.boundaryPoints().size()));
        }
        return sb.toString();
    }

    // Predefined features
    public static final LinguisticFeature RHOTICITY = 
        new LinguisticFeature("Rhoticity", "phonological", "Pronunciation of post-vocalic /r/");
    public static final LinguisticFeature VOWEL_SHIFT = 
        new LinguisticFeature("Northern Cities Vowel Shift", "phonological", "Chain shift of short vowels");
    public static final LinguisticFeature CODA_DEVOICING = 
        new LinguisticFeature("Final Obstruent Devoicing", "phonological", "Devoicing of word-final consonants");
    public static final LinguisticFeature DOUBLE_NEGATION = 
        new LinguisticFeature("Negative Concord", "syntactic", "Multiple negation in sentence");
    public static final LinguisticFeature Y_ALL = 
        new LinguisticFeature("Y'all", "lexical", "Second person plural pronoun");

    private static DialectDataPoint findClosest(DialectDataPoint target, List<DialectDataPoint> candidates) {
        if (candidates.isEmpty()) return null;
        DialectDataPoint closest = null;
        double minDist = Double.MAX_VALUE;
        for (DialectDataPoint candidate : candidates) {
            double dist = geographicDistance(target, candidate);
            if (dist < minDist) {
                minDist = dist;
                closest = candidate;
            }
        }
        return closest;
    }

    private static double geographicDistance(DialectDataPoint a, DialectDataPoint b) {
        double lat1 = getLatitude(a), lon1 = getLongitude(a);
        double lat2 = getLatitude(b), lon2 = getLongitude(b);
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    private static double getLatitude(DialectDataPoint point) {
        if (point.location() == null || point.location().getCenter() == null) return 0;
        return point.location().getCenter().getLatitude().to(org.episteme.core.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    private static double getLongitude(DialectDataPoint point) {
        if (point.location() == null || point.location().getCenter() == null) return 0;
        return point.location().getCenter().getLongitude().to(org.episteme.core.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
    }
}

