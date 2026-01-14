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

package org.jscience.physics.solids.examples;

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.AtlasPreferences;
import org.jscience.physics.solids.InvalidSolutionException;
import org.jscience.physics.solids.constraint.NodeConstraint;
import org.jscience.physics.solids.element.Rod2DElement;
import org.jscience.physics.solids.geom.AtlasVector;
import org.jscience.physics.solids.load.NodeLoad;
import org.jscience.physics.solids.solution.LinearStaticSolution;

import java.io.IOException;

//import org.apache.log4j.Logger;

//import org.apache.log4j.PropertyConfigurator;
/**
 * Example problem of an extremely simple rod.
 * <p/>
 * It is ten inches long, with constraint on one end and an axial load at the other.
 *
 * @author Wegge
 */
public class SimpleRodModel {

    //static Logger AtlasLogger = Logger.getLogger((SimpleRodModel.class).getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AtlasPreferences pref = new AtlasPreferences();
        pref.setLoggingLevel(1);

        //
        // Start Logger
        //
        if (pref.getLoggingLevel() == 1) {
            //PropertyConfigurator.configure("atlasinfo_log4j.properties");
        } else if (pref.getLoggingLevel() == 2) {
            //PropertyConfigurator.configure("atlasdebug_log4j.properties");
        }
        //AtlasLogger.info("\n");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("  Beginning Atlas Solution Trace  ");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info("\n");
        //AtlasLogger.info("Running Simple Rod Example Problem.\n\n");

        SimpleRodModel srm = new SimpleRodModel();

        AtlasModel fem = srm.getModel();

        //AtlasLogger.info( fem.toString() );

        try {
            fem.writeXML("C:\\temp\\model.xml");
        } catch (IOException io) {
            io.printStackTrace();
        }
        //AtlasLogger.info("Done");
        System.exit(0);

        /*
        * This is the end of the model generation
        */

        //Solve the model... one line of code
        try {

            //Add linear static solution
            LinearStaticSolution solution = new LinearStaticSolution("Test Solution");
            fem.addObject(solution);

            solution.solveModel();

        } catch (InvalidSolutionException bomb) {
            //AtlasLogger.error("FATAL: " + bomb.getMessage());
        }

        //Then what?

        //AtlasLogger.info("\n\nProblem has finished.");


    }


    private AtlasModel fem;

    public SimpleRodModel() {

        //Model definitions stuff
        int numelems = 2; //Number of rod elements;
        double area = 1.0; //Area of rods
        double modulus = 10.3e6; //Modulus of rods
        int numnodes = 3;

        AtlasPreferences pref = new AtlasPreferences();
        //Create a new AtlasModel
        fem = new AtlasModel("Simple Rod Model", pref);

        /*
        * This is the beginning of the model generation.
        * Normally, this would be handled by reading in a file, etc..
        */

        //Populate it with nodes - each node is one inch long and spaced on X axis
        AtlasNode[] nodes = new AtlasNode[numnodes];
        String sid;
        double xloc;
        double yloc;
        nodes[0] = new AtlasNode("0", 0.0, 0.0, 0.0);
        fem.addObject(nodes[0]);
        nodes[1] = new AtlasNode("1", 5.0, 5.0, 0.0);
        fem.addObject(nodes[1]);
        nodes[2] = new AtlasNode("2", 0.0, 5.0, 0.0);
        fem.addObject(nodes[2]);

        //Populate it with elements
        fem.addObject(new Rod2DElement("0", nodes[0], nodes[1], area, modulus));
        fem.addObject(new Rod2DElement("1", nodes[2], nodes[1], area, modulus));

        //Add constraints - first node at 0,0,0 is brickwalled
        int[] dof = {1, 2};
        fem.addObject(new NodeConstraint("Brickwall1", nodes[0], dof));
        fem.addObject(new NodeConstraint("Brickwall2", nodes[2], dof));
        int[] dof2 = {3, 4, 5, 6};
        fem.addObject(new NodeConstraint("C1", nodes[0], dof2));
        fem.addObject(new NodeConstraint("C2", nodes[1], dof2));
        fem.addObject(new NodeConstraint("C3", nodes[2], dof2));

        //Add loads - last node has axial force on it
        AtlasVector force = new AtlasVector(100.0, 100.0, 0.0);
        AtlasVector moment = new AtlasVector(0.0, 0.0, 0.0);
        fem.addObject(new NodeLoad("Axial Load", nodes[1], force, moment));


    }

    public AtlasModel getModel() {
        return fem;
    }

}
