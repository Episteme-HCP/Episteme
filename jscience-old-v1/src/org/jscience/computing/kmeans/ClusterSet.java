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

package org.jscience.computing.kmeans;

import java.util.Iterator;

/**
 * Represents a collection of clusters, and allows operations on them.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: ClusterSet.java,v 1.2 2007-10-21 21:08:00 virtualcall Exp $
 * @date April 19, 2003
 */
public class ClusterSet {
    /**
     * The clusters.
     */
    private Cluster[] clusters;

    /**
     * The log likelihood of this cluster.
     * Set to -1 if not yet initialized.  Set to -1 once initialized.
     */
    private double loglikelihood;

    /**
     * Constructs an empty clusterset of given length.
     */
    public ClusterSet(int numClusters) {
        clusters = new Cluster[numClusters];
        loglikelihood = -1;
    }

    /**
     * Get the size of this clusterset (not neccessarily the number of
     * clusters contained.)
     *
     * @return The size of this clusterset.
     */
    public int numClusters() {
        return clusters.length;
    }

    /**
     * Set a given cluster in the set.
     *
     * @param i       The index of the cluster to set.
     * @param cluster The cluster to place at that location.
     */
    public void set(int i, Cluster cluster) {
        clusters[i] = cluster;
    }

    /**
     * Accessor.
     *
     * @param i The index of the cluster to access.
     * @return The wanted cluster.
     */
    public Cluster get(int i) {
        return clusters[i];
    }

    /**
     * Get an iterator for this clusterset.
     *
     * @return A cluster iterator.
     */
    public Iterator iterator() {
        return new ClusterIterator(this);
    }

    /**
     * Helper function for finding the log2 of a number.
     *
     * @param x The number to get log2 of.
     * @return The log2 of given number.
     */
    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Get the prior probability of a cluster.
     *
     * @param i The number of the cluster to examine.
     * @return The prior probablity of this cluster.
     */
    public double prior(int i) {
        if (i < 0 || i >= clusters.length)
            throw new IllegalArgumentException();

        // get value for all clusters.
        int totalnumsamples = 0;
        for (Iterator it = iterator(); it.hasNext();) {
            Cluster clust = (Cluster) it.next();
            totalnumsamples += clust.numSamples();
        }

        Cluster thisclust = (Cluster) clusters[i];

        return thisclust.numSamples() / ((double) totalnumsamples);
    }

    /**
     * @return The dimension of the clusters in this set.
     */
    public int dimension() {
        return clusters[0].dimension();
    }

    /**
     * Helper function that computes the log likelihood of this model
     * given all the data.
     *
     * @param dataset All the samples.
     * @return The loglikelihood of this model.
     */
    private void calcLogLikelihood(DataSet dataset) {

        // see the likelihood of each sample from full dataset against
        // the likelihood of each cluster.
        double sum = 0;

        for (Iterator datait = dataset.iterator(); datait.hasNext();) {
            Coordinate sample = (Coordinate) datait.next();

            int i = 0;
            double exsum = 0;
            for (Iterator it = iterator(); it.hasNext();) {
                Cluster clust = (Cluster) it.next();
                exsum += clust.likelihood(sample) * prior(i);
                i++;
            }
            sum += log2(exsum);
        }
        this.loglikelihood = sum;
    }

    /**
     * Get the log likelihood of this model given all the data.
     *
     * @param dataset All the samples.
     * @return The log likelihood of this model.
     */
    public double logLikelihood(DataSet dataset) {
        if (loglikelihood == -1)
            calcLogLikelihood(dataset);

        return loglikelihood;
    }

    /**
     * Get the description length of this model.
     *
     * @param dataset All the samples.
     * @return The description length of this model.
     */
    public double descriptionLength(DataSet dataset) {
        if (loglikelihood == -1)
            calcLogLikelihood(dataset);
        double modeldesc = 0;
        for (Iterator it = iterator(); it.hasNext();) {
            Cluster clust = (Cluster) it.next();
            for (int i = 0; i < dimension(); i++)
                modeldesc += -log2(clust.standardDeviation(dataset, i));
        }
        return -loglikelihood - modeldesc;
    }

    /**
     * An inner class for iterating the clusters in a clusterset.
     */
    private class ClusterIterator implements Iterator {
        /**
         * The next index to iterate
         */
        private int next;

        /**
         * The clusters being iterated
         */
        private ClusterSet cset;

        /**
         * Generates a new iterator
         *
         * @param cset The clusterset.
         */
        public ClusterIterator(ClusterSet cset) {
            this.next = 0;
            this.cset = cset;
        }

        /**
         * @return The next object to iterate.
         */
        public Object next() {
            next++;
            return cset.clusters[next - 1];
        }

        /**
         * @return True if there are more objects to iterate
         */
        public boolean hasNext() {
            return next < cset.clusters.length;
        }

        /**
         * Optional method defined by interface that we choose not to
         * implement.
         */
        public void remove() {
        }
    }

}
