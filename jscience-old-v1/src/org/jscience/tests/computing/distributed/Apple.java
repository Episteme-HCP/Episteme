/*
 * Apple.java
 *
 * Created on 21 July 2003, 18:53
 */
package org.jscience.tests.distributed;

import org.jscience.computing.distributed.InteractiveTask;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Apple implements InteractiveTask {
    /** DOCUMENT ME! */
    Cream c;

/**
     * Creates a new instance of Apple
     */
    public Apple() {
        try {
            c = new Cream();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object p) {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object p) throws InterruptedException {
        System.out.println("No no");

        return null;
    }

    /**
     * Used to send input to the task
     *
     * @param paramsAndWhat this could be anything, either a single object if
     *        the task knows what to do with it, or a packet with
     *        variable->value pairs..
     */
    public void set(Object paramsAndWhat) {
    }
}
