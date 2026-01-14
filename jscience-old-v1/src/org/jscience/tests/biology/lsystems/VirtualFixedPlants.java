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

/*
---------------------------------------------------------------------
VIRTUAL PLANTS
==============

University of applied sciences Biel

hosted by Claude Schwab

programmed by Rene Gressly

March - July 1999

---------------------------------------------------------------------
*/
package org.jscience.tests.biology.lsystems;

import com.sun.j3d.utils.applet.MainFrame;

import org.jscience.biology.lsystems.common.Log;
import org.jscience.biology.lsystems.fixed.LSystem;
import org.jscience.biology.lsystems.fixed.Turtle;

import java.io.File;

import java.util.Arrays;
import java.util.List;


/**
 * This is the class with the main() function. Here it is used to test and
 * run the other classed used to generate LSystems.<pre>
 * Usage:   java [-mx64m] VirtualPlants inputfile<p/>
 * Example: java -mx64m VirtualPlants bush2.lsy</pre>
 */
public class VirtualFixedPlants {
    /**
     * DOCUMENT ME!
     *
     * @param strArguments DOCUMENT ME!
     */
    public static void main(String[] strArguments) {
        try {
            Log.log("");
            Log.log("VirtualPlants");
            Log.log("-------------");
            Log.log("");
            Log.log("University of applied sciences, Biel Switzerland");
            Log.log("http://www.isbiel.ch");
            Log.log("");
            Log.log("Computer graphics project");
            Log.log("by Rene Gressly");
            Log.log("------------------------------------------------");
            Log.log("");

            String strFileName = new String();

            //make a list out of the argument string array
            List listArgs = Arrays.asList(strArguments);

            if (listArgs.isEmpty() == false) //the argument list is not empty
             {
                //the first argument must be the LSystem file name
                strFileName = (String) listArgs.get(0);

                //try to open and read the file
                Log.log("Reading file " + strFileName);

                File file = new File(strFileName);

                LSystem ls = new LSystem();

                // get information in the file and store the found values
                ls.build(file);
                Log.log("LSystem created");

                // derivate the lsystem
                ls.derivate();
                Log.log("Derivation succeeded");

                //parse lsystem and create 3D scene
                Log.log("building scene.... this might take a while");

                MainFrame mf = new MainFrame(new Turtle(ls), 700, 700);
            } else {
                Log.log("Usage: java [-mx64m] VirtualPlants inputfile");
                Log.log("");
                Log.log("Example:");
                Log.log("");
                Log.log("java -mx64m VirtualPlants bush2.lsy");
            }
        } catch (Exception e) //an error wich causes program to fail has occurred
         {
            //log exception message
            Log.log(e.toString());
        }
    }
}
