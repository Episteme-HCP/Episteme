package org.jscience.computing.kmeans;

import java.util.StringTokenizer;

/**
 * Represents a single sample of data.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: Coordinate.java,v 1.2 2007-10-21 21:08:01 virtualcall Exp $
 * @date April 17, 2003
 */
public class Coordinate {

    /**
     * The set of coordinates specifying this sample of data
     */
    double[] coordinates;

    /**
     * Generates a single sample of data given a string description.
     * <p/>
     * String should be a sequence of tokens, with one real coordinate per
     * token.
     *
     * @param sample A string containing a sequence of tokens, with one
     *               real coordinate per token.
     */
    public Coordinate(String sample) {
        StringTokenizer tok = new StringTokenizer(sample);

        coordinates = new double[tok.countTokens()];

        int i = 0;
        while (tok.hasMoreTokens()) {
            coordinates[i] = Double.parseDouble(tok.nextToken());
            i++;
        }
    }

    /**
     * Builds a new coordinate from a list of doubles.
     *
     * @param coordinates A list of coordinates.
     */
    public Coordinate(double[] coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Constructs a coordinate initialized to origin in given dimension.
     *
     * @param dimension The dimension of the coordinate to create.
     */
    public Coordinate(int dimension) {
        this.coordinates = new double[dimension];
        for (int i = 0; i < coordinates.length; i++)
            coordinates[i] = 0.0;
    }

    /**
     * @param othercoordinate The other coordinate to compare this one to.
     * @return True if two coordinates are equal, false otherwise.
     */
    public boolean equals(Object othercoordinate) {
        try {
            Coordinate other = (Coordinate) othercoordinate;
            if (coordinates.length != other.coordinates.length)
                return false;
            for (int i = 0; i < coordinates.length; i++)
                if (coordinates[i] != other.coordinates[i])
                    return false;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns the dimension this sample exists in.
     *
     * @return The dimensionality of this sample.
     */
    public int dimension() {
        return coordinates.length;
    }

    /**
     * For accessing the data in this sample.
     *
     * @param index The index to extract.
     * @return The data in this sample in given dimension.
     */
    public double get(int index) {
        return coordinates[index];
    }

    /**
     * Returns the squared euclidean distance between this point and another.
     *
     * @param other The other point.
     * @return The distance between this point and the other one.
     */
    public double distance(Coordinate other) {
        if (other.coordinates.length != coordinates.length)
            throw new IllegalArgumentException();

        double dist = 0;

        for (int i = 0; i < coordinates.length; i++) {
            double thisdist = coordinates[i] - other.coordinates[i];
            dist += thisdist * thisdist;
        }
        return Math.sqrt(dist);
    }

    /**
     * Adds this coordinate to another one.
     *
     * @param other The other operand.
     * @return A coordiante containing the sum.
     */
    public Coordinate add(Coordinate other) {
        if (other.coordinates.length != coordinates.length)
            throw new IllegalArgumentException();

        double[] newcor = new double[coordinates.length];

        for (int i = 0; i < coordinates.length; i++)
            newcor[i] = coordinates[i] + other.coordinates[i];

        return new Coordinate(newcor);
    }

    /**
     * Scales the coordinate using scalar multiplication.
     *
     * @param scale The value to scale all dimensions by.
     * @return The scaled value.
     */
    public Coordinate scale(double scale) {
        double[] coords = new double[coordinates.length];
        for (int i = 0; i < coordinates.length; i++)
            coords[i] = coordinates[i] * scale;
        return new Coordinate(coords);
    }

    /**
     * @return The string representation of this sample.
     */
    public String toString() {
        String ret = "<";
        for (int i = 0; i < coordinates.length; i++) {
            ret += coordinates[i];
            if (i < coordinates.length - 1)
                ret += ",";
        }
        return ret + ">";
    }
}
