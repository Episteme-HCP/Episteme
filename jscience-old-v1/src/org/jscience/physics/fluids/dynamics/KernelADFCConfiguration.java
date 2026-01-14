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

package org.jscience.physics.fluids.dynamics;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author Alejandro "balrog" Rodriguez Gallego
 */
public class KernelADFCConfiguration implements java.io.Serializable {
    // DEFINES
    /** DOCUMENT ME! */
    public static final int NAVIERSTOKES = 0; // solver

    /** DOCUMENT ME! */
    public static final int POISSON = 1; // solver

    // Configuration KERNEL_ADFC
    /** DOCUMENT ME! */
    public boolean gui = true;

    /** DOCUMENT ME! */
    public boolean plugin;

    /** DOCUMENT ME! */
    public int solver;

    // Data about CARGA_MALLADO
    /** DOCUMENT ME! */
    public double[][] coordinates;

    /** DOCUMENT ME! */
    public int[][] nodes;

    /** DOCUMENT ME! */
    public int[] nodesDirichlet;

    /** DOCUMENT ME! */
    public int[] nodesDragLift;

    /** DOCUMENT ME! */
    public int[] nodesMagnus;

    /** DOCUMENT ME! */
    public int[] nodesNeumann;

    /** DOCUMENT ME! */
    public int[] nodesPerfilDirichlet;

    /** DOCUMENT ME! */
    public int[] nodesSlip;

    /** DOCUMENT ME! */
    public double[][] valuesDirichlet;

    /** DOCUMENT ME! */
    public double[] valuesMagnus;

    /** DOCUMENT ME! */
    public double[] valuesNeumann;

    // Configuration NAVIER_STOKES
    /** DOCUMENT ME! */
    public double deltaCCSlip;

    /** DOCUMENT ME! */
    public double deltaConvergenceNavierStokes;

    /** DOCUMENT ME! */
    public int domain;

    /** DOCUMENT ME! */
    public boolean saveDiv;

    /** DOCUMENT ME! */
    public boolean saveMuT;

    /** DOCUMENT ME! */
    public boolean saveP;

    /** DOCUMENT ME! */
    public boolean saveVx;

    /** DOCUMENT ME! */
    public boolean saveVy;

    /** DOCUMENT ME! */
    public boolean saveVz;

    /** DOCUMENT ME! */
    public double timeStepDuration;

    /** DOCUMENT ME! */
    public int maxIterationsNavierStokes;

    /** DOCUMENT ME! */
    public String meshName;

    /** DOCUMENT ME! */
    public int stepsSave;

    /** DOCUMENT ME! */
    public int stepsTime;

    /** DOCUMENT ME! */
    public double reynolds;

    /** DOCUMENT ME! */
    public boolean smagorinsky;

    /** DOCUMENT ME! */
    public double initialSpeedX;

    /** DOCUMENT ME! */
    public double initialSpeedY;

    // Configuration OBSERVER
    /** DOCUMENT ME! */
    public double[] observerCoordinateX;

    /** DOCUMENT ME! */
    public double[] observerCoordinateY;

    // Configuration FEEDBACK
    /** DOCUMENT ME! */
    public double feedbackCoordinate;

    /** DOCUMENT ME! */
    public boolean feedback;

    // Configuration SMAGORINSKY
    /** DOCUMENT ME! */
    public double cSmagorinsky;

    // Configuration SOLVER CONJUGATE GRADIENT
    /** DOCUMENT ME! */
    public double deltaConjugatedGradientConvergence;

    // Configuration INK
    /** DOCUMENT ME! */
    public double coordinateBubblesH2;

    /** DOCUMENT ME! */
    public double deltaTBubblesH2;

    /** DOCUMENT ME! */
    public int[] nodesInk;

    // Configuration UTILIDADES
    /** DOCUMENT ME! */
    public String accountSMTP;

    /** DOCUMENT ME! */
    public String hostSMTP;

