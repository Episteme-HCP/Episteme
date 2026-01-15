/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience;

import java.util.ResourceBundle;

/**
 * <p> This class represents the <b>J</b>Science library; it contains the
 * library optional {@link #initialize} method as well as a {@link #main}
 * method for versionning, self-tests, and performance analysis.</p>
 * <p> Initialization is performed automatically when quantities classes are
 * used for the first time or explicitely by calling the
 * {@link #initialize} static method.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class JScience {

  /**
   * Holds the version information.
   */
  public final static String VERSION = "@VERSION@";

  /**
   * Default constructor.
   */
  private JScience() {// Forbids derivation.
  }

  /**
   * The library {@link #main} method.
   * The archive <codejscience.jar</code> is auto-executable.
   * <ul>
   * <li><code>java [-cp javolution.jar] -jar jscience.jar version</code>
   * to output version information.</li>
   * </ul>
   *
   * @param args the option arguments.
   * @throws Exception if a problem occurs.
   */
  public static void main(String[] args) throws Exception {
    System.out.println("JScience - Java(TM) Tools and Libraries for the Advancement of Sciences");
    System.out.println("Version " + VERSION + " (http://jscience.org)");
    System.out.println("");
    if (args.length > 0) {
      if (args[0].equals("version")) {
        System.out.println("Version " + VERSION);
        return;
      } else if (args[0].equals("test")) {
        //testing();
        return;
      } else if (args[0].equals("perf")) {
        //benchmark();
        return;
      }
    }
    System.out
            .println("Usage: java -jar jscience.jar [arg]");
    System.out.println("where arg is one of:");
    System.out.println("    version (to show version information)");
    //System.out.println("    test    (to perform self-tests)");
    //System.out.println("    perf    (to run benchmark)");
  }

  /**
   * DOCUMENT ME!
   *
   * @param name DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static String getProperty(String name) {
    return ResourceBundle.getBundle("JScienceBundle").getString(name);
  }

}
