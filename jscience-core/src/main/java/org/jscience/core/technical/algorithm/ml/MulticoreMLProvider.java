package org.jscience.core.technical.algorithm.ml;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.MLProvider;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Multicore Java implementation of ML algorithms using Parallel Streams.
 * Optimized for multi-threaded execution.
 */
@AutoService(MLProvider.class)
public class MulticoreMLProvider implements MLProvider {

    @Override
    public String getName() {
        return "Java ML (Multicore)";
    }

    @Override
    public int getPriority() {
        return 10; // Higher priority than basic reference
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    // ========== DOUBLE PRECISION ==========

    @Override
    public int[] kMeans(double[][] data, int k, int maxIterations) {
        int n = data.length;
        int d = data[0].length;
        
        // Initialize centroids randomly
        double[][] centroids = new double[k][d];
        Random rand = new Random(42);
        for (int i = 0; i < k; i++) {
            int idx = rand.nextInt(n);
            System.arraycopy(data[idx], 0, centroids[i], 0, d);
        }
        
        int[] assignments = new int[n];
        
        // K-Means iterations
        for (int iter = 0; iter < maxIterations; iter++) {
            // Assign points to nearest centroid (Parallel)
            double[][] finalCentroids = centroids; // Effective final for lambda
            IntStream.range(0, n).parallel().forEach(i -> {
                double minDist = Double.MAX_VALUE;
                int bestCluster = 0;
                for (int j = 0; j < k; j++) {
                    double dist = euclideanDistance(data[i], finalCentroids[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        bestCluster = j;
                    }
                }
                assignments[i] = bestCluster;
            });
            
            // Update centroids
            double[][] newCentroids = new double[k][d];
            int[] counts = new int[k];
            
            // Serial accumulation for stability/simplicity, parallelize if k is large?
            // For now, serial accumulation is safer/easier unless we use concurrent accumulators.
            // Given k is usually small << n, serial update is fast enough compared to assignment.
            for (int i = 0; i < n; i++) {
                int cluster = assignments[i];
                counts[cluster]++;
                for (int j = 0; j < d; j++) {
                    newCentroids[cluster][j] += data[i][j];
                }
            }
            
            for (int i = 0; i < k; i++) {
                if (counts[i] > 0) {
                    for (int j = 0; j < d; j++) {
                        centroids[i][j] = newCentroids[i][j] / counts[i];
                    }
                }
            }
        }
        
        return assignments;
    }

    @Override
    public double[][] pca(double[][] data, int nComponents) {
        int n = data.length;
        int d = data[0].length;
        
        // Center the data
        double[] mean = new double[d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                mean[j] += data[i][j];
            }
        }
        for (int j = 0; j < d; j++) {
            mean[j] /= n;
        }
        
        double[][] centered = new double[n][d];
        double[] finalMean = mean; // Effective final
        
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j] - finalMean[j];
            }
        });
        
        // Simplified PCA: just return first nComponents dimensions
        double[][] result = new double[n][Math.min(nComponents, d)];
        IntStream.range(0, n).parallel().forEach(i -> {
            System.arraycopy(centered[i], 0, result[i], 0, Math.min(nComponents, d));
        });
        
        return result;
    }

    // ========== REAL PRECISION ==========

    @Override
    public int[] kMeans(Real[][] data, int k, int maxIterations) {
        int n = data.length;
        int d = data[0].length;
        
        // Initialize centroids
        Real[][] centroids = new Real[k][d];
        Random rand = new Random(42);
        for (int i = 0; i < k; i++) {
            int idx = rand.nextInt(n);
            System.arraycopy(data[idx], 0, centroids[i], 0, d);
        }
        
        int[] assignments = new int[n];
        
        for (int iter = 0; iter < maxIterations; iter++) {
            // Assign points (Parallel)
            Real[][] finalCentroids = centroids;
            IntStream.range(0, n).parallel().forEach(i -> {
                Real minDist = Real.of(Double.MAX_VALUE);
                int bestCluster = 0;
                for (int j = 0; j < k; j++) {
                    Real dist = euclideanDistanceReal(data[i], finalCentroids[j]);
                    if (dist.compareTo(minDist) < 0) {
                        minDist = dist;
                        bestCluster = j;
                    }
                }
                assignments[i] = bestCluster;
            });
            
            // Update centroids (Serial for now)
            Real[][] newCentroids = new Real[k][d];
            int[] counts = new int[k];
            
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < d; j++) {
                    newCentroids[i][j] = Real.ZERO;
                }
            }
            
            for (int i = 0; i < n; i++) {
                int cluster = assignments[i];
                counts[cluster]++;
                for (int j = 0; j < d; j++) {
                    newCentroids[cluster][j] = newCentroids[cluster][j].add(data[i][j]);
                }
            }
            
            for (int i = 0; i < k; i++) {
                if (counts[i] > 0) {
                    for (int j = 0; j < d; j++) {
                        centroids[i][j] = newCentroids[i][j].divide(Real.of(counts[i]));
                    }
                }
            }
        }
        
        return assignments;
    }

    @Override
    public Real[][] pca(Real[][] data, int nComponents) {
        int n = data.length;
        int d = data[0].length;
        
        // Mean
        Real[] mean = new Real[d];
        for (int j = 0; j < d; j++) {
            mean[j] = Real.ZERO;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                mean[j] = mean[j].add(data[i][j]);
            }
        }
        for (int j = 0; j < d; j++) {
            mean[j] = mean[j].divide(Real.of(n));
        }
        
        // Center
        Real[][] centered = new Real[n][d];
        Real[] finalMean = mean;
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j].subtract(finalMean[j]);
            }
        });
        
        // Result
        Real[][] result = new Real[n][Math.min(nComponents, d)];
        IntStream.range(0, n).parallel().forEach(i -> {
            System.arraycopy(centered[i], 0, result[i], 0, Math.min(nComponents, d));
        });
        
        return result;
    }

    // ========== HELPER METHODS ==========

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    private Real euclideanDistanceReal(Real[] a, Real[] b) {
        Real sum = Real.ZERO;
        for (int i = 0; i < a.length; i++) {
            Real diff = a[i].subtract(b[i]);
            sum = sum.add(diff.multiply(diff));
        }
        return sum.sqrt();
    }
}
