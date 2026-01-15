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
class StarsArray {
    /** DOCUMENT ME! */
    protected int count;

    /** DOCUMENT ME! */
    protected StarPoint[] stars;

/**
     * Creates a new StarsArray object.
     */
    public StarsArray() {
        count = 0;
    }

/**
     * Creates a new StarsArray object.
     *
     * @param i DOCUMENT ME!
     */
    public StarsArray(int i) {
        count = 0;
        stars = new StarPoint[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param starpoint DOCUMENT ME!
     */
    public void addStar(StarPoint starpoint) {
        stars[count++] = starpoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StarPoint getStar(int i) {
        return stars[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    protected void error(String s) {
        System.out.println(s);
    }
}
