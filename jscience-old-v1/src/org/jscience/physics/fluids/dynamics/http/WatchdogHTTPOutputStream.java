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

package org.jscience.physics.fluids.dynamics.http;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class WatchdogHTTPOutputStream extends Thread {
    /** DOCUMENT ME! */
    HTTPOutputStream stream;

/**
     * Creates a new WatchdogHTTPOutputStream object.
     *
     * @param s DOCUMENT ME!
     */
    WatchdogHTTPOutputStream(HTTPOutputStream s) {
        stream = s;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        while (stream.alive) {
            try {
                if (stream.enviado) {
                    stream.enviado = false;
                } else {
                    stream.out.close();
                    stream.alive = false;
                    System.out.println("HTTPOutputStream.Watchdog()");
                }

                sleep(HTTPOutputStream.WATCHDOG_TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
