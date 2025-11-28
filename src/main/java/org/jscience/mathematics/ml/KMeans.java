package org.jscience.mathematics.ml;

import org.jscience.mathematics.number.Real;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * k-means clustering algorithm.
 * <p>
 * Partitions n observations into k clusters based on nearest centroid.
 * Unsupervised learning for data grouping, pattern recognition.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class KMeans {

    private final int k;
    private final int maxIterations;
    private final Real[][] centroids;
    private final Random random = new Random();

    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.centroids = new Real[k][];
    }

    /**
     * Fits k-means model to data.
     * 
     * @param data n samples × d features
     * @return cluster assignments for each sample
     */
    public int[] fit(Real[][] data) {
        int n = data.length;
        int d = data[0].length;

        // Initialize centroids randomly (k-means++)
        initializeCentroidsKMeansPlusPlus(data);

        int[] assignments = new int[n];
        boolean changed = true;
        int iter = 0;

        while (changed && iter < maxIterations) {
            changed = false;
            iter++;

            // Assignment step: assign each point to nearest centroid
            for (int i = 0; i < n; i++) {
                int newCluster = findNearestCentroid(data[i]);
                if (newCluster != assignments[i]) {
                    changed = true;
                    assignments[i] = newCluster;
                }
            }

            // Update step: recompute centroids
            updateCentroids(data, assignments);
        }

        return assignments;
    }

    /**
     * k-means++ initialization: smarter centroid selection for better convergence.
     */
    private void initializeCentroidsKMeansPlusPlus(Real[][] data) {
        int n = data.length;
        int d = data[0].length;

        // First centroid: random point
        int firstIdx = random.nextInt(n);
        centroids[0] = data[firstIdx].clone();

        // Remaining centroids: probability proportional to distance²
        for (int c = 1; c < k; c++) {
            Real[] distances = new Real[n];
            Real sumDistances = Real.ZERO;

            for (int i = 0; i < n; i++) {
                // Distance to nearest centroid so far
                Real minDist = Real.of(Double.MAX_VALUE);
                for (int j = 0; j < c; j++) {
                    Real dist = euclideanDistance(data[i], centroids[j]);
                    if (dist.compareTo(minDist) < 0) {
                        minDist = dist;
                    }
                }
                distances[i] = minDist.multiply(minDist); // squared
                sumDistances = sumDistances.add(distances[i]);
            }

            // Select proportional to distance²
            Real r = Real.of(random.nextDouble()).multiply(sumDistances);
            Real cumSum = Real.ZERO;
            for (int i = 0; i < n; i++) {
                cumSum = cumSum.add(distances[i]);
                if (cumSum.compareTo(r) >= 0) {
                    centroids[c] = data[i].clone();
                    break;
                }
            }
        }
    }

    private int findNearestCentroid(Real[] point) {
        int nearest = 0;
        Real minDist = euclideanDistance(point, centroids[0]);

        for (int i = 1; i < k; i++) {
            Real dist = euclideanDistance(point, centroids[i]);
            if (dist.compareTo(minDist) < 0) {
                minDist = dist;
                nearest = i;
            }
        }

        return nearest;
    }

    private void updateCentroids(Real[][] data, int[] assignments) {
        int d = data[0].length;
        int[] counts = new int[k];

        // Reset centroids
        for (int i = 0; i < k; i++) {
            centroids[i] = new Real[d];
            for (int j = 0; j < d; j++) {
                centroids[i][j] = Real.ZERO;
            }
        }

        // Sum points in each cluster
        for (int i = 0; i < data.length; i++) {
            int cluster = assignments[i];
            counts[cluster]++;
            for (int j = 0; j < d; j++) {
                centroids[cluster][j] = centroids[cluster][j].add(data[i][j]);
            }
        }

        // Compute means
        for (int i = 0; i < k; i++) {
            if (counts[i] > 0) {
                for (int j = 0; j < d; j++) {
                    centroids[i][j] = centroids[i][j].divide(Real.of(counts[i]));
                }
            }
        }
    }

    private Real euclideanDistance(Real[] a, Real[] b) {
        Real sum = Real.ZERO;
        for (int i = 0; i < a.length; i++) {
            Real diff = a[i].subtract(b[i]);
            sum = sum.add(diff.multiply(diff));
        }
        return sum.sqrt();
    }

    /**
     * Computes inertia (sum of squared distances to centroids).
     * <p>
     * Lower is better. Used for elbow method to find optimal k.
     * </p>
     */
    public Real inertia(Real[][] data, int[] assignments) {
        Real sum = Real.ZERO;
        for (int i = 0; i < data.length; i++) {
            Real dist = euclideanDistance(data[i], centroids[assignments[i]]);
            sum = sum.add(dist.multiply(dist));
        }
        return sum;
    }

    public Real[][] getCentroids() {
        return centroids;
    }

    /**
     * Predicts cluster for new data points.
     */
    public int[] predict(Real[][] newData) {
        int[] predictions = new int[newData.length];
        for (int i = 0; i < newData.length; i++) {
            predictions[i] = findNearestCentroid(newData[i]);
        }
        return predictions;
    }
}
