/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
