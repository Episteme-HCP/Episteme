/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.net.URL;

import java.util.Arrays;
import java.util.Comparator;


/**
 * A simple convenience class that sorts URLs by their external form
 * lexicographically.<p>Note that this implementation is inconsistent to
 * the equals() method.</p>
 *
 * @author Holger Antelmann
 */
public class URLSorter implements Comparator<URL> {
    /**
     * DOCUMENT ME!
     */
    static URLSorter sorter = new URLSorter();

    /**
     * DOCUMENT ME!
     *
     * @param urlList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static URL[] sort(URL[] urlList) {
        Arrays.sort(urlList, sorter);

        return urlList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url1 DOCUMENT ME!
     * @param url2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(URL url1, URL url2) {
        return url1.toExternalForm().compareTo(url2.toExternalForm());
    }
}
