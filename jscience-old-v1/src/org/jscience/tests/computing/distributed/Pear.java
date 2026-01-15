/*
 * Pear.java
 *
 * Created on 21 July 2003, 20:34
 */
package org.jscience.tests.distributed;

import jaga.Genotype;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Pear implements org.jscience.computing.distributed.InteractiveTask {
/**
     * Creates a new instance of Pear
     */
    public Pear() {
    }

    /**
     * Used to get output from the task
     *
     * @param params can be null if this task doesn't need to know WHAT it has
     *        to output
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object params) {
        Genotype x = new Genotype("0110");
        x.setFitness(0.4);

        return x;
    }

    /**
     * Totally customizable method say throws InterruptedException?
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object params) throws InterruptedException {
        while (true) {
            Thread.currentThread().sleep(1000);
        }
    }

    /**
     * Used to send input to the task
     *
     * @param paramsAndWhat this could be anything, either a single object if
     *        the task knows what to do with it, or a packet with
     *        variable->value pairs..
     */
    public void set(Object paramsAndWhat) {
        System.out.println(paramsAndWhat);

        Genotype y = (Genotype) paramsAndWhat;
        Genotype z = new Genotype("000");
        Class x;
        z.setFitness(0.6);
        System.out.println(y + "=y , z=" + z + " " + y.compareTo(z));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Pear I";
    }
}
