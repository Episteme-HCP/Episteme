package org.jscience.linguistics;

import org.jscience.geography.Place;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;
import org.jscience.geography.coordinates.Coordinates;
import org.jscience.geography.coordinates.LatLong;

/**
 * Dialect mapping and isogloss analysis.
 */
public final class DialectMapper {

    private DialectMapper() {}

    public record LinguisticFeature(
        String name,
        String category,  // phonological, lexical, morphological, syntactic
        String description
    ) {}

    public record DialectDataPoint(
        Place location,
        List<LinguisticFeature> features,
        String dialectName,
        int respondentCount
    ) {}

    public record Isogloss(
        String name,
        LinguisticFeature dividingFeature,
        List<double[]> boundaryPoints,  // latitude, longitude pairs
        String description
    ) {}

    /**
     * Creates an isogloss from dialect data.
     */
    public static Isogloss createIsogloss(List<DialectDataPoint> data, 
            LinguisticFeature feature, String name) {
        
        // Find the boundary between areas with/without the feature
        List<DialectDataPoint> withFeature = new ArrayList<>();
        List<DialectDataPoint> withoutFeature = new ArrayList<>();
        
        for (DialectDataPoint point : data) {
            if (point.features().contains(feature)) {
                withFeature.add(point);
            } else {
                withoutFeature.add(point);
            }
        }
        
        // Calculate approximate boundary (simplified: midpoints between closest pairs)
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
     * Calculates linguistic distance between two dialects.
     */
    public static Real linguisticDistance(DialectDataPoint a, DialectDataPoint b) {
        Set<LinguisticFeature> featuresA = new HashSet<>(a.features());
        Set<LinguisticFeature> featuresB = new HashSet<>(b.features());
        
        Set<LinguisticFeature> union = new HashSet<>(featuresA);
        union.addAll(featuresB);
        
        Set<LinguisticFeature> intersection = new HashSet<>(featuresA);
        intersection.retainAll(featuresB);
        
        // Jaccard distance = 1 - (intersection / union)
        double jaccard = union.isEmpty() ? 0 : 
            1.0 - (double) intersection.size() / union.size();
        
        return Real.of(jaccard);
    }

    /**
     * Clusters dialects based on feature similarity.
     */
    public static Map<Integer, List<DialectDataPoint>> clusterDialects(
            List<DialectDataPoint> data, int numClusters) {
        
        // Simple k-means-like clustering based on linguistic distance
        Random random = new Random(42);
        
        // Initialize cluster centers
        List<DialectDataPoint> centers = new ArrayList<>();
        List<DialectDataPoint> shuffled = new ArrayList<>(data);
        Collections.shuffle(shuffled, random);
        for (int i = 0; i < numClusters && i < shuffled.size(); i++) {
            centers.add(shuffled.get(i));
        }
        
        // Assign points to clusters
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
     * Generates a dialect map summary.
     */
    public static String generateMapSummary(List<DialectDataPoint> data, List<Isogloss> isoglosses) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== Dialect Map Summary ===\n\n");
        sb.append(String.format("Total data points: %d\n", data.size()));
        
        // Count distinct dialects
        Set<String> dialects = new HashSet<>();
        for (DialectDataPoint point : data) {
            dialects.add(point.dialectName());
        }
        sb.append(String.format("Distinct dialects: %d\n\n", dialects.size()));
        
        sb.append("Isoglosses:\n");
        for (Isogloss iso : isoglosses) {
            sb.append(String.format("  - %s: %s (%d boundary points)\n",
                iso.name(), iso.dividingFeature().name(), iso.boundaryPoints().size()));
        }
        
        return sb.toString();
    }

    /**
     * Predefined linguistic features for common dialectal variations.
     */
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
        if (point.location() == null || point.location().getCoordinates() == null) return 0;
        Coordinates c = point.location().getCoordinates();
        if (c instanceof LatLong ll) return ll.getLatitude();
        return c.getDimension() > 0 ? c.get(0) : 0;
    }

    private static double getLongitude(DialectDataPoint point) {
        if (point.location() == null || point.location().getCoordinates() == null) return 0;
        Coordinates c = point.location().getCoordinates();
        if (c instanceof LatLong ll) return ll.getLongitude();
        return c.getDimension() > 1 ? c.get(1) : 0;
    }
}
