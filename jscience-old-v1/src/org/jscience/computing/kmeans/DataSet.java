package org.jscience.computing.kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * Represents the dataset of samples.
 * A container for Data objects.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: DataSet.java,v 1.2 2007-10-21 21:08:01 virtualcall Exp $
 * @date April 17, 2003
 */
public class DataSet {

    /**
     * The coordinates in this dataset
     */
    protected Vector dataset;

    /**
     * The dimension of the samples in this dataset
     */
    private int dimension;

    /**
     * Constructs a dataset from a vector of samples.
     *
     * @param collection The collection of samples in this dataset.
     */
    public DataSet(Vector collection) {
        this.dimension = -1;
        this.dataset = new Vector();

        for (Iterator it = collection.iterator(); it.hasNext();) {
            Coordinate coord = (Coordinate) it.next();
            if (dimension == -1) {
                dimension = coord.dimension();
            } else {
                if (coord.dimension() != dimension)
                    throw new IllegalArgumentException();
            }
            dataset.add(coord);
        }
    }

    /**
     * Constructs a new dataset by reading a description from a file.
     *
     * @param filename The filename containing the dataset.
     */
    public DataSet(String filename) throws FileNotFoundException, IOException,
            Exception {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            Vector fileLines = new Vector();

            String line = in.readLine();
            // need to read a line to find out dim
            Coordinate data = new Coordinate(line);
            dimension = data.dimension();

            while (null != line) { // read the rest
                fileLines.add(line);
                line = in.readLine();
            }
            in.close();

            dataset = new Vector(fileLines.size());

            for (Iterator it = fileLines.iterator(); it.hasNext();) {
                line = (String) it.next();

                data = new Coordinate(line);

                if (dimension != data.dimension())
                    throw new Exception("File contains samples of different " +
                            "dimensions");
                dataset.add(data);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format of datafile: " +
                    filename);
        }

    }

    /**
     * Access the number of samples in the dataset.
     *
     * @return The number of samples in the dataset.
     */
    public int numSamples() {
        return dataset.size();
    }

    /**
     * Access the dimension of the samples in the dataset.
     *
     * @return The dimensionality of the samples in the dataset.
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Get an iterator for this dataset.
     *
     * @return An iterator of the Data items in this collection.
     */
    public Iterator iterator() {
        return new DataSetIterator(this);
    }

    /**
     * Get an iterator for this dataset that iterates in a random order.
     *
     * @return A random iterator of the dataset items.
     */
    public Iterator randomIterator() {
        return new DataSetRandomIterator(this);
    }

    /**
     * @return The string representation of this dataset.
     */
    public String toString() {
        String ret = "";
        for (Iterator it = iterator(); it.hasNext();) {
            ret += ((Coordinate) it.next()) + "\n";
        }
        return ret;
    }

    /**
     * An iterator for datasets.
     */
    private class DataSetIterator implements Iterator {
        /**
         * The next index to iterate
         */
        int next;

        /**
         * The dataset being iterated
         */
        DataSet set;

        /**
         * The constructor.
         *
         * @param dataset The dataset to iterate.
         */
        public DataSetIterator(DataSet dataset) {
            this.set = dataset;
            this.next = 0;
        }

        /**
         * Check if there are more items to iterate.
         *
         * @return True if there are more items to iterate.
         */
        public boolean hasNext() {
            return next < set.dataset.size();
        }

        /**
         * Access the next object in the iterator.
         *
         * @return The next item being iterated.
         */
        public Object next() {
            next++;
            return set.dataset.get(next - 1);
        }

        /**
         * An optional remove function defined by interface.
         * <p/>
         * I choose not to implement it.
         */
        public void remove() {
        }
    }

    /**
     * An iterator for datasets that iterates in random order.
     */
    private class DataSetRandomIterator implements Iterator {
        /**
         * A vector of indicies remaining to iterate
         */
        private Vector indices;

        /**
         * The set to iterate
         */
        private DataSet set;

        /**
         * A random number generator
         */
        private Random rand;

        /**
         * Constructs a new random interator.
         *
         * @param set The set to iterate.
         */
        public DataSetRandomIterator(DataSet set) {
            this.set = set;
            indices = new Vector(set.numSamples());
            for (int i = 0; i < set.dataset.size(); i++)
                indices.add(new Integer(i));
            rand = new Random();
        }

        /**
         * @return The next randomly chosen object in the set.
         */
        public Object next() {
            int randindex = rand.nextInt(indices.size());
            int setindex = ((Integer) indices.remove(randindex)).intValue();
            return set.dataset.get(setindex);
        }

        /**
         * @return True if there are more objects to iterate in the set.
         */
        public boolean hasNext() {
            return !indices.isEmpty();
        }

        /**
         * An optional remove function defined by the interface.
         * <p/>
         * I choose not to implement it.
         */
        public void remove() {
        }
    }
}
