/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene/

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
*/

//import all own classed so they are compiled is modifyed

package org.jscience.tests.biology.lsystems;

import org.jscience.biology.lsystems.common.Log;
import org.jscience.biology.lsystems.growing.gui.Settings;

/**
 * The main class of the project. This is used to start the programm and thus holds
 * the main() function.<br>
 * Type:
 * <p/>
 * java -mx64m VirtualPlants
 * <p/>
 * on your console to start the program. Note that the VirtualPlants.class file has
 * to be in the directory you are in.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class VirtualGrowingPlants {
    /**
     * The main() function to start the application. This makes a new settings GUI.
     *
     * @param strArguments Arguments to pass from the command line. No arguments are
     *                     needed. Make your settings in the GUI.
     */
    public static void main(String[] strArguments) {
        try {
            //show the settings GUI
            Settings settings = new Settings();
            settings.setVisible(true);

            Log.log("");
            Log.log("VirtualPlants");
            Log.log("-------------");
            Log.log("");
            Log.log("University of applied sciences Biel, Switzerland");
            Log.log("http://www.hta-bi.bfh.ch");
            Log.log("");
            Log.log("Computer graphics diploma work");
            Log.log("by Rene Gressly 1999");
            Log.log("------------------------------------------------");
            Log.log("");
        } catch (Exception e) //an error wich causes program to fail has occurred
        {
            //log exception message
            Log.log("Exception catched in main()");
            Log.log(e.toString());
            Log.log(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
