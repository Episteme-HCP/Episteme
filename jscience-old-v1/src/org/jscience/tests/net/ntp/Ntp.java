package org.jscience.tests.net.ntp;

import org.jscience.tests.net.ntp.gui.TimeFrame;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class Ntp {
    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] argv) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Couldn't get system specific look and feel");
        }

        JWindow ntpFrame = new TimeFrame();
        ntpFrame.setVisible(true);
    }
}
