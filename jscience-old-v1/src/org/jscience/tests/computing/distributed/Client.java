/*
 * Client.java
 *
 * Created on 21 April 2004, 18:59
 */
package org.jscience.tests.distributed;

import java.rmi.Naming;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Client {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        //java.rmi.server.RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory());
        String hostname = "localhost";

        Object raw = Naming.lookup("//localhost/Bob");

        System.out.println("raw object is " + raw);

        Getter s = (Getter) raw;
        System.out.println("Got remote object");
    }
}
