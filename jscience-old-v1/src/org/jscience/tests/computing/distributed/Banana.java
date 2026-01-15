/*
 * Banana.java
 *
 * Created on 21 July 2003, 16:56
 */
package org.jscience.tests.distributed;

import org.jscience.computing.distributed.InteractiveTask;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Banana implements InteractiveTask {
    /** DOCUMENT ME! */
    String runLabel = "I";

    /** DOCUMENT ME! */
    String showLabel = "I";

    /** DOCUMENT ME! */
    Chocolate choco;

/**
     * Creates a new Banana object.
     */
    public Banana() {
        choco = new Chocolate();
    }

/**
     * Creates a new instance of Banana
     *
     * @param rl DOCUMENT ME!
     * @param sl DOCUMENT ME!
     */
    public Banana(String rl, String sl) {
        this();
        runLabel = rl;
        showLabel = sl;
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
        return "my banana " + this;
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
        System.out.println("Babanana " + runLabel);

        return "out of bananas";
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Banana " + showLabel + " and " + choco;
    }
}
