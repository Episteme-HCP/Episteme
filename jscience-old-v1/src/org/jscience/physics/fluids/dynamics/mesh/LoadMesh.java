/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.LoadMesh
 *         Load meshs desde disco. Loaddor universal.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 28/07/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.mesh;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LoadMesh {
    /** version of is class */
    public static final String VERSION = "v2.0";

    /** DOCUMENT ME! */
    private static final int IMPOSE_SPEED = 0;

    /** DOCUMENT ME! */
    private static final int FLUID_EXIT = 1;

    /** DOCUMENT ME! */
    private static final int MAGNUS = 2;

    /** DOCUMENT ME! */
    private static final int SLIDING = 3;

    /** DOCUMENT ME! */
    private static final int PARABOLIC_PROFILE = 4;

    /** DOCUMENT ME! */
    private static final int DRAGLIFT = 5;

    /** DOCUMENT ME! */
    private static final int INK = 10;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private int dimension;

    /** DOCUMENT ME! */
    private int totalNodes;

    /** DOCUMENT ME! */
    private int elementGrade;

    /** DOCUMENT ME! */
    private int totalElements;

    /** DOCUMENT ME! */
    private int[][] nodes;

    /** DOCUMENT ME! */
    private double[][] coord;

    /** DOCUMENT ME! */
    private Vector nodesDirichlet;

    /** DOCUMENT ME! */
    private Vector nodesMagnus;

    /** DOCUMENT ME! */
    private Vector nodesNeumann;

    /** DOCUMENT ME! */
    private Vector valuesNeumann;

    /** DOCUMENT ME! */
    private Vector nodesDragLift;

    /** DOCUMENT ME! */
    private Vector nodesSlip;

    /** DOCUMENT ME! */
    private Vector nodesPerfilDirichlet;

    /** DOCUMENT ME! */
    private Vector nodesInk;

    /** DOCUMENT ME! */
    private Vector valuesMagnus;

    /** DOCUMENT ME! */
    private Vector[] valuesDirichlet;

/**
     * Creates a new LoadMesh object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public LoadMesh(KernelADFC kadfc) {
        kernel = kadfc;

        nodesDirichlet = new Vector();
        nodesMagnus = new Vector();
        valuesMagnus = new Vector();
        nodesNeumann = new Vector();
        valuesNeumann = new Vector();
        nodesDragLift = new Vector();
        nodesSlip = new Vector();
        nodesPerfilDirichlet = new Vector();
        nodesInk = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     */
    public void loadGidDataFile(String filename) {
        StreamTokenizer tok = null;
        InputStreamReader in;
        int token;
        int interval;

        try {
            // We first open the InputStreamReader to be able to close it at the end.
            kernel.out("<B>loadGidDataFile:</B> reading <I>" + filename +
                "</I>");
            in = new InputStreamReader((filename.startsWith("http://")
                    ? new java.net.URL(filename).openStream()
                    : new FileInputStream(filename)));

            tok = new StreamTokenizer(in);

            // configure tokenizer
            tok.eolIsSignificant(false);
            tok.slashSlashComments(true);
            tok.slashStarComments(true);
            tok.parseNumbers();

            kernel.setLoadProgress(0);

            //////////////////////// COORDINATE LOADER //////////////
            // Loads dimension and totalNodes
            dimension = readInt(tok);
            totalNodes = readInt(tok);

            //
            //System.out.println("D="+dimension+" N="+totalNodes);
            // check
            if ((dimension != 2) && (dimension != 3)) {
                throw new Exception();
            }

            // reserve memory RAM
            coord = new double[dimension][totalNodes];

            valuesDirichlet = new Vector[dimension];

            for (int j = 0; j < dimension; j++)
                valuesDirichlet[j] = new Vector();

            // from time to time refresh porcentual information
            interval = totalNodes / 40;

            // Loading
            for (int i = 0; i < totalNodes; i++) {
                if ((i % interval) == 0) {
                    kernel.setLoadProgress((40 * i) / totalNodes);
                }

                if (readInt(tok) != (i + 1)) {
                    System.out.println("Synchronism failure at node " +
                        (i + 1) + ", top was " + totalNodes);
                }

                for (int j = 0; j < dimension; j++)
                    coord[j][i] = readDouble(tok);

                //

                /*  if(i % 1000 == 0)
                  {
                      for(int j=0; j<dimension; j++)
                          System.out.print(" "+coord[j][i]);
                
                      System.out.println("");
                  }*/
            }

            kernel.setLoadProgress(40);

            //////////////////////// LOADER OF NODE INDEXES /////////
            // Loading elementGrade y totalElements
            elementGrade = readInt(tok);
            totalElements = readInt(tok);

            //
            //System.out.println("G="+gradoElement+" E="+totalElementos);
            // check
            if ((elementGrade != 6) && (elementGrade != 10)) {
                kernel.newErrorFatalDialog("The element grade is incorrect\n" +
                    "Loading of the mesh can not continue\n" +
                    "Ensure of choosing triangular elements\n" +
                    "quadratics in your mesh.");
                System.exit(0);
            }

            // reserve RAM
            nodes = new int[elementGrade][totalElements];

            // from time to time refresh porcentual information
            interval = totalElements / 50;

            // Loading
            for (int i = 0; i < totalElements; i++) {
                if ((i % interval) == 0) {
                    kernel.setLoadProgress(40 + ((50 * i) / totalElements));
                }

                if (readInt(tok) != (i + 1)) {
                    System.out.println("Synchronism failure at node " +
                        (i + 1) + ", top " + totalElements);
                    System.exit(0);
                }

                for (int j = 0; j < elementGrade; j++)
                    nodes[j][i] = readInt(tok) - 1;

                //

                /*  if(i % 1000 == 0)
                  {
                      for(int j=0; j<gradoElement; j++)
                          System.out.print(" "+nodes[j][i]);
                
                      System.out.println("");
                  }*/
            }

            //
            /*
                for(int j=0; j<gradoElement; j++)
                    System.out.print(" "+nodes[j][totalElements-1]);
            
                System.out.println("");
             */
            kernel.setLoadProgress(90);

            ////////////////////////  CONTOUR LOADER ////////////////
            int node;

            ////////////////////////  CONTOUR LOADER ////////////////
            int type;
            double value;

            while ((node = readInt(tok)) != -1) {
                type = readInt(tok);

                // value = leeDouble(tok);
                node--;

                //
                //System.out.println(""+node+" "+tipo+" "+valor);
                //if(node == -1) System.exit(0);
                Integer intNodo = new Integer(node);

                // Double dobValor = new Double(valor);
                switch (type) {
                case FLUID_EXIT:

                    if (!exists(intNodo)) {
                        nodesNeumann.addElement(intNodo);

                        // valuesNeumann.addElement(new Double(0.0));
                    } else {
                        System.out.println("   NEUMANN " + node +
                            " - Dropped!");
                    }

                    break;

                // wishing it will work
                case DRAGLIFT:
                    nodesDragLift.addElement(intNodo);

                    break;

                case SLIDING:
                    nodesSlip.addElement(intNodo);

                    if (!exists(intNodo)) {
                        nodesDirichlet.addElement(intNodo);

                        // valuesDirichlet.addElement(dobValor);
                        for (int d = 0; d < dimension; d++)
                            valuesDirichlet[d].addElement(new Double(0.0));
                    } else {
                        System.out.println("   DIRICHLET " + node +
                            " - Dropped!");
                    }

                    break;

                case IMPOSE_SPEED:

                    if (!exists(intNodo)) {
                        nodesDirichlet.addElement(intNodo);

                        for (int d = 0; d < dimension; d++)
                            valuesDirichlet[d].addElement(new Double(readDouble(
                                        tok)));
                    } else {
                        System.out.println("   DIRICHLET " + node +
                            " - Dropped!");
                    }

                    break;

                case PARABOLIC_PROFILE:
                    nodesPerfilDirichlet.addElement(intNodo);

                    // kernel.getConfiguracion().hayPerfil = true;
                    if (!exists(intNodo)) {
                        nodesDirichlet.addElement(intNodo);

                        // valuesDirichlet.addElement(dobValor);
                        for (int d = 0; d < dimension; d++)
                            valuesDirichlet[d].addElement(new Double(0.0));
                    } else {
                        System.out.println("   DIRICHLET " + node +
                            " - Dropped!");
                    }

                    break;

                case INK:
                    nodesInk.addElement(intNodo);

                    break;

                case MAGNUS:
                    nodesMagnus.addElement(intNodo);

                    if (!exists(intNodo)) {
                        nodesDirichlet.addElement(intNodo);

                        // valuesDirichlet.addElement(dobValor);
                        for (int d = 0; d < dimension; d++)
                            valuesDirichlet[d].addElement(new Double(0.0));

                        valuesMagnus.addElement(new Double(readDouble(tok)));
                    } else {
                        System.out.println("   MAGNUS " + node + " - Dropped!");
                    }

                    break;

                default:
                    System.out.println(
                        "internal error: unknown type LoadMesh.java");
                    System.exit(0);

                    break;
                }
            }

            kernel.setLoadProgress(100);
            in.close();

            kernel.out("<TABLE WIDTH=100% BORDER=0>" + "<TR>" +
                "<TD><B>Nodes " + dimension + "D</B></TD><TD>" + totalNodes +
                "</TD>" + "<TD><B>Elements " + elementGrade + "N</B></TD><TD>" +
                totalElements + "</TD>" + "</TR><TR>" +
                "<TD><B>Dirichlet</B></TD><TD>" + nodesDirichlet.size() +
                "</TD>" + "<TD><B>Neumann</B></TD><TD>" + nodesNeumann.size() +
                "</TD>" + "</TR><TR>" + "<TD><B>DragLift</B></TD><TD>" +
                nodesDragLift.size() + "</TD>" + "<TD><B>Slip</B></TD><TD>" +
                nodesSlip.size() + "</TD>" + "</TR><TR>" +
                "<TD><B>Profile</B></TD><TD>" + nodesPerfilDirichlet.size() +
                "</TD>" + "<TD><B>Ink</B></TD>" + nodesInk.size() +
                "<TD></TD>" + "</TR><TR>" + "<TD><B>Magnus</B></TD><TD>" +
                nodesMagnus.size() + "</TD>" + "<TD></TD><TD></TD>" + "</TR>" +
                "</TABLE>");

            if ((dimension == 2) && (elementGrade == 6)) {
                kernel.out(
                    "Mesh <FONT COLOR=#800000><B>2D</B></FONT> loaded correctly.");
            }

            if ((dimension == 3) && (elementGrade == 10)) {
                kernel.out(
                    "Mesh <FONT COLOR=#800000><B>3D</B></FONT> loaded correctly.");
            }
        } catch (Exception ex) {
            System.out.println("LoadMesh: " + ex);
            ex.printStackTrace();
            kernel.newErrorFatalDialog("Loading of the mesh has failed.\n" +
                "Possibly data format was\n" +
                "incorrect, or without a filename.\n");

            System.exit(1);
        }

        // We send all the information to the Kernel
        KernelADFCConfiguration c = kernel.getConfiguration();
        c.coordinates = coord;
        c.nodes = nodes;
        c.nodesDirichlet = matrixInts(nodesDirichlet);
        c.nodesDragLift = matrixInts(nodesDragLift);
        c.nodesNeumann = matrixInts(nodesNeumann);
        c.nodesPerfilDirichlet = matrixInts(nodesPerfilDirichlet);
        c.nodesSlip = matrixInts(nodesSlip);
        c.nodesMagnus = matrixInts(nodesMagnus);
        c.valuesDirichlet = new double[dimension][];

        for (int d = 0; d < dimension; d++)
            c.valuesDirichlet[d] = matrixDoubles(valuesDirichlet[d]);

        c.valuesMagnus = matrixDoubles(valuesMagnus);
        c.valuesNeumann = new double[c.nodesNeumann.length];
        c.nodesInk = matrixInts(nodesInk);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tok DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private double readDouble(StreamTokenizer tok) throws Exception {
        int token = tok.nextToken();

        if (token == tok.TT_NUMBER) {
            return tok.nval;
        } else if (token != tok.TT_EOF) {
            System.out.println("leeDouble(): no es a number !");
            kernel.newErrorFatalDialog("Loading of the mesh has failed.\n" +
                "Possibly data format was\n" +
                "incorrect, or without a filename.\n");
        }

        return Double.NaN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tok DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private int readInt(StreamTokenizer tok) throws Exception {
        int token = tok.nextToken();

        if (token == tok.TT_NUMBER) {
            return (int) tok.nval;
        } else if (token != tok.TT_EOF) {
            System.out.println("readInt(): is not a number !");
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     */
    void uneVectores(Vector parent, Vector child) {
        for (int i = 0; i < child.size(); i++)
            parent.addElement(child.elementAt(i));
    }

    // Going from Vector to matrix.
    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int[] matrixInts(Vector v) {
        int[] m = new int[v.size()];

        for (int i = 0; i < v.size(); i++)
            m[i] = ((Integer) v.elementAt(i)).intValue();

        if (v.size() == 0) {
            m = null;
        }

        return m;
    }

    // Going from Vector to matrix.
    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[] matrixDoubles(Vector v) {
        double[] m = new double[v.size()];

        for (int i = 0; i < v.size(); i++)
            m[i] = ((Double) v.elementAt(i)).doubleValue();

        return m;
    }

    /**
     * returns true if the node has been read. Only afect border
     * conditions, no Drag/Lift.
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean exists(Integer node) {
        if (nodesNeumann.indexOf(node) != -1) {
            return true;
        } else if (nodesDirichlet.indexOf(node) != -1) {
            return true;
        }
        //else if (nodesSlip.indexOf(node) != -1)
        //        return true;
        else {
            return false;
        }
    }
}
