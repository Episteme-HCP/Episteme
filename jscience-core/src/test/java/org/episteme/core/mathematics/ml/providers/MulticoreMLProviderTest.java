package org.episteme.core.mathematics.ml.providers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MulticoreMLProviderTest {

    @Test
    public void testKMeans() {
        MulticoreMLProvider provider = new MulticoreMLProvider();
        
        // Simple 2D data: 2 clusters, one around (-10,-10), one around (10,10)
        double[][] data = {
            {-10, -10}, {-9, -11}, {-11, -9},
            {10, 10}, {9, 11}, {11, 9}
        };

        int[] assignments = provider.kMeans(data, 2, 10);
        
        // Verify clustering: first 3 should be same, last 3 should be same, and different from each other
        int cluster1 = assignments[0];
        int cluster2 = assignments[3];
        
        assertEquals(cluster1, assignments[1]);
        assertEquals(cluster1, assignments[2]);
        assertEquals(cluster2, assignments[4]);
        assertEquals(cluster2, assignments[5]);
        assertNotEquals(cluster1, cluster2);
    }

    @Test
    public void testPCA() {
        MulticoreMLProvider provider = new MulticoreMLProvider();
        
        // Synthetic data perfectly correlated: y = x
        // Variance along (1,1) should be max. Variance along (-1,1) should be 0.
        int n = 100;
        double[][] data = new double[n][2];
        for(int i=0; i<n; i++) {
            data[i][0] = i;
            data[i][1] = i; 
        }
        
        double[][] projected = provider.pca(data, 2);
        
        // The first component should capture the variance.
        // The second component should be near zero (since points lie on a line).
        
        double var1 = variance(projected, 0);
        double var2 = variance(projected, 1);
        
        assertTrue(var1 > 1.0, "Principal component should have significant variance");
        assertTrue(var2 < 1e-5, "Second component should have near-zero variance for perfectly correlated data: " + var2);
    }
    
    private double variance(double[][] data, int col) {
        double mean = 0;
        for(double[] row : data) mean += row[col];
        mean /= data.length;
        
        double sum = 0;
        for(double[] row : data) {
            double diff = row[col] - mean;
            sum += diff*diff;
        }
        return sum / (data.length - 1);
    }
}
