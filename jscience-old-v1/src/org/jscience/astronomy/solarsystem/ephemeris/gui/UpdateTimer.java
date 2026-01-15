//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class UpdateTimer implements Runnable {
    /** DOCUMENT ME! */
    CSpherePanel app;

    /** DOCUMENT ME! */
    boolean suicide;

    /** DOCUMENT ME! */
    private final int sleep_secs = 5;

/**
     * Creates a new UpdateTimer object.
     *
     * @param cspherepanel DOCUMENT ME!
     */
    UpdateTimer(CSpherePanel cspherepanel) {
        suicide = false;
        app = cspherepanel;
        (new Thread(this, "UpdateTimerThread")).start();
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        suicide = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        while (!suicide) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException _ex) {
            }

            app.updateTime();
        }
    }
}
