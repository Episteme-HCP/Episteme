package org.jscience.core.mathematics.ml.providers;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.ml.MLProvider;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.Random;
import java.util.Arrays;
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
        
        // 1. Center the data
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
        double[] finalMean = mean; 
        
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j] - finalMean[j];
            }
        });

        // 2. Compute Covariance Matrix: C = (X^T * X) / (n - 1)
        // C is d x d and Symmetric.
        double[][] covariance = new double[d][d];
        
        // Parallelize outer loop of covariance computation
        IntStream.range(0, d).parallel().forEach(i -> {
            for (int j = i; j < d; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += centered[k][i] * centered[k][j];
                }
                double val = sum / (n - 1);
                covariance[i][j] = val;
                covariance[j][i] = val; // Symmetric
            }
        });

        // 3. Eigen Decomposition (Jacobi Algorithm for Symmetric Matrix)
        // Returns eigenvectors as columns in V, and eigenvalues in d
        JacobiResult result = jacobi(covariance);
        
        // 4. Sort eigenvalues and permute eigenvectors
        Integer[] indices = new Integer[d];
        for (int i = 0; i < d; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> Double.compare(result.eigenvalues[b], result.eigenvalues[a])); // Descending
        
        // 5. Project data onto top k eigenvectors
        int k = Math.min(nComponents, d);
        double[][] projection = new double[n][k];
        
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int comp = 0; comp < k; comp++) {
                int originalColIndex = indices[comp];
                double sum = 0.0;
                for (int j = 0; j < d; j++) {
                    // eigenVectors are columns in result.eigenvectors
                    // result.eigenvectors[row][col] -> V[j][originalColIndex]
                    sum += centered[i][j] * result.eigenvectors[j][originalColIndex];
                }
                projection[i][comp] = sum;
            }
        });
        
        return projection;
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
        // For Real precision, we currently fallback to double for speed or implement full Real Jacobi.
        // Given complexity, let's map to double for now as requested by user verification plan
        // or implement a Real version of Jacobi.
        // The interface defines Real[][] support. Implementing true Real Jacobi is best.
        
        int n = data.length;
        int d = data[0].length;
        
        // 1. Center
        Real[] mean = new Real[d];
        for (int j = 0; j < d; j++) mean[j] = Real.ZERO;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                mean[j] = mean[j].add(data[i][j]);
            }
        }
        for (int j = 0; j < d; j++) mean[j] = mean[j].divide(Real.of(n));
        
        Real[][] centered = new Real[n][d];
        Real[] finalMean = mean;
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j].subtract(finalMean[j]);
            }
        });

        // 2. Covariance
        Real[][] covariance = new Real[d][d];
        Real nMinusOne = Real.of(n - 1);
        
        IntStream.range(0, d).parallel().forEach(i -> {
            for (int j = i; j < d; j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < n; k++) {
                    sum = sum.add(centered[k][i].multiply(centered[k][j]));
                }
                Real val = sum.divide(nMinusOne);
                covariance[i][j] = val;
                covariance[j][i] = val;
            }
        });

        // 3. Jacobi
        JacobiResultReal result = jacobiReal(covariance);
        
        // 4. Sort
        Integer[] indices = new Integer[d];
        for (int i = 0; i < d; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> result.eigenvalues[b].compareTo(result.eigenvalues[a]));
        
        // 5. Project
        int k = Math.min(nComponents, d);
        Real[][] projection = new Real[n][k];
        
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int comp = 0; comp < k; comp++) {
                int originalCol = indices[comp];
                Real sum = Real.ZERO;
                for (int j = 0; j < d; j++) {
                    sum = sum.add(centered[i][j].multiply(result.eigenvectors[j][originalCol]));
                }
                projection[i][comp] = sum;
            }
        });
        
        return projection;
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
    
    // --- Jacobi Algorithm (Double) ---
    
    private static class JacobiResult {
        double[] eigenvalues;
        double[][] eigenvectors;
    }

    private JacobiResult jacobi(double[][] A) {
        int n = A.length;
        double[][] V = new double[n][n];
        double[] d = new double[n];
        
        // Initialize V as identity, d as diagonal of A
        for (int i = 0; i < n; i++) {
            V[i][i] = 1.0;
            d[i] = A[i][i];
        }
        
        double[][] B = new double[n][n]; // Copy of A to modify
        for(int i=0; i<n; i++) System.arraycopy(A[i], 0, B[i], 0, n);
        
        int maxIter = 100;
        for (int iter = 0; iter < maxIter; iter++) {
            double maxOffDiag = 0.0;
            int p = 0, q = 0;
            
            // Find max off-diagonal element
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(B[i][j]) > maxOffDiag) {
                        maxOffDiag = Math.abs(B[i][j]);
                        p = i; q = j;
                    }
                }
            }
            
            if (maxOffDiag < 1e-10) break; // Converged
            
            double phi;
            if (Math.abs(B[p][p] - B[q][q]) < 1e-10) {
                phi = Math.PI / 4;
            } else {
                phi = 0.5 * Math.atan(2 * B[p][q] / (B[p][p] - B[q][q]));
            }
            
            double c = Math.cos(phi);
            double s = Math.sin(phi);
            
            // Update diagonal elements
            // We only need eigenvalues for PCA variance, but eigenvectors (V) are crucial for projection.
            // B is updated to diagonal form. V is updated by rotations.
            
            // Update B elements (only needed parts)
            // Easier to update whole matrix B and V for correctness in this simplified implementation context
            // A more optimized version updates only rows/cols p and q.
            
            // Optimization: Update only changed rows/cols
            double Bpp = B[p][p];
            double Bqq = B[q][q];
            double Bpq = B[p][q];
            
            B[p][p] = c*c*Bpp - 2*s*c*Bpq + s*s*Bqq;
            B[q][q] = s*s*Bpp + 2*s*c*Bpq + c*c*Bqq;
            B[p][q] = 0; B[q][p] = 0;
            
            for (int i = 0; i < n; i++) {
                if (i != p && i != q) {
                    double Bip = B[i][p];
                    double Biq = B[i][q];
                    B[i][p] = c*Bip - s*Biq;
                    B[p][i] = B[i][p];
                    B[i][q] = s*Bip + c*Biq;
                    B[q][i] = B[i][q];
                }
                
                // Update Eigenvectors
                double Vip = V[i][p];
                double Viq = V[i][q];
                V[i][p] = c*Vip - s*Viq;
                V[i][q] = s*Vip + c*Viq;
            }
        }
        
        for(int i=0; i<n; i++) d[i] = B[i][i];
        
        JacobiResult res = new JacobiResult();
        res.eigenvalues = d;
        res.eigenvectors = V;
        return res;
    }
    
    // --- Jacobi Algorithm (Real) ---
    
    private static class JacobiResultReal {
        Real[] eigenvalues;
        Real[][] eigenvectors;
    }
    
    private JacobiResultReal jacobiReal(Real[][] A) {
        int n = A.length;
        Real[][] V = new Real[n][n];
        Real[] d = new Real[n];
        
        // Identity
        for (int i = 0; i < n; i++) {
            for(int j=0; j<n; j++) V[i][j] = (i==j) ? Real.ONE : Real.ZERO;
            d[i] = A[i][i];
        }
        
        Real[][] B = new Real[n][n];
        for(int i=0; i<n; i++) System.arraycopy(A[i], 0, B[i], 0, n);
            
        int maxIter = 100;
        Real tolerance = Real.of(1e-10);
        
        for (int iter = 0; iter < maxIter; iter++) {
            Real maxOffDiag = Real.ZERO;
            int p = 0, q = 0;
            
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    Real val = B[i][j].abs();
                    if (val.compareTo(maxOffDiag) > 0) {
                        maxOffDiag = val;
                        p = i; q = j;
                    }
                }
            }
            
            if (maxOffDiag.compareTo(tolerance) < 0) break;
            
            // Calculate rotation
            Real Bpp = B[p][p];
            Real Bqq = B[q][q];
            Real Bpq = B[p][q];
            
            Real phi;
            Real diff = Bpp.subtract(Bqq);
            if (diff.abs().compareTo(tolerance) < 0) {
                phi = Real.PI.divide(Real.of(4));
            } else {
                Real atanArg = Bpq.multiply(Real.of(2)).divide(diff);
                // Math.atan equivalent for Real? Assuming implemented or fallback
                // If Real doesn't have atan, we might need approximation or conversion
                // Using double conversion for angle calculation (safe for small matrix)
                double angle = 0.5 * Math.atan(atanArg.doubleValue());
                phi = Real.of(angle);
            }
            
            Real c = Real.of(Math.cos(phi.doubleValue()));
            Real s = Real.of(Math.sin(phi.doubleValue()));
            
            // Update B
            Real c2 = c.multiply(c);
            Real s2 = s.multiply(s);
            Real sc2 = s.multiply(c).multiply(Real.of(2));
            
            B[p][p] = c2.multiply(Bpp).subtract(sc2.multiply(Bpq)).add(s2.multiply(Bqq));
            B[q][q] = s2.multiply(Bpp).add(sc2.multiply(Bpq)).add(c2.multiply(Bqq));
            B[p][q] = Real.ZERO; 
            B[q][p] = Real.ZERO;
            
             for (int i = 0; i < n; i++) {
                if (i != p && i != q) {
                    Real Bip = B[i][p];
                    Real Biq = B[i][q];
                    
                    B[i][p] = c.multiply(Bip).subtract(s.multiply(Biq));
                    B[p][i] = B[i][p];
                    
                    B[i][q] = s.multiply(Bip).add(c.multiply(Biq));
                    B[q][i] = B[i][q];
                }
                
                Real Vip = V[i][p];
                Real Viq = V[i][q];
                V[i][p] = c.multiply(Vip).subtract(s.multiply(Viq));
                V[i][q] = s.multiply(Vip).add(c.multiply(Viq));
            }
        }
        
        for(int i=0; i<n; i++) d[i] = B[i][i];
        
        JacobiResultReal res = new JacobiResultReal();
        res.eigenvalues = d;
        res.eigenvectors = V;
        return res;
    }
}
