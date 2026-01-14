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
