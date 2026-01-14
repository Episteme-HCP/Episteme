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

package org.jscience.architecture.traffic;

import org.jscience.architecture.traffic.edit.EditController;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;


/**
 * The general class which is used to start up the editor or simulator
 *
 * @author Group Model
 * @version 1.0
 */
public class TrafficStarter {
    /** DOCUMENT ME! */
    protected static int type = -1;

    /** DOCUMENT ME! */
    protected final static int EDITOR = 0;

    /** DOCUMENT ME! */
    protected final static int SIMULATOR = 1;

    /** DOCUMENT ME! */
    protected boolean splashScreen = false;

    /** DOCUMENT ME! */
    protected boolean loadFile = false;

    /** DOCUMENT ME! */
    protected boolean noFurtherOptions = false;

    /** DOCUMENT ME! */
    protected String filename = "";

    /** DOCUMENT ME! */
    protected String[] params;

/**
     * Make a new GLDStarter
     *
     * @param params    The command line parameters with which the real main class
     *                  was loaded.
     * @param startType The type of the controller
     */
    protected TrafficStarter(String[] params, int startType) {
        type = startType;
        this.params = params;
    }

    /**
     * Indicates what kind of program is running in this VM (EDITOR or
     * SIMULATOR). The method returns -1 if the program is not yet running.
     *
     * @return DOCUMENT ME!
     */
    public static int getProgramModus() {
        return type;
    }

    /**
     * Process the command line parameters that where specified when
     * the program was started.
     */
    public void processParams() {
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].startsWith("-") && !noFurtherOptions) {
                    processOption(params[i]);
                } else if (!loadFile) {
                    filename = params[i];
                    loadFile = true;
                } else {
                    illegalParametersError();
                }
            }
        }
    }

    /**
     * Each option parameter (parameter that begins with a dash) is
     * processed by this method
     *
     * @param option option, including dash sign
     */
    public void processOption(String option) {
        if ("-splash".equals(option)) {
            System.out.println("Splash!");
            splashScreen = true;
        } else if ("-nosplash".equals(option)) {
            System.out.println("No Splash!");
            splashScreen = false;
        } else if ("--".equals(option)) {
            noFurtherOptions = true;
        } else {
            illegalParametersError();
        }
    }

    /**
     * This method is called when a command line parameter is
     * encountered that we do not understand. It prints an error message.
     */
    public void illegalParametersError() {
        System.out.println("Illegal parameters");
        System.out.println("Usage : java " + getStarterName() +
            " [--] [filename] [-splash]");
        System.exit(1);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        processParams();

        Controller controller = getController();

        if (loadFile) {
            controller.tryLoad(filename);
        }
    }

    /**
     * Method which gives the right controller for starting
     *
     * @return DOCUMENT ME!
     */
    public Controller getController() {
        switch (type) {
        case EDITOR:
            return new EditController(new EditModel(), splashScreen);

        case SIMULATOR:
            return new SimController(new SimModel(), splashScreen);
        }

        System.out.println(
            "GLDStarter was called with unknown controller type " + type +
            ". Cannot get new instance of Controller.");
        System.exit(1);

        return null; // Dummy to satisfy the compiler
    }

    /**
     * Method which gives the right main class name for error messages
     *
     * @return DOCUMENT ME!
     */
    public String getStarterName() {
        switch (type) {
        case EDITOR:
            return "GLDEdit";

        case SIMULATOR:
            return "GLDSim";
        }

        System.out.println(
            "GLDStarter was called with unknown controller type " + type +
            ". Cannot get name of caller.");
        System.exit(1);

        return null; // Dummy to satisfy the compiler
    }
}
