package org.jscience.ml.cml.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * a 'cyclic vector' that is  element[0] succeeds element[nElem-1] where
 * nElem is size. Provides elements(startIndex) as Enumeration which iterates
 * through whole of Vector cyclicly. [Normal elements() is inherited from
 * Vector and starts from element[0]]. This class is not thread-safe. The
 * vector and its contents can be changed but Enumerations will be
 * invalidated.
 */
public class CyclicVector extends Vector {
/**
     * create with an empty vector
     */
    public CyclicVector() {
        this(new Vector());
    }

/**
     * create from a (non-empty) vector
     *
     * @param vector DOCUMENT ME!
     */
    public CyclicVector(Vector vector) {
        for (Enumeration e = vector.elements(); e.hasMoreElements();) {
            this.addElement(e.nextElement());
        }
    }

    /**
     * creates an Enumeration starting with (first) element containing
     * startObject.
     *
     * @param startObject DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Enumeration elements(Object startObject)
        throws IllegalArgumentException {
        int index = this.indexOf(startObject);

        if (index == -1) {
            throw new IllegalArgumentException("No such element: " +
                startObject.toString());
        }

        return new CyclicVectorEnumeration(this, index);
    }

    /**
     * creates an Enumeration starting at index
     *
     * @param startIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Enumeration elements(int startIndex) throws IllegalArgumentException {
        return new CyclicVectorEnumeration(this, startIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: CyclicVector size start");
            System.exit(0);
        }

        int size = Integer.parseInt(args[0]);
        int start = Integer.parseInt(args[1]);
        CyclicVector cyclicVector = new CyclicVector();

        for (int i = 0; i < size; i++) {
            // add an element and iterate through vector. If element is not yet
            // present we will get an exception
            cyclicVector.addElement("" + i);

            try {
                for (Enumeration e = cyclicVector.elements("" + start);
                        e.hasMoreElements();) {
                    System.out.println("obj>" + e.nextElement());
                }

                System.out.println("------------------");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        while (cyclicVector.size() > 0) {
            // remove elements from vector
            cyclicVector.removeElementAt(0);

            try {
                for (Enumeration e = cyclicVector.elements("" + start);
                        e.hasMoreElements();) {
                    System.out.println("obj>" + e.nextElement());
                }

                System.out.println("------------------");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class CyclicVectorEnumeration implements Enumeration {
    /** DOCUMENT ME! */
    Vector vector;

    /** DOCUMENT ME! */
    int startIndex;

    /** DOCUMENT ME! */
    int currentIndex;

    /** DOCUMENT ME! */
    boolean finished;

/**
     * Creates a new CyclicVectorEnumeration object.
     *
     * @param vector     DOCUMENT ME!
     * @param startIndex DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public CyclicVectorEnumeration(Vector vector, int startIndex)
        throws IllegalArgumentException {
        this.vector = vector;

        if ((startIndex < 0) || (startIndex > vector.size())) {
            throw new IllegalArgumentException("No such index: " + startIndex);
        }

        this.startIndex = startIndex;
        currentIndex = startIndex;
        finished = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreElements() {
        return !finished;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public Object nextElement() throws NoSuchElementException {
        if (finished) {
            throw new NoSuchElementException("finished");
        }

        if (vector.size() <= 0) {
            throw new NoSuchElementException("Empty vector");
        }

        Object object = vector.elementAt(currentIndex);
        currentIndex = (++currentIndex) % vector.size();

        if (!finished) {
            finished = (currentIndex == startIndex);
        }

        return object;
    }
}
