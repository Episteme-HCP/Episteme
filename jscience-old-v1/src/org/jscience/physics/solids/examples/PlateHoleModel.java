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
import org.jscience.physics.solids.constraint.GlobalConstraint;
import org.jscience.physics.solids.constraint.NodeConstraint;
import org.jscience.physics.solids.element.Quad2DElement;
import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.geom.AtlasVector;
import org.jscience.physics.solids.load.NodeLoad;
import org.jscience.physics.solids.material.PlaneStress;
import org.jscience.physics.solids.solution.LinearStaticSolution;

import java.io.IOException;


//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.apache.log4j.ConsoleAppender;
//import org.apache.log4j.SimpleLayout;
/**
 * Example problem of a plate with a hole in it.
 *
 * @author Wegge
 */
public class PlateHoleModel {
    /**
     * DOCUMENT ME!
     */
    private AtlasModel fem;

    /**
     * Creates a new PlateHoleModel object.
     */
    public PlateHoleModel() {
        //Model definitions stuff
        int numQuarterNodes = 10; //Number of nodes around one eighth of the hole;
        int depth = 8; //Number of nodes deep
        int depth2 = 4; //Number of nodes to "finish" the plate.
        double radius1 = 1.0; //Radius of hole
        double radius2 = 1.5; //Radius of "boss" around hole
        double a = 2; //Length of plate
        double force = 10; //Running load to applied on model

        AtlasPreferences pref = new AtlasPreferences();
        //Create a new AtlasModel
        fem = new AtlasModel("Plate With Hole", pref);

        int numnodes = 8 * (numQuarterNodes - 1);
        AtlasNode[][] nodes = new AtlasNode[depth + depth2 + 1][numnodes];

        double deg = (2 * Math.PI) / (numnodes);
        double rad = (radius2 - radius1) / (depth - 1);

        //Do ring around hole
        for (int j = 0; j < depth; j++) {
            for (int i = 0; i < numnodes; i++) {
                double xloc = (radius1 + (j * rad)) * Math.cos(i * deg);
                double yloc = (radius1 + (j * rad)) * Math.sin(i * deg);

                int id = (j * numnodes) + (i + 1);
                String sid = "INNER" + id;
                AtlasNode n = new AtlasNode(String.valueOf(sid), xloc, yloc, 0.0);
                fem.addObject(n);
                nodes[j][i] = n;
            }
        }

        //Do square around hole
        double edgeSpace = a / (numQuarterNodes - 1);
        int counter = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < (numQuarterNodes - 1); j++) {
                double xloc = 0;
                double yloc = 0;

                //Develop each eighth of the plate independantly
                switch (i) {
                case 0:
                    xloc = a;
                    yloc = j * edgeSpace;

                    break;

                case 1:
                    yloc = a;
                    xloc = a - (j * edgeSpace);

                    break;

                case 2:
                    yloc = a;
                    xloc = -1 * j * edgeSpace;

                    break;

                case 3:
                    xloc = -1 * a;
                    yloc = a - (j * edgeSpace);

                    break;

                case 4:
                    xloc = -1 * a;
                    yloc = -1 * j * edgeSpace;

                    break;

                case 5:
                    yloc = -1 * a;
                    xloc = (-1 * a) + (j * edgeSpace);

                    break;

                case 6:
                    yloc = -1 * a;
                    xloc = j * edgeSpace;

                    break;

                case 7:
                    xloc = a;
                    yloc = (-1 * a) + (j * edgeSpace);

                    break;
                }

                String edgeId = "EDGE" + counter;

                //System.out.println(i + " " + xloc + " , " + yloc);
                AtlasNode edgeNode = new AtlasNode(edgeId, xloc, yloc, 0.0);
                fem.addObject(edgeNode);
                nodes[depth + depth2][counter] = edgeNode;

                //Make interior nodes
                AtlasNode bossNode = nodes[depth - 1][counter];

                AtlasPosition[] interiorPoints = bossNode.interpolatePoints(depth2,
                        edgeNode);

                for (int k = 0; k < depth2; k++) {
                    int id = ((k + depth) * numnodes) + (counter + 1);
                    String sid = "OUTER" + id;

                    double[] loc = interiorPoints[k].getGlobalPosition();

                    AtlasNode newNode = new AtlasNode(String.valueOf(sid),
                            loc[0], loc[1], loc[2]);
                    fem.addObject(newNode);
                    nodes[depth + k][counter] = newNode;
                }

                counter++;
            }
        }

        //Rename the corner nodes for convenience
        for (int i = 0; i < 4; i++) {
            int id = (i * (numnodes / 4)) + (numnodes / 8);
            AtlasNode cn = fem.getNode("EDGE" + id);

            if (cn != null) {
                cn.setId("CORNER" + (i + 1));
            }
        }

        //Build elements
        int numElements = depth + depth2;

        for (int j = 0; j < numElements; j++) {
            for (int i = 0; i < numnodes; i++) {
                int id = (j * numnodes) + (i + 1);

                AtlasNode[] n = new AtlasNode[4];
                n[0] = nodes[j][i];
                n[1] = nodes[j + 1][i];

                if (i == (numnodes - 1)) {
                    n[2] = nodes[j + 1][0];
                    n[3] = nodes[j][0];
                } else {
                    n[2] = nodes[j + 1][i + 1];
                    n[3] = nodes[j][i + 1];
                }

                PlaneStress mat = new PlaneStress("Aluminum", 10.3e6, .33);
                fem.addObject(mat);

                Quad2DElement elem = new Quad2DElement(String.valueOf(id), n,
                        mat);
                fem.addObject(elem);
            }
        }

        //Add loads

        //Right hand side
        for (int i = numnodes / 4; i < (numnodes / 2); i++) {
            AtlasNode n = nodes[depth + depth2][i];
            AtlasVector fv = new AtlasVector(-1 * force, 0., 0.);
            fem.addObject(new NodeLoad(n.getId(), n, fv, new AtlasVector()));
        }

        //Left hand side
        for (int i = (3 * numnodes) / 4; i < numnodes; i++) {
            AtlasNode n = nodes[depth + depth2][i];
            AtlasVector fv = new AtlasVector(force, 0., 0.);
            fem.addObject(new NodeLoad(n.getId(), n, fv, new AtlasVector()));
        }

        //Add constraints
        GlobalConstraint gc = new GlobalConstraint("Z Constraint", 3);
        fem.addObject(gc);

        int[] dof = { 1, 2 };

        AtlasNode corner3 = fem.getNode("CORNER3");
        AtlasNode corner2 = fem.getNode("CORNER2");

        if ((corner3 != null) && (corner2 != null)) {
            fem.addObject(new NodeConstraint("Corner 12", corner3, dof));
            fem.addObject(new NodeConstraint("Corner 1", corner2, 1));
        }

        //Add a linear solution
        LinearStaticSolution sol = new LinearStaticSolution("Static Solution");
        fem.addObject(sol);
    }

    //static Logger logger = Logger.getRootLogger();
    /**
     * 
    DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //logger.addAppender(new ConsoleAppender(new SimpleLayout()));;
        AtlasPreferences pref = new AtlasPreferences();
        pref.setLoggingLevel(1);

        //
        // Start Logger
        //
        //		if (pref.getLoggingLevel() == 1) {
        //			PropertyConfigurator.configure("atlasinfo_log4j.properties");
        //		} else if (pref.getLoggingLevel() == 2) {
        //			PropertyConfigurator.configure("atlasdebug_log4j.properties");
        //		}
        //logger.info("\n");
        //logger.info("==================================");
        //logger.info(" ");
        //logger.info("  Beginning Atlas Solution Trace  ");
        //logger.info(" ");
        //logger.info("==================================");
        //logger.info("\n");

        //logger.info("Running Plate With Hole Problem.\n\n");
        PlateHoleModel phm = new PlateHoleModel();
        AtlasModel fem = phm.getModel();

        try {
            fem.writeXML("C:\\temp\\platehole.xml");
        } catch (IOException io) {
            io.printStackTrace();
        }

        try {
            LinearStaticSolution sol = (LinearStaticSolution) fem.getObject(LinearStaticSolution.TYPE,
                    "Static Solution");
            sol.solveModel();
        } catch (InvalidSolutionException fail) {
        }

        //logger.info("\n\nProblem has finished.");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasModel getModel() {
        return fem;
    }
}
