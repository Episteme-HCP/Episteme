/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.VectorRapidoInt
 *         Emula the class java.util.Vector pero optimizada para
 *         arrays of elements class "int". Muy rapida, menor consumo of RAM.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 26/12/2000
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.util;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class VectorFastInt {
    /** DOCUMENT ME! */
    private static int INITIAL_SIZE = 5000;

    /** DOCUMENT ME! */
    private static int DELTA = 5000;

    /** DOCUMENT ME! */
    private int[] data;

    /** DOCUMENT ME! */
    private int pos;

    /** DOCUMENT ME! */
    private int max;

    /** DOCUMENT ME! */
    private int incr;

/**
     * constructor by default
     */
    public VectorFastInt() {
        this(INITIAL_SIZE, DELTA);
    }

/**
     * constructor
     *
     * @param initialSize DOCUMENT ME!
     * @param incr        DOCUMENT ME!
     */
    public VectorFastInt(int initialSize, int incr) {
        this.incr = incr;
        data = new int[initialSize];
        max = initialSize;
        pos = 0;
    }

    /**
     * add an element.
     *
     * @param value DOCUMENT ME!
     */
    public void addElement(int value) {
        data[pos++] = value;

        if (pos == max) {
            int[] d2 = new int[max + incr];

            System.arraycopy(data, 0, d2, 0, max);
            max += incr;

            data = d2;

            System.out.println("Redimensioning of VectorFastInt -> " + max);
        }
    }

    /**
     * add an element only if not contained yet.
     *
     * @param value DOCUMENT ME!
     */
    public void addElementNoRep(int value) {
        if (isElement(value)) {
            return;
        }

        addElement(value);
    }

    /**
     * returns the data contained in the container as an array of
     * int's.
     *
     * @return DOCUMENT ME!
     */
    public int[] array() {
        return data;
    }

    /**
     * returns the element at the given position
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int elementAt(int i) {
        return data[i];
    }

    /**
     * returns the data contained in the container as an array of int's
     * with the exact size.
     *
     * @return DOCUMENT ME!
     */
    public int[] getTruncatedArray() {
        int[] res = new int[pos];

        System.arraycopy(data, 0, res, 0, pos);

        return res;
    }

    /**
     * Sorts the elements of the array.
     *
     * @param tipo 1 for going from lower to bigger, -1 for going from bigger
     *        to lower.
     */
    public void sortArray(int tipo) {
        int t;

        // Bubble sort
        for (int j = 0; j < (pos - 1); j++)
            for (int i = j + 1; i < pos; i++)
                if ((tipo * (data[j] - data[i])) > 0) {
                    t = data[j];
                    data[j] = data[i];
                    data[i] = t;
                }
    }

    /**
     * returns <code>true</code> if the element is contained in the
     * container.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isElement(int value) {
        for (int j = 0; j < pos; j++)
            if (data[j] == value) {
                return true;
            }

        return false;
    }

    /**
     * Empty the vector quickly, but keeping the memory RAM. This
     * method es useful to reuse the array without freeing it and reserving
     * another.
     */
    public void reset() {
        pos = 0;
    }

    /**
     * returns the number of stored elements.
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return pos;
    }
}