    /** DOCUMENT ME! */
    public int notificationInterval;

    /** DOCUMENT ME! */
    public boolean notifyFin;

    /** DOCUMENT ME! */
    public boolean notifyIntervalos;

    /** DOCUMENT ME! */
    public int registry;

    // Configuration PERFIL_ENTRADA
    /** DOCUMENT ME! */
    public double coeficientDirichletProfile;

    /** DOCUMENT ME! */
    public double maxProfileHeight;

    /** DOCUMENT ME! */
    public double minProfileHeight;

    // Configuration POISSON
    /** DOCUMENT ME! */
    public double deltaConvergencePoisson;

/**
     * Create new ConfigurationKernelADFC
     */
    public KernelADFCConfiguration() {
    }

    /**
     * Opens and analizes .PRB configuration <code>file</code>.
     *
     * @param file DOCUMENT ME!
     */
    void analizeGidPrbFile(String file) {
        StreamTokenizer tok = null;
        int token;

        try {
            tok = new StreamTokenizer(new InputStreamReader((file.startsWith(
                            "http://") ? new java.net.URL(file).openStream()
                                       : new FileInputStream(file))));

            // configure tokenizer
            tok.eolIsSignificant(false);
            tok.slashSlashComments(true);
            tok.slashStarComments(true);

            // We want all of these in the words
            tok.wordChars(':', ':');
            tok.wordChars('_', '_');
            tok.wordChars('(', '(');
            tok.wordChars(')', ')');
            tok.wordChars('*', '*');
            tok.wordChars(',', ',');
            tok.wordChars('#', '#');
            tok.wordChars('@', '@');

            token = 0;

            // Load header, ignore it
            exigeToken(tok, "PROBLEM");
            exigeToken(tok, "DATA");

            // Read parameters.
            do {
                token = tok.nextToken();

                if (token == StreamTokenizer.TT_WORD) {
                    // END of .PRB file
                    if (tok.sval.equalsIgnoreCase("END")) {
                        return;
                    }
                    // Parameter
                    else if (tok.sval.equalsIgnoreCase("QUESTION:")) {
                        token = tok.nextToken();

                        String parameter = new String(tok.sval);

                        exigeToken(tok, "VALUE:");

                        token = tok.nextToken();
                        configureGidPrbValue(parameter, tok.sval, tok.nval);
                    } else if (tok.sval.equalsIgnoreCase("TITLE:")) {
                        token = tok.nextToken();
                    }
                    // error
                    else {
                        error("Unexpected or unknown token \"" + tok.sval +
                            "\"");
                    }
                }
                // If not word and  EOF, then it is an error !
                else if (token != StreamTokenizer.TT_EOF) {
                    error("Unknown token \"" + tok.sval + "\"");
                }
            } while (token != StreamTokenizer.TT_EOF);

            System.out.println("Loading Configuration completed!");
        } catch (Exception e) {
            error("Excepcion " + e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    void analizeArguments(String[] args) {
        // the points from the observer are read one by one
        // and are passed to the end of two coordinate arrays.
        Vector obsX = new Vector();

        // the points from the observer are read one by one
        // and are passed to the end of two coordinate arrays.
        Vector obsY = new Vector();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fieldini")) {
                initialSpeedX = new Double(args[i + 1]).doubleValue();
                initialSpeedY = new Double(args[i + 2]).doubleValue();
                i += 2;
                System.out.println("   -fieldini " + initialSpeedX + " " +
                    initialSpeedY);
            } else if (args[i].equals("-deltat")) {
                timeStepDuration = new Double(args[++i]).doubleValue();
                System.out.println("   -deltat " + args[i]);
            } else if (args[i].equals("-deltaslip")) {
                deltaCCSlip = new Double(args[++i]).doubleValue();
                System.out.println("   -deltaslip " + args[i]);
            } else if (args[i].equals("-felec")) {
                System.out.println("   -felec " + args[i] +
                    " DESACTIVATED OPTION !");
                System.exit(1);
            } else if (args[i].equals("-gid")) {
                java.io.File gid = new java.io.File(args[++i]);

                if (!gid.isDirectory()) {
                    System.out.println("It is not a directory!");
                    System.exit(1);
                }

                String name = gid.getName();
                String problem = name.substring(0, name.lastIndexOf('.'));

                String inputFile = args[i] + problem;
                System.out.println(name + " " + problem + " " + inputFile);
                analizeGidPrbFile(new java.io.File(args[i], problem + ".prb").getAbsolutePath());
                meshName = new java.io.File(args[i], problem).getAbsolutePath();
            } else if (args[i].equals("-nogui")) {
                gui = false;
                System.out.println("-nogui\t Graphical environment not used.");
            } else if (args[i].equals("-observ")) {
                System.out.println("-observ " + args[i + 1] + " " +
                    args[i + 2]);

                obsX.addElement(new Double(args[i + 1]));
                obsY.addElement(new Double(args[i + 2]));
                i += 2;
            } else if (args[i].equals("-steps")) {
                stepsTime = Integer.parseInt(args[++i]);
                System.out.println("   -steps " + args[i]);
            } else if (args[i].equals("-stepfile")) {
                stepsSave = Integer.parseInt(args[++i]);
                System.out.println("   -stepfile " + args[i]);
            } else if (args[i].equals("-plugin")) {
                System.out.println("-plugin");
                plugin = true;
            } else if (args[i].equals("-reynolds")) {
                reynolds = new Double(args[++i]).doubleValue();
                System.out.println("   -reynolds " + args[i]);
            } else if (args[i].equals("-smagorinsky")) {
                smagorinsky = true;
                System.out.println("   -smagorinsky");
            } else if (args[i].startsWith("-")) {
                System.out.println("Incorrect parameter: " + args[i]);
                System.exit(0);
            } else {
                meshName = new String(args[++i]);

                System.out.println("   " + args[i]);
            }
        }

        // We pass the Observer points to an array
        int totalPoints = obsX.size();

        // Extract coordinates
        observerCoordinateX = new double[totalPoints];
        observerCoordinateY = new double[totalPoints];

        for (int i = 0; i < totalPoints; i++) {
            observerCoordinateX[i] = ((Double) obsX.elementAt(i)).doubleValue();
            observerCoordinateY[i] = ((Double) obsY.elementAt(i)).doubleValue();
        }
    }

    /**
     * Configure parameter <code>variable</code> of the solver.
     *
     * @param parameter parameter to configure.
     * @param str string
     * @param value double
     */
    private void configureGidPrbValue(String parameter, String str, double value) {
        System.out.println("Configured (" + parameter + ", " + str + ", " +
            value + ")");

        // INPUT_PROFILE : profile coeficient
        if (parameter.equalsIgnoreCase("Coeficiente_Perfil")) {
            System.out.println("Coeficiente_Perfil = " + value);
            coeficientDirichletProfile = value;
        }

        // INK: Coordinate for defining the hydrogen bubbles
        if (parameter.equalsIgnoreCase("Coordinate_Bubbles_H2")) {
            System.out.println("Coordinate_Bubbles_H2 = " + value);
            coordinateBubblesH2 = value;
        }

        // NAVIER-STOKES : feedback coordinate
        if (parameter.equalsIgnoreCase("Coordinate_Realimentacion")) {
            System.out.println("Coordinate_Realimentacion = " + value);
            feedbackCoordinate = value;
        }
        // TURBULENCE : Smagorinsky Constant
        else if (parameter.equalsIgnoreCase("Cte_Smagorinsky")) {
            System.out.println("Cte_Smagorinsky = " + value);
            cSmagorinsky = value;
        }
        // UTIL : SMTP account
        else if (parameter.equalsIgnoreCase("Cuenta_SMTP")) {
            System.out.println("Cuenta_SMTP = " + str);
            accountSMTP = str;
        }
        // NAVIER_STOKES : Delta Condicion of Contorno Slip
        else if (parameter.equalsIgnoreCase("Delta_CC_Slip")) {
            System.out.println("Delta_CC_Slip = " + value);
            deltaCCSlip = value;
        }
        // CONJUGATE GRADIENT : convergence delta
        else if (parameter.equalsIgnoreCase("Delta_Convergence_GC")) {
            System.out.println("Delta_Convergence_GC = " + value);
            deltaConjugatedGradientConvergence = value;
        }
        // NAVIER_STOKES : convergence delta
        else if (parameter.equalsIgnoreCase("Delta_Convergence_NavierStokes")) {
            System.out.println("Delta_Convergence_NavierStokes = " + value);
            deltaConvergenceNavierStokes = value;
        }
        // POISSON : convergence delta
        else if (parameter.equalsIgnoreCase("Delta_Convergence_Poisson")) {
            System.out.println("Delta_Convergence_Poisson = " + value);
            deltaConvergencePoisson = value;
        }
        // INK: Delta t to insert Hydrogen
        else if (parameter.equalsIgnoreCase("Delta_T_Bubbles_H2")) {
            System.out.println("Delta_T_Bubbles_H2 = " + value);
            deltaTBubblesH2 = value;
        }
        // NAVIER_STOKES : Store divergence
        else if (parameter.equalsIgnoreCase("Grabar_Divergence#CB#(1,0)")) {
            System.out.println("Grabar_Divergence = " + value);

            if (value == 0) {
                saveDiv = false;
            } else {
                saveDiv = true;
            }
        }
        // NAVIER_STOKES : Store Mu Turbulent
        else if (parameter.equalsIgnoreCase("Grabar_MuTurbulenta#CB#(1,0)")) {
            System.out.println("Grabar_MuTurbulenta = " + value);

            if (value == 0) {
                saveMuT = false;
            } else {
                saveMuT = true;
            }
        }
        // NAVIER_STOKES : Store Pressure
        else if (parameter.equalsIgnoreCase("Grabar_Presion#CB#(1,0)")) {
            System.out.println("Grabar_Presion = " + value);

            if (value == 0) {
                saveP = false;
            } else {
                saveP = true;
            }
        }
        // NAVIER_STOKES : Store VX
        else if (parameter.equalsIgnoreCase("Grabar_Velocidad_X#CB#(1,0)")) {
            System.out.println("Grabar_Velocidad_X = " + value);

            if (value == 0) {
                saveVx = false;
            } else {
                saveVx = true;
            }
        }
        // NAVIER_STOKES : Store VY
        else if (parameter.equalsIgnoreCase("Grabar_Velocidad_Y#CB#(1,0)")) {
            System.out.println("Grabar_Velocidad_Y = " + value);

            if (value == 0) {
                saveVy = false;
            } else {
                saveVy = true;
            }
        }
        // NAVIER_STOKES : Store VZ
        else if (parameter.equalsIgnoreCase("Grabar_Velocidad_Z#CB#(1,0)")) {
            System.out.println("Grabar_Velocidad_Z = " + value);

            if (value == 0) {
                saveVz = false;
            } else {
                saveVz = true;
            }
        }
        // UTIL : Host SMTP
        else if (parameter.equalsIgnoreCase("Host_SMTP")) {
            System.out.println("Host_SMTP = " + str);
            hostSMTP = str;
        }
        // UTIL : Host Remitente SMTP
        else if (parameter.equalsIgnoreCase("Host_Remitente_SMTP")) {
            System.out.println("Host_Remitente_SMTP --- OBSOLETE");

            // hostRemitenteSMTP = valorCadena;
        }
        // NAVIER_STOKES : time step duration
        else if (parameter.equalsIgnoreCase("Longitud_Paso_de_Tiempo")) {
            System.out.println("Longitud_Paso_de_Tiempo = " + value);
            timeStepDuration = value;
        }
        // PERFIL_ENTRADA : maximum heigh of wind profile
        else if (parameter.equalsIgnoreCase("Max_Altura_Perfil")) {
            System.out.println("Max_Altura_Perfil = " + value);
            maxProfileHeight = value;
        }
        // NAVIER_STOKES : maximum iteracion number
        else if (parameter.equalsIgnoreCase("Max_Iter_NavierStokes")) {
            System.out.println("Max_Iter_NavierStokes = " + value);
            maxIterationsNavierStokes = (int) value;
        }
        // PERFIL_ENTRADA :  minimum heigh of wind profile
        else if (parameter.equalsIgnoreCase("Min_Altura_Perfil")) {
            System.out.println("Min_Altura_Perfil = " + value);
            minProfileHeight = value;
        }
        // TURBULENCE : turbulence model
        else if (parameter.equalsIgnoreCase(
                    "Modelo_Turbulencia:#CB#(Smagorinsky,Ninguno)")) {
            System.out.println("Modelo_Turbulencia = " + str);

            if (str.equalsIgnoreCase("Smagorinsky")) {
                smagorinsky = true;
            } else {
                smagorinsky = false;
            }
        }
        // NAVIER_STOKES : Reynolds Number
        else if (parameter.equalsIgnoreCase("Numero_de_Reynolds")) {
            System.out.println("Numero_de_Reynolds = " + value);
            reynolds = value;
        }
        // NAVIER_STOKES : time step number
        else if (parameter.equalsIgnoreCase("Pasos_de_Tiempo")) {
            System.out.println("Pasos_de_Tiempo = " + value);
            stepsTime = (int) value;
        }
        // NAVIER_STOKES : number of steps when to save
        else if (parameter.equalsIgnoreCase("Pasos_para_Salvar")) {
            System.out.println("Pasos_para_Salvar = " + value);
            stepsSave = (int) value;
        }
        // NAVIER_STOKES : Activate or not feedback dirichlet profile
        else if (parameter.equalsIgnoreCase("Realimentacion#CB#(Si,No)")) {
            System.out.println("Realimentacion = " + str);

            if (str.equalsIgnoreCase("Si")) {
                feedback = true;
            } else {
                feedback = false;
            }
        }
        // GENERAL : Solver to use
        else if (parameter.equalsIgnoreCase("Solver:#CB#(NavierStokes,Poisson)")) {
            System.out.println("Solver = " + str);

            if (str.equalsIgnoreCase("NavierStokes")) {
                solver = NAVIERSTOKES;
            } else {
                solver = POISSON;
            }
        }
        // NAVIER_STOKES : initial value of the component X of the speed field
        else if (parameter.equalsIgnoreCase("Velocidad_X_Inicial")) {
            System.out.println("Velocidad_X_Inicial = " + value);
            initialSpeedX = value;
        }
        // NAVIER_STOKES : initial value of the component Y of the speed field
        else if (parameter.equalsIgnoreCase("Velocidad_Y_Inicial")) {
            System.out.println("Velocidad_Y_Inicial = " + value);
            initialSpeedY = value;
        }
    }

    /**
     * Print error message. Ends the program.
     *
     * @param description mensaje of error.
     */
    private void error(String description) {
        System.out.println("Error: in file .prb\n\t" + description);
        System.exit(-1);
    }

    /**
     * This method is used to ask for input of a precise token.
     *
     * @param tok DOCUMENT ME!
     * @param demand string with the demanded token.
     *
     * @throws Exception DOCUMENT ME!
     */
    private void exigeToken(StreamTokenizer tok, String demand)
        throws Exception {
        int token = tok.nextToken();

        if (token == StreamTokenizer.TT_WORD) {
            if (tok.sval.equalsIgnoreCase(demand)) {
                return;
            }
        }

        error("Expected token \"" + demand + "\" but found " + tok.sval);
    }
}
