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

package org.jscience.chemistry.gui.basic;

import java.applet.Applet;

import java.awt.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;


/**
 * A class representing a 3D molecule Viewer for pdb files.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this code is adapted from Eric Harlow
//http://www.netbrain.com/~brain/molecule/
//bigbrain@netbrain.com
//This is a standalone package where atoms and bonds don't rely on the chemistry package and matrix doesn't rely on maths package.
//It provides partial support for reading pdb files and is a quick viewer for these files.
//However, as it does not rely on the chemistry package you cannot view your molecules with it.
//It is meant to be deprecated and replaced by a viewer with the same functionnality and that rely on subpackages.
public class MoleculeViewer extends Applet {
    /** DOCUMENT ME! */
    protected static boolean m_bFog = false;

    /** DOCUMENT ME! */
    private boolean bDrawLabels = false;

    /** DOCUMENT ME! */
    private boolean application = false;

    /** DOCUMENT ME! */
    private String[] args;

    /** DOCUMENT ME! */
    private Matrix3D mat = new Matrix3D();

    /** DOCUMENT ME! */
    private Matrix3D amat = new Matrix3D();

    /** DOCUMENT ME! */
    private Matrix3D tmat = new Matrix3D();

    /** DOCUMENT ME! */
    private int prevx;

    /** DOCUMENT ME! */
    private int prevy;

    /** DOCUMENT ME! */
    private int nMoleculeRadius = 2;

    /** DOCUMENT ME! */
    private Image backBuffer;

    /** DOCUMENT ME! */
    private Graphics backGC;

    /** DOCUMENT ME! */
    private Dimension backSize;

    /** DOCUMENT ME! */
    private double xmin;

    /** DOCUMENT ME! */
    private double xmax;

    /** DOCUMENT ME! */
    private double ymin;

    /** DOCUMENT ME! */
    private double ymax;

    /** DOCUMENT ME! */
    private double zmin;

    /** DOCUMENT ME! */
    private double zmax;

    /** DOCUMENT ME! */
    private double min_range;

    /** DOCUMENT ME! */
    private double max_range;

    /** DOCUMENT ME! */
    private int[] sortindex;

    /** DOCUMENT ME! */
    private Atom[] atomlist;

    /** DOCUMENT ME! */
    private int nAtoms = 0;

    /** DOCUMENT ME! */
    private int nMaxAtoms = 5000;

    /** DOCUMENT ME! */
    private Bond[] bondlist;

    /** DOCUMENT ME! */
    private int nBonds = 0;

    /** DOCUMENT ME! */
    private int nMaxBonds = 10000;

    /*
     * main
     *
     * Used on application mode to try and test the code.
     *
     *@deprecated whole package is deprecated
     *
     */
    public static void main(String[] args) {
        MoleculeViewer moleculeViewer;

        moleculeViewer = new MoleculeViewer();
        moleculeViewer.application = true;
        moleculeViewer.args = args;
        moleculeViewer.init();
    }

    /*
     * init
     *
     * Called when the applet initializes.
     * Reads in the modecule descriptor file.
     */
    public void init() {
        String sFilename;
        InputStream is;

        atomlist = new Atom[nMaxAtoms];
        bondlist = new Bond[nMaxBonds];

        // --- If testing from an application
        if (application) {
            sFilename = args[0];
            nMoleculeRadius = 2;
        } else {
            // --- Reading in as an applet.
            sFilename = getParameter("model");

            String sMoleculeRadius = getParameter("mole_rad");

            try {
                nMoleculeRadius = (int) Integer.parseInt(sMoleculeRadius);
            } catch (Exception e) {
                nMoleculeRadius = 2;
            }

            // --- Get labels
            String sLabels = getParameter("labels");

            if (sLabels != null) {
                if (sLabels.equals("1")) {
                    bDrawLabels = true;
                }
            }

            String sFog = getParameter("fog");

            if (sFog != null) {
                if (sFog.equals("1")) {
                    m_bFog = true;
                }
            }
        }

        try {
            System.out.println("get URL");

            // --- Open a file to the model file.
            if (application) {
                is = new FileInputStream(sFilename);
            } else {
                is = new URL(getDocumentBase(), sFilename).openStream();
            }

            System.out.println("read atoms");
            readAtoms(is);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
     * ReadAtoms
     *
     * Read the atoms in the molecule descriptor file.
     */
    private void readAtoms(InputStream is) throws Exception {
        int nToken;
        String sAtomType;
        int nIndex;
        double x;
        double y;
        double z;
        Atom atom;
        String sData;
        String sKeyword;

        sortindex = new int[nMaxAtoms];

        // --- Prepare the file for reading.
        BufferedReader bin = new BufferedReader(new InputStreamReader(is));

        // --- Read a line of text.
        sData = bin.readLine();

        try {
            // --- While not EOF
            while (sData != null) {
                System.out.println("Reading a line...");

                int nEnd = Math.min(6, sData.length());
                sKeyword = sData.substring(0, nEnd).trim();

                if (sKeyword.equals("ATOM")) {
                    // --- Get atom index
                    String str = sData.substring(6, 11).trim();
                    nIndex = Integer.parseInt(str);

                    // --- Get atom type
                    sAtomType = sData.substring(12, 16).trim();

                    // --- Get x-y-z coordinates.
                    x = Double.parseDouble(sData.substring(30, 38).trim());
                    y = Double.parseDouble(sData.substring(38, 46).trim());
                    z = Double.parseDouble(sData.substring(46, 54).trim());

                    // --- Next line, first word
                    System.out.println("Adding " + nIndex + " '" + sAtomType +
                        "' " + x + " " + y + " " + z);

                    if (nIndex < nMaxAtoms) {
                        if (nIndex >= nAtoms) {
                            nAtoms = nIndex;
                        }

                        // --- All the atom needs to know about itself.
                        atomlist[nIndex] = new Atom(sAtomType, x, y, z);
                        sortindex[nIndex] = nIndex;
                    }
                } else if (sKeyword.equals("TER")) {
                    System.out.println("TER");
                } else if (sKeyword.equals("CONECT")) {
                    int idx = 6;
                    int nFirstBond = Integer.parseInt(sData.substring(idx,
                                idx + 6).trim());
                    idx += 5;

                    while (idx < sData.length()) {
                        int ixEnd = Math.min(idx + 6, sData.length());
                        int nNextBond = (int) Integer.parseInt(sData.substring(
                                    idx, ixEnd).trim());

                        if (nBonds < nMaxBonds) {
                            bondlist[nBonds] = new Bond(nFirstBond,
                                    (int) nNextBond);
                            atomlist[nFirstBond].addBond(bondlist[nBonds]);
                            atomlist[nNextBond].addBond(bondlist[nBonds]);

                            System.out.print("-->" + (int) nNextBond);
                            nBonds++;
                        }

                        idx += 5;
                    }

                    System.out.println("");
                } else {
                    System.out.println("Can't handle " + sKeyword);
                }

                sData = bin.readLine();
            }
        } catch (Exception e) {
            System.out.println("Exception...");
        }

        is.close();

        System.out.println("Find BoundingBox");
        findBoundingBox();
        sortMolecules(atomlist, sortindex);

        System.out.println("x " + xmin + " " + xmax);
        System.out.println("y " + ymin + " " + ymax);
        System.out.println("z " + zmin + " " + zmax);
    }

    /*
     * findBoundingBox
     *
     * Find the bounding box of the molecule.  Bounding box is the
     * min and max of (x, y, z) points.
     */
    private void findBoundingBox() {
        int i;
        Atom atom;
        double nDelta;

        System.out.println("Atom 1");
        atom = atomlist[1];

        xmin = atom.m_x;
        xmax = atom.m_x;

        ymin = atom.m_y;
        ymax = atom.m_y;

        zmin = atom.m_z;
        zmax = atom.m_z;

        // --- Atoms start at 1
        for (i = 2; i <= nAtoms; i++) {
            System.out.println("Atom " + i + "(" + atom.m_x + "," + atom.m_y +
                "," + atom.m_z + ")");

            atom = atomlist[i];

            if (atom.m_x < xmin) {
                xmin = atom.m_x;
            }

            if (atom.m_x > xmax) {
                xmax = atom.m_x;
            }

            if (atom.m_y < ymin) {
                ymin = atom.m_y;
            }

            if (atom.m_y > ymax) {
                ymax = atom.m_y;
            }

            if (atom.m_z < zmin) {
                zmin = atom.m_z;
            }

            if (atom.m_z > zmax) {
                zmax = atom.m_z;
            }
        }

        if (true) {
            // --- Default to -x- range
            nDelta = xmax - xmin;
            min_range = xmin;
            max_range = xmax;

            // --- See if -y- range is better...
            if ((ymax - ymin) > nDelta) {
                nDelta = ymax - ymin;
                min_range = ymin;
                max_range = ymax;
            }

            // --- See if -z- range is better...
            if ((zmax - zmin) > nDelta) {
                nDelta = zmax - zmin;
                min_range = zmin;
                max_range = zmax;
            }
        } else {
            if ((xmin <= ymin) && (xmin <= zmin)) {
                min_range = xmin;
            } else if ((ymin <= xmin) && (ymin <= zmin)) {
                min_range = ymin;
            } else {
                min_range = zmin;
            }

            if ((xmax >= ymax) && (xmax >= zmax)) {
                max_range = xmax;
            } else if ((ymax >= xmax) && (ymax >= zmax)) {
                max_range = ymax;
            } else {
                max_range = zmax;
            }
        }

        double delta = max_range - min_range;
        min_range -= (delta / 5.0);
    }

    /*
     * mouseDown
     *
     * Store the position the mouse went down.  (Used to rotate
     * the molecule as they drag.)
     */
    public boolean mouseDown(Event e, int x, int y) {
        prevx = x;
        prevy = y;

        return true;
    }

    /*
     * mouseDrag
     *
     * Rotate the molecule based on the amount they've dragged the mouse
     * Also, update the mouse position for the next drag position.
     */
    public boolean mouseDrag(Event e, int x, int y) {
        double xtheta = (prevy - y) * (360.0f / size().width);
        double ytheta = (x - prevx) * (360.0f / size().height);

        tmat.unit();
        tmat.xrot(xtheta);
        tmat.yrot(ytheta);

        amat.mult(tmat);

        // --- Now that rotation information has been updated, paint it.
        repaint();

        prevx = x;
        prevy = y;

        return true;
    }

    /*
     * TransformPoints
     *
     * Transform the 3-d points into a 2-d series.
     */
    private void transformPoints(Matrix3D mat) {
        int i;

        for (i = 1; i <= nAtoms; i++) {
            mat.transform(atomlist[i]);
        }
    }

    /*
     * update
     *
     * Override the update so that we can just paint without
     * erasing.  This gets rid of the flicker because the
     * default action for update is to erase the background.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /*
     * paint
     *
     * paint the molecule.
     */
    public void paint(Graphics g) {
        double xfac;
        double yfac;

        // --- Get the range of coordinates
        double xw = xmax - xmin;
        double yw = ymax - ymin;
        double zw = zmax - zmin;

        // --- Calculate max range
        if (yw > xw) {
            xw = yw;
        }

        if (zw > xw) {
            xw = zw;
        }

        // --- Calculate molecule position to pixel position coordinates.
        double f1 = size().width / xw;
        double f2 = size().height / xw;
        xfac = .7 * ((f1 < f2) ? f1 : f2);

        // --- Calculate maxtrix for molecule transformation
        mat.unit();
        mat.translate(-(xmin + xmax) / 2, -(ymin + ymax) / 2, -(zmin + zmax) / 2);
        mat.mult(amat);
        mat.scale(xfac, -xfac, 1.0); // 16 * xfac / size().width);
        mat.translate(size().width / 2.0, size().height / 2.0,
            (zmin + zmax) / 2.0); //eaheah was 10

        // --- Transform molecule
        transformPoints(mat);

        // --- Create double buffering buffer
        if (backBuffer == null) {
            newBackgoundBuffer();
        }

        // --- If we're using double buffering
        if (backBuffer != null) {
            // --- Draw to the background
            //backGC.setColor (getBackground());
            backGC.setColor(Color.gray);
            backGC.fillRect(0, 0, size().width, size().height);
            drawMolecule(backGC);

            // --- Copy background to foreground
            g.drawImage(backBuffer, 0, 0, this);
        } else {
            // --- Draw to the screen.
            g.fillRect(0, 0, size().width, size().height);
            drawMolecule(g);
        }
    }

    /*
     * newBackBuffer
     *
     * Create a background image for double buffering.
     */
    private void newBackgoundBuffer() {
        backBuffer = createImage(size().width, size().height);
        backGC = backBuffer.getGraphics();
        backSize = size();
    }

    /*
     * sortMolecules
     *
     * Sort the molecules by their depth.  This is a simple
     * bubblesort.
     */
    private void sortMolecules(Atom[] atomlist, int[] sortindex) {
        int i;
        int j;
        int nTmp;

        for (i = 0; i < (nAtoms - 1); i++) {
            for (j = 1; j < nAtoms; j++) {
                if (atomlist[sortindex[j]].m_tz > atomlist[sortindex[j + 1]].m_tz) {
                    nTmp = sortindex[j];
                    sortindex[j] = sortindex[j + 1];
                    sortindex[j + 1] = nTmp;
                }
            }
        }
    }

    /*
     * DrawMolecule
     *
     * Draw the molecule.
     */
    private void drawMolecule(Graphics g) {
        Atom atom;
        int i;
        int j;
        int nIndex;
        int nDiameter;
        Bond bond;
        double _minz;
        double _maxz;

        // --- Sort molecules so that farthest ones are drawn first.
        sortMolecules(atomlist, sortindex);

        g.setColor(Color.black);

        nDiameter = nMoleculeRadius + nMoleculeRadius;

        /*
         * Mark all bonds as "not painted" so that they're drawn
         * only once from the farthest ones first.
         */
        for (i = 0; i < nBonds; i++) {
            bondlist[i].m_painted = false;
        }

        for (i = 1; i <= nAtoms; i++) {
            atomlist[sortindex[i]].calculateFog(min_range, max_range);
        }

        atom = atomlist[1];
        _minz = atom.m_tz;
        _maxz = atom.m_tz;

        /*
         * Draw atoms.
         */
        for (i = 1; i <= nAtoms; i++) {
            //nIndex = sortindex[i];
            atom = atomlist[sortindex[i]];

            if (atom.m_tz > _maxz) {
                _maxz = atom.m_tz;
            }

            if (atom.m_tz < _minz) {
                _minz = atom.m_tz;
            }

            if (atom != null) {
                // --- Draw the atom
                atom.setColor(g, min_range, max_range);

                g.fillOval((int) atom.m_tx - nMoleculeRadius,
                    (int) atom.m_ty - nMoleculeRadius, nDiameter, nDiameter);

                // --- Draw labels if we want to
                if (bDrawLabels) {
                    g.drawString(atom.m_sName,
                        (int) atom.m_tx + nMoleculeRadius,
                        (int) atom.m_ty + nMoleculeRadius);
                }

                g.setColor(Color.black);

                if (atom.bondlist != null) {
                    for (j = 0; j < atom.bondlist.size(); j++) {
                        bond = (Bond) atom.bondlist.elementAt(j);

                        if (bond != null) {
                            if (bond.m_painted == false) {
                                drawBond(g, atomlist[bond.m_nIndex1],
                                    atomlist[bond.m_nIndex2]);
                                bond.m_painted = true;
                            }
                        } else {
                            System.out.println("bond null");
                        }
                    }
                }
            } else {
                System.out.println("atom null");
            }
        }

        System.out.println("zrange = " + _minz + ", " + _maxz + "min/max = [" +
            min_range + "," + max_range);
        g.setColor(Color.black);
    }

    /*
     * Draw a bond between two molecules.
     */
    private void drawBond(Graphics g, Atom atom1, Atom atom2) {
        double dx = atom1.m_tx - atom2.m_tx;
        double dy = atom1.m_ty - atom2.m_ty;
        double dz = atom1.m_tz - atom2.m_tz;

        // --- Calculate length of bond
        double len = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));

        // --- take out the length of the molecule on both ends.
        double xfactor = (nMoleculeRadius * dx) / len;
        double yfactor = (nMoleculeRadius * dy) / len;
        double zfactor = (nMoleculeRadius * dz) / len;

        // --- Draw the bond.
        //eaheah
        g.setColor(atom1.adjustColor(Color.black));
        g.drawLine((int) (atom1.m_tx - xfactor), (int) (atom1.m_ty - yfactor),
            (int) (atom2.m_tx + xfactor), (int) (atom2.m_ty + yfactor));
    }
}
