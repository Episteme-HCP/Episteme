package org.jscience.computing.kmeans;

import java.util.Iterator;
import java.util.Vector;

/**
 * Represents a collection of samples in a cluster.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: Cluster.java,v 1.2 2007-10-21 21:08:00 virtualcall Exp $
 * @date April 19, 2003
 */
public class Cluster extends DataSet {

    /**
     * The coordinate of the average of the samples in this cluster
     */
    private Coordinate average;

    /**
     * A constant used when calculating gaussian density
     */
    private static final double normalConstant = Math.sqrt(2.0 * Math.PI);

    /**
     * Generates a cluster from given set vector of examples.
     *
     * @param collection The collection of samples in this cluster.
     * @param dimension  The dimension of this collection
     */
    public Cluster(Vector collection) {
        super(collection);

        average = calculateAverage();
    }

    /**
     * Finds the average of the coordinates in this cluster.
     *
     * @return A coordinate of the average of the coordinates in this cluster.
     */
    private Coordinate calculateAverage() {
        Coordinate ret = new Coordinate(dimension());

        for (Iterator it = iterator(); it.hasNext();) {
            Coordinate cor = (Coordinate) it.next();
            ret = ret.add(cor);
        }

        return ret.scale(1.0 / numSamples());
    }

    /**
     * @return The coordinate of the average of the coordinates in this
     *         cluster.
     */
    public Coordinate average() {
        return average;
    }

    /**
     * Get the standard deviation of this cluster in a given axis.
     *
     * @param axis The axis (0<=x<dimension) we want to find the standard
     *             deviation of in this cluster.
     */
    public double standardDeviation(int axis) {
        return standardDeviation(this, axis);
    }

    /**
     * Get the standard deviation of points in given dataset given the mean
     * of this cluster.
     *
     * @param dataset The dataset to compare.
     * @param axis    The axis (0<=x<dimension) we want to find the standard
     *                deviation of.
     */
    public double standardDeviation(DataSet dataset, int axis) {
        if (axis > dataset.dimension())
            throw new IllegalArgumentException();

        double sum = 0;
        for (Iterator it = dataset.iterator(); it.hasNext();) {
            Coordinate cor = (Coordinate) it.next();
            double diff = cor.get(axis) - average.get(axis);
            sum += diff * diff;
        }
        // TODO: some sources say to divide by N, some by N-1.  why?!
        sum /= dataset.numSamples();

        return Math.sqrt(sum);
    }

    /**
     * Finds the density of a sample given a normal curve described by given
     * mean and standard deviation.
     *
     * @param mean   The mean value.
     * @param sample The current sample coordinate.
     * @param stddev The standard deviation.
     * @return The likelihood of the sample given this normal curve.
     */
    private double normalDensity(double sample, double mean, double stddev) {
        double diff = sample - mean;
        double top = Math.exp(-(diff * diff / (2 * stddev * stddev)));
        double bottom = normalConstant * stddev;
        return top / bottom;
    }

    /**
     * Finds the loglikelihood of this sample given the cluster.
     *
     * @param sample The sample to test against this cluster.
     * @return The log likelihood of this data given the cluster.
     */
    public double likelihood(Coordinate sample) {
        double prod = 1.0;

        for (int i = 0; i < sample.dimension(); i++) {
            double val = normalDensity(sample.get(i), average().get(i), standardDeviation(i));

            prod *= val;
        }
        return prod;
    }

}
