package org.jscience.chemistry.gui.extended.molecule;

import org.jscience.chemistry.gui.extended.geometry.Angle;
import org.jscience.chemistry.gui.extended.geometry.Line;
import org.jscience.chemistry.gui.extended.geometry.Point3D;
import org.jscience.chemistry.gui.extended.jviewer.IRenderer;

import java.awt.*;
import java.util.Vector;

/**
 * An implementation of a JViewer's renderer interface for drawing and
 * manipulating a Molecule.
 *
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 *         <p/>
 *         Modified Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Added color scheme to allow future expansion of color schema
 *         Used Atom::getColorFromType() to get real color from atom type
 *         Added picking stuff
 *         date: 9/19/97
 * @see IRenderer
 */
public class Renderer implements IRenderer {
    /**
     * Display all hydrogens
     */
    public static final int SHOW_ALL_H = 0;

    /**
     * Don't display any hydrogens
     */
    public static final int SHOW_NO_H = 1;

    /**
     * Display hydrogens not attached to carbons
     */
    public static final int SHOW_NON_C_H = 2;

    /**
     * Coloring scheme constants
     */

    // instead of boolean, this allow more schema to be added in future
    public static final int COLOR_BY_ATOM_TYPE = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int ALL_ATOMS_ONE_COLOR = 1;

    /**
     * Indicate the atom is preferred picked object when an atom and
     * a bond both lie close enough to a single-picking location
     */
    public static final int PICK_ATOM_FIRST = 0;

    /**
     * Indicate the bond is preferred picked object when an atom and
     * a bond both lie close enough to a single-picking location
     */
    public static final int PICK_BOND_FIRST = 1;

    /**
     * Indicate distance is used to break the tie when an atom and
     * a bond both lie close enough to a single-picking locaition
     */
    public static final int SHORTEST_WIN = 2;

    /**
     * Indicate only atoms are pickable, bond picking is not interested
     */
    public static final int PICK_ATOM_ONLY = 3;

    /**
     * Indicate only bonds are pickable, atom picking is not interested
     */
    public static final int PICK_BOND_ONLY = 4;

    /**
     * molecule to be drawn
     */
    protected Molecule mol;

    /**
     * Flag indicating how to handle the display of hydrogens
     */
    protected int hTreatment = SHOW_NO_H;

    /**
     * Double bond separation
     */
    protected float DB_DEV = 2.4f;

    /**
     * Triple bond separation
     */
    protected float TB_DEV = 4.8f;

    /**
     * Aromatic bond separation
     */
    protected float AB_DEV = 4.8f;

    /**
     * Wedge bond separation
     */
    protected float WB_DEV = 7.2f;

    /**
     * Font to display atom labels
     */
    protected Font atomFont = new Font("Helvetica", Font.BOLD, 12);

    /**
     * Default color to display bonds
     */
    protected Color bondColor = Color.black;

    /**
     * Indicates whether atom labels should be displayed
     */
    protected boolean showAtomLabels = true;

    /**
     * Indicates whether atoms and bonds should be colored by atom type
     */

    // redundent data member, but kept to be compatible with pre-existing code
    private boolean colorByAtomType = false;

    /**
     * draw atom color according to which coloring scheme
     */
    protected int colorScheme = ALL_ATOMS_ONE_COLOR;

    /**
     * Color to display selected atoms
     */
    protected Color selectColor = Color.green;

    /**
     * Color to display highlighted atoms
     */
    protected Color highlightColor = Color.yellow;

    /**
     * Color of default atom background
     */
    protected Color atombg = Color.black;

    /**
     * Upper bound of the distance (in pixel) between an atom (or bond)
     * and the pick-location if the atom (or bond) is considered picked
     */
    protected float findRadius = 7.0f;

    /**
     * Indicate whether hidden atoms are pickable
     */
    protected boolean pickHiddenAtom = false;

    /**
     * Indicate whether hidden atoms are pickable
     */
    protected boolean pickHiddenBond = false;

    /**
     * Indicate the picking preference is on atom, bond or distance
     */
    public int pickPreference = PICK_ATOM_FIRST;

    /**
     * Default constructor
     */
    public Renderer() {
    }

    /**
     * Constructor
     *
     * @param mol the Molecule drawn and manipulated by this renderer.
     */
    public Renderer(Molecule mol) {
        this();
        setMol(mol);
    }

    /**
     * Copy constructor
     *
     * @param r Renderer
     */
    public Renderer(Renderer r) {
        if (r == null) {
            return;
        }

        hTreatment = r.hTreatment;
        DB_DEV = r.DB_DEV;
        TB_DEV = r.TB_DEV;
        AB_DEV = r.AB_DEV;
        WB_DEV = r.WB_DEV;
        atomFont = new Font(r.atomFont.getName(), r.atomFont.getStyle(),
                r.atomFont.getSize());
        bondColor = new Color(r.bondColor.getRGB());
        showAtomLabels = r.showAtomLabels;
        colorByAtomType = r.colorByAtomType;
        colorScheme = r.colorScheme;
        selectColor = new Color(r.selectColor.getRGB());
        highlightColor = new Color(r.highlightColor.getRGB());
        atombg = new Color(r.atombg.getRGB());

        //reference to the same molecule
        mol = r.mol;
    }

    /**
     * Return the molecule.
     */
    public Molecule getMol() {
        return mol;
    }

    /**
     * Set the molecule
     *
     * @param mol Molecule
     */
    public void setMol(Molecule mol) {
        this.mol = mol;
    }

    /**
     * Return the bounding box of the Molecule.
     *
     * @return Array of 6 floats in order of xmin, xmax, ymin, ymax, zmin, zmax
     */
    public float[] getBBox() {
        float[] bb = new float[6];

        mol.findBB();

        bb[0] = mol.xmin;
        bb[1] = mol.xmax;
        bb[2] = mol.ymin;
        bb[3] = mol.ymax;
        bb[4] = mol.zmin;
        bb[5] = mol.zmax;

        return bb;
    }

    /**
     * Reset the Molecule's transformation matrix to the identity matrix.
     */
    public void matUnit() {
        mol.mat.unit();
    }

    /**
     * Apply the specified translation to the Molecule's transformation matrix.
     *
     * @param xt x component of translation
     * @param yt y component of translation
     * @param zt z component of translation
     */
    public void matTranslate(float xt, float yt, float zt) {
        mol.mat.translate(xt, yt, zt);
    }

    /**
     * Apply the specified scale to the Molecule's transformation matrix.
     *
     * @param xf x scale factor
     * @param yf y scale factor
     * @param zf z scale factor
     */
    public void matScale(float xf, float yf, float zf) {
        mol.mat.scale(xf, yf, zf);
    }

    /**
     * Multiply the Molecule's transformation matrix by the matrix specified.
     *
     * @param rhs matrix to be multiplied to Molecule's matrix: M = M * rhs
     */
    public void matMult(Matrix3D rhs) {
        mol.mat.mult(rhs);
    }

    /**
     * Apply the Molecule's transformation matrix to its atoms.
     */
    public void matTransform() {
        mol.mat.transform(mol);
    }

    /**
     * Draws an atom.  Draws a filled circle, the atom name, then atom charge.
     *
     * @param a atom
     * @param g graphics context
     * @see #draw
     */
    public void drawAtom(Graphics g, Atom a) {
        boolean drawC = a.display;
        Color atomColor;

        //
        // If the atom is highlighted, draw its label in the highlight color.  Else
        // if its selected, draw its label in the selected color. Else if color by
        // atom type is disabled, draw its label in the same color as the bond its
        // attached to (usually black or white).  Otherwise color the atom's label
        // according to its type.
        //
        if (a.select) {
            atomColor = selectColor;
            drawC = true;
        } else if (a.highlight) {
            atomColor = highlightColor;
            drawC = true;
        }
        //else if ( !colorByAtomType ) {
        else if (colorScheme != COLOR_BY_ATOM_TYPE) {
            atomColor = bondColor;
        } else if (a.color == atombg) {
            atomColor = (atombg == Color.black) ? Color.white : Color.black;
        } else {
            atomColor = a.color;
        }

        if (drawC) {
            g.setColor(atombg);
            g.fillOval((int) a.tx - 6, (int) a.ty - 6, 12, 12);
            g.setColor(atomColor);
            g.drawString(a.name, (int) a.tx - 4, (int) a.ty + 4);
        }

        if (a.charge != 0) {
            int w = g.getFontMetrics().stringWidth(a.name);
            String ch = new String();

            if (a.charge > 0) {
                if (a.charge > 1) {
                    ch += String.valueOf(a.charge);
                }

                ch += "+";
            } else {
                if (a.charge < -1) {
                    ch += String.valueOf(-1 * a.charge);
                }

                ch += "-";
            }

            g.drawString(ch, (int) a.tx - 4 + w, (int) a.ty);
        }
    }

    /**
     * Draws a bond
     *
     * @param b bond
     * @param g graphics context
     * @see #draw
     * @see #drawSingleBond
     * @see #drawAromaticBond
     * @see #drawWedgeBond
     * @see #drawDashBond
     * @see #drawAnyBond
     */
    public void drawBond(Graphics g, Bond b) {
        float x1 = b.a1.tx;
        float y1 = b.a1.ty;
        float x2 = b.a2.tx;
        float y2 = b.a2.ty;
        Color color1;
        Color color2;
        Color atomColor = null;

        //
        // Determine how to draw the bond halves.  If one of the bond's atoms is
        // highlighted, color that half of the bond the highlight color.  If color by
        // atom type is enabled, color according to atom type, unless the atom type
        // color is the same as the current background color.  Otherwise just color
        // the bond its default color.  Note that we're assuming color by atom type
        // is for 3D renderings and thus not coloring wedge bonds by atom type.
        //
        if (b.a1.highlight) {
            color1 = highlightColor;
        } else if (colorScheme == COLOR_BY_ATOM_TYPE) {
            //atomColor = (Color) Atom.atomColor.get( b.a1.getName() );
            //atomColor = b.a1.getColor();
            atomColor = b.a1.getColorFromType();

            if (atomColor == atombg) {
                color1 = (atombg == Color.black) ? Color.white : Color.black;
            } else {
                color1 = atomColor;
            }
        } else {
            color1 = bondColor;
        }

        if (b.a2.highlight) {
            color2 = highlightColor;
        }
        //else if ( colorByAtomType ) {
        else if (colorScheme == COLOR_BY_ATOM_TYPE) {
            //atomColor = (Color) Atom.atomColor.get( b.a2.getName() );
            //atomColor = b.a2.getColor();
            atomColor = b.a2.getColorFromType();

            if (atomColor == atombg) {
                color2 = (atombg == Color.black) ? Color.white : Color.black;
            } else {
                color2 = atomColor;
            }
        } else {
            color2 = bondColor;
        }

        if (showAtomLabels) {
            color1 = Color.black;
            color2 = Color.black;
        }

        switch (b.type) {
            default:
            case Bond.SINGLE:
                drawSingleBond(g, color1, color2, x1, y1, x2, y2);

                break;

            case Bond.WEDGE:
                drawWedgeBond(g, x1, y1, x2, y2);

                break;

            case Bond.DASH:
                drawDashBond(g, x1, y1, x2, y2);

                break;

            case Bond.ANY:
                drawAnyBond(g, color1, color2, x1, y1, x2, y2);

                break;

            case Bond.DOUBLE:
            case Bond.TRIPLE:
            case Bond.AROMATIC:

                float pex = 0.0f;
                float pey = 0.0f;
                float[] px = new float[1];
                float[] py = new float[1];
                px[0] = py[0] = 0.0f;
                mol.calcPerpUnitVec(x1, y1, x2, y2, px, py);

                switch (b.type) {
                    case Bond.DOUBLE:
                        pex = 0.6f * px[0] * DB_DEV;
                        pey = 0.6f * py[0] * DB_DEV;
                        drawSingleBond(g, color1, color2, x1 + pex, y1 + pey, x2 + pex,
                                y2 + pey);
                        drawSingleBond(g, color1, color2, x1 - pex, y1 - pey, x2 - pex,
                                y2 - pey);

                        break;

                    case Bond.TRIPLE:
                        pex = px[0] * TB_DEV;
                        pey = py[0] * TB_DEV;
                        drawSingleBond(g, color1, color2, x1 + pex, y1 + pey, x2 + pex,
                                y2 + pey);
                        drawSingleBond(g, color1, color2, x1, y1, x2, y2);
                        drawSingleBond(g, color1, color2, x1 - pex, y1 - pey, x2 - pex,
                                y2 - pey);

                        break;

                    case Bond.AROMATIC:

                        //
                        // If this is an "outer" ring bond, draw the dashes on the "other"
                        // side.
                        //
                        if (mol.isOuterBond(b, x1, y1, x2, y2)) {
                            px[0] = -px[0];
                            py[0] = -py[0];
                        }

                        pex = px[0] * AB_DEV;
                        pey = py[0] * AB_DEV;
                        drawSingleBond(g, color1, color2, x1, y1, x2, y2);
                        drawAromaticBond(g, color1, color2, x1 - pex, y1 - pey,
                                x2 - pex, y2 - pey);

                        break;
                }

                break;
        }
    }

    /**
     * Draws a single bond
     *
     * @param g  graphics context
     * @param c1 color of the first half of the bond
     * @param c2 color of the second half of the bond
     * @param x1 x coord of from atom
     * @param y1 y coord of from atom
     * @param x2 x coord of to atom
     * @param y2 y coord of to atom
     * @see #drawBond
     */
    protected void drawSingleBond(Graphics g, Color c1, Color c2, float x1,
                                  float y1, float x2, float y2) {
        int midx = (int) ((x1 + x2) / 2.0f);
        int midy = (int) ((y1 + y2) / 2.0f);

        g.setColor(c1);
        g.drawLine((int) x1, (int) y1, midx, midy);

        g.setColor(c2);
        g.drawLine(midx, midy, (int) x2, (int) y2);
    }

    /**
     * Draws an aromatic bond
     *
     * @param g  graphics context
     * @param c1 color of the first half of the bond
     * @param c2 color of the second half of the bond
     * @param x1 x coord of from atom
     * @param y1 y coord of from atom
     * @param x2 x coord of to atom
     * @param y2 y coord of to atom
     * @see #drawBond
     */
    protected void drawAromaticBond(Graphics g, Color c1, Color c2, float x1,
                                    float y1, float x2, float y2) {
        float[] px = new float[1];
        float[] py = new float[1];
        mol.calcPerpUnitVec(x1, y1, x2, y2, px, py);

        float pex = px[0];
        float pey = py[0];

        float xlow = pex;
        float ylow = pey;
        float xhigh = -pex;
        float yhigh = -pey;

        float xori = x1;
        float yori = y1;

        float xm = x2 - x1;
        float ym = y2 - y1;
        float length = (float) Math.sqrt(((x2 - x1) * (x2 - x1)) +
                ((y2 - y1) * (y2 - y1)));
        xm = xm / length;
        ym = ym / length;

        int[] xpoints = new int[4];
        int[] ypoints = new int[4];

        float div = 1.0f / 9.0f;

        // draw from - mid
        g.setColor(c1);

        for (int i = 1; i < 5; i += 2) {
            x1 = (xm * length * div * (float) i) + xori;
            x2 = (xm * length * div * (float) (i + 1)) + xori;
            y1 = (ym * length * div * (float) i) + yori;
            y2 = (ym * length * div * (float) (i + 1)) + yori;
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        // draw mid - to
        g.setColor(c2);

        for (int i = 5; i < 9; i += 2) {
            x1 = (xm * length * div * (float) i) + xori;
            x2 = (xm * length * div * (float) (i + 1)) + xori;
            y1 = (ym * length * div * (float) i) + yori;
            y2 = (ym * length * div * (float) (i + 1)) + yori;
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }

    /**
     * Draws a wedge bond
     *
     * @see #drawBond
     */
    protected void drawWedgeBond(Graphics g, float x1, float y1, float x2,
                                 float y2) {
        float[] px = new float[1];
        float[] py = new float[1];
        mol.calcPerpUnitVec(x1, y1, x2, y2, px, py);

        float pex = px[0];
        float pey = py[0];
        float xlow = pex * WB_DEV;
        float ylow = pey * WB_DEV;
        float xhigh = -xlow;
        float yhigh = -ylow;
        float xm = (x1 + x2) * 0.5f;
        float ym = (y1 + y2) * 0.5f;

        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        xpoints[0] = (int) (xlow + x2);
        xpoints[1] = (int) (xhigh + x2);
        xpoints[2] = (int) x1;
        ypoints[0] = (int) (ylow + y2);
        ypoints[1] = (int) (yhigh + y2);
        ypoints[2] = (int) y1;
        g.fillPolygon(xpoints, ypoints, 3);
    }

    /**
     * Draws a dashed wedge bond (assumes black & white)
     *
     * @see #drawBond
     */
    protected void drawDashBond(Graphics g, float x1, float y1, float x2,
                                float y2) {
        float[] px = new float[1];
        float[] py = new float[1];
        mol.calcPerpUnitVec(x1, y1, x2, y2, px, py);

        float pex = px[0];
        float pey = py[0];
        float xlow;
        float xhigh;
        float ylow;
        float yhigh;
        float xm = (x1 + x2) * 0.5f;
        float ym = (y1 + y2) * 0.5f;
        float length = (float) Math.sqrt(((x2 - x1) * (x2 - x1)) +
                ((y2 - y1) * (y2 - y1)));
        float sinTheta = (y2 - y1) / length;
        float cosTheta = (x2 - x1) / length;
        float dlength = 2.0f;
        int steps = (int) (length / dlength);
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        boolean isWhite = true;

        for (int i = 0; i < steps; i++) {
            xlow = pex * (float) ((WB_DEV * (steps - i)) / steps);
            ylow = pey * (float) ((WB_DEV * (steps - i)) / steps);
            xhigh = -xlow;
            yhigh = -ylow;
            x2 = x1 + (length * cosTheta);
            y2 = y1 + (length * sinTheta);

            length -= dlength;
            xpoints[0] = (int) (xlow + x2);
            xpoints[1] = (int) (xhigh + x2);
            xpoints[2] = (int) x1;
            ypoints[0] = (int) (ylow + y2);
            ypoints[1] = (int) (yhigh + y2);
            ypoints[2] = (int) y1;

            if (isWhite) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }

            isWhite = !isWhite;
            g.drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
        }

        if (isWhite) {
            g.setColor(Color.black);
        }
    }

    /**
     * Draws an any bond
     *
     * @param g  graphics context
     * @param c1 color of the first half of the bond
     * @param c2 color of the second half of the bond
     * @param x1 x coord of from atom
     * @param y1 y coord of from atom
     * @param x2 x coord of to atom
     * @param y2 y coord of to atom
     * @see #drawBond
     */
    protected void drawAnyBond(Graphics g, Color c1, Color c2, float x1,
                               float y1, float x2, float y2) {
        float xori = x1;
        float yori = y1;
        float length = (float) Math.sqrt(((x2 - x1) * (x2 - x1)) +
                ((y2 - y1) * (y2 - y1)));
        float sinTheta = (y2 - y1) / length;
        float cosTheta = (x2 - x1) / length;
        float dlength = 3.5f;
        int steps = (int) (length / dlength);

        g.setColor(c2);

        for (int i = 0; i < (steps / 2); i += 2) {
            x1 = xori + (length * cosTheta);
            y1 = yori + (length * sinTheta);
            length -= dlength;
            x2 = xori + (length * cosTheta);
            y2 = yori + (length * sinTheta);
            length -= dlength;
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        g.setColor(c1);

        for (int i = steps / 2; i < steps; i += 2) {
            x1 = xori + (length * cosTheta);
            y1 = yori + (length * sinTheta);
            length -= dlength;
            x2 = xori + (length * cosTheta);
            y2 = yori + (length * sinTheta);
            length -= dlength;
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }

    /**
     * Draw the Molecule.
     *
     * @param g the graphics context.
     */
    public void draw(Graphics g) {
        for (int i = 0; i < mol.numBonds; i++) {
            Bond b = mol.myBonds.getBond(i);

            if (b.a1.isHydrogen()) {
                if ((hTreatment == SHOW_NO_H) ||
                        ((hTreatment == SHOW_NON_C_H) && b.a2.isCarbon())) {
                    b.a1.display = false;

                    continue;
                } else {
                    b.a1.display = true;
                }
            } else if (b.a1.isCarbon()) {
                b.a1.display = (b.a1.charge != 0) ? true : false;
            }

            if (b.a2.isHydrogen()) {
                if ((hTreatment == SHOW_NO_H) ||
                        ((hTreatment == SHOW_NON_C_H) && b.a1.isCarbon())) {
                    b.a2.display = false;

                    continue;
                } else {
                    b.a2.display = true;
                }
            } else if (b.a2.isCarbon()) {
                b.a2.display = (b.a2.charge != 0) ? true : false;
            }

            drawBond(g, b);
        }

        if (!showAtomLabels) {
            return;
        }

        g.setFont(atomFont);

        for (int i = 0; i < mol.numAtoms; i++) {
            Atom a = mol.myAtoms.getAtom(i);

            if (a.isHydrogen() && !a.needDisplay()) {
                continue;
            }

            drawAtom(g, a);
        }
    }

    /**
     * @param tx transformed x coordinate
     * @param ty transformed y coordinate
     * @return an rendered object closest to the input transformed (tx, ty)
     *         coordinate within this renderer, with consideration to pickPreference.
     * @see pickPreference
     */
    public Object findObject(float tx, float ty) {
        switch (pickPreference) {
            case PICK_BOND_FIRST: {
                Bond bond = findBond(tx, ty);

                if (bond == null) {
                    return findAtom(tx, ty);
                }

                return bond;
            }

            case SHORTEST_WIN: {
                Atom atom = findAtom(tx, ty);
                Bond bond = findBond(tx, ty);

                if (atom == null) {
                    return bond;
                }

                if (bond == null) {
                    return atom;
                }

                //
                // both atom and bond are candidates, then compare the distance
                // from pick-location to atom and to mid-point of bond, the lesser wins.
                //
                float diffAtomX = atom.getTx() - tx;
                float diffAtomY = atom.getTy() - ty;
                float distToAtomSquare = (diffAtomX * diffAtomX) +
                        (diffAtomY * diffAtomY);
                float diffBondX = ((bond.a1.getTx() + bond.a2.getTx()) / 2.0f) -
                        tx;
                float diffBondY = ((bond.a1.getTy() + bond.a2.getTy()) / 2.0f) -
                        ty;
                float distToBondSquare = (diffBondX * diffBondX) +
                        (diffBondY * diffBondY);

                if (distToAtomSquare < distToBondSquare) {
                    return atom;
                } else {
                    return bond;
                }
            }

            case PICK_ATOM_ONLY:
                return findAtom(tx, ty);

            case PICK_BOND_ONLY:
                return findBond(tx, ty);

            case PICK_ATOM_FIRST:
            default: {
                Atom atom = findAtom(tx, ty);

                if (atom == null) {
                    return findBond(tx, ty);
                }

                return atom;
            }
        }
    }

    /**
     * @param tx transformed x coordinate
     * @param ty transformed y coordinate
     * @return a rendered atom closest to the input transformed (tx, ty) coordinate
     */
    public Atom findAtom(float tx, float ty) {
        Atom resultAtom = null;
        float minDist = Float.MAX_VALUE;

        AtomVector av = mol.getMyAtoms();
        Atom tempAtom = null;

        for (int i = 0; i < av.size(); i++) {
            tempAtom = av.getAtom(i);

            //
            // if atom is hiddened and the policy is not to pick hidden atoms,
            // ignore atom. At this stage, only hydrogens may hide.
            //

            /*
                    if ( tempAtom.isHydrogen() && !tempAtom.needDisplay()
                         && !pickHiddenAtom )
            */
            if (isHidden(tempAtom) && !pickHiddenAtom) {
                continue;
            }

            float diffX = tempAtom.getTx() - tx;
            float diffY = tempAtom.getTy() - ty;
            float dist = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));

            if ((dist < findRadius) && (dist < minDist)) {
                minDist = dist;
                resultAtom = tempAtom;
            }
        }

        return resultAtom;
    }

    /**
     * @param tx transformed x coordinate
     * @param ty transformed y coordinate
     * @return a rendered bond closest to the input transformed (tx, ty) coordinate
     */
    public Bond findBond(float tx, float ty) {
        Bond resultBond = null;
        BondVector bv = mol.getMyBonds();
        Bond tempBond = null;
        float minDist = Float.MAX_VALUE;

        for (int i = 0; i < bv.size(); i++) {
            tempBond = bv.getBond(i);

            //
            // if bond is hiddened and the policy is not to pick hidden bonds,
            // ignore atom. At this stage, only bonds with hidden hydrogens are hide.
            //
            if ((isHidden(tempBond.a1) || isHidden(tempBond.a2)) &&
                    !pickHiddenBond) {
                continue;
            }

            Point3D pointA = new Point3D(tempBond.a1.getTx(),
                    tempBond.a1.getTy(), 0.0);
            Point3D pointB = new Point3D(tempBond.a2.getTx(),
                    tempBond.a2.getTy(), 0.0);
            Point3D pointX = new Point3D(tx, ty, 0.0);

            //
            // the point(tx,ty) should not fall outside of the bond, i.e., the
            // angles A-B-X and B-A-X should not exceed 90 degree. Otherwise,
            // don't consider the bond as candidate for return.
            //
            Angle abx = pointA.angleWith(pointB, pointX);

            if (abx.degreeValue() > 90) {
                continue;
            }

            Angle bax = pointB.angleWith(pointA, pointX);

            if (bax.degreeValue() > 90) {
                continue;
            }

            Line line = new Line(pointA, pointB);
            float dist = (float) line.distanceTo(pointX);

            if ((dist < findRadius) && (dist < minDist)) {
                minDist = dist;
                resultBond = tempBond;
            }
        }

        return resultBond;
    }

    /**
     * Return a list of objects that are within the given polygon
     *
     * @param pickPolygon the polygon within which objects are considered picked
     */
    public Vector findObjects(Polygon pickPolygon) {
        Vector v = new Vector();

        if (pickPreference != PICK_BOND_ONLY) {
            findAtoms(pickPolygon, v);
        }

        if (pickPreference != PICK_ATOM_ONLY) {
            findBonds(pickPolygon, v);
        }

        return v;
    }

    /**
     * Find a list of atoms within the given polygon
     *
     * @param pickPolygon  the polygon with which atoms are considerred picked
     * @param resultVector vector into which the found atoms are put
     * @throw NullPointerException if the input resultVector is null
     */
    public void findAtoms(Polygon pickPolygon, Vector resultVector) {
        if ((pickPolygon == null) || (mol == null)) {
            return;
        }

        AtomVector av = mol.getMyAtoms();

        if (av == null) {
            return;
        }

        for (int i = 0; i < av.size(); i++) {
            Atom tempAtom = av.getAtom(i);

            //
            // if atom is hiddened and the policy is not to pick hidden atoms,
            // ignore atom. At this stage, only hydrogens may hide.
            //
            if (isHidden(tempAtom) && !pickHiddenAtom) {
                continue;
            }

            int x = (int) tempAtom.getTx();
            int y = (int) tempAtom.getTy();

            if (pickPolygon.inside(x, y)) {
                resultVector.addElement(tempAtom);
            }
        }

        if (resultVector.size() < 1) {
            resultVector = null;
        }
    }

    /**
     * Find a list of bonds within the given polygon
     *
     * @param pickPolygon  the polygon with which bonds are considerred picked
     * @param resultVector vector into which the found bonds are put
     * @throw NullPointerException if the input resultVector is null
     */
    public void findBonds(Polygon pickPolygon, Vector resultVector) {
        if ((pickPolygon == null) || (mol == null)) {
            return;
        }

        BondVector bv = mol.getMyBonds();

        if (bv == null) {
            return;
        }

        for (int i = 0; i < bv.size(); i++) {
            Bond tempBond = bv.getBond(i);

            //
            // if bond is hiddened and the policy is not to pick hidden bonds,
            // ignore atom. At this stage, only bonds with hidden hydrogens are hide.
            //
            if ((isHidden(tempBond.a1) || isHidden(tempBond.a2)) &&
                    !pickHiddenBond) {
                continue;
            }

            // if the mid-point of the bond is in the lasso,
            // it's considered picked.
            int x1 = (int) tempBond.a1.getTx();
            int y1 = (int) tempBond.a1.getTy();
            int x2 = (int) tempBond.a2.getTx();
            int y2 = (int) tempBond.a2.getTy();

            if (pickPolygon.inside((x1 + x2) / 2, (y1 + y2) / 2)) {
                resultVector.addElement(tempBond);
            }
        }

        if (resultVector.size() < 1) {
            resultVector = null;
        }
    }

    /**
     * @param atom the atom to be check whether it is hidden
     * @return true iff an atom is hidden
     */
    public boolean isHidden(Atom atom) {
        return !atom.needDisplay() && atom.isHydrogen();
    }

    // getter section:

    /**
     * Return hydrogen treatment
     */
    public int getHTreatment() {
        return hTreatment;
    }

    /**
     * Return double bond separation
     */
    public float getDB_DEV() {
        return DB_DEV;
    }

    /**
     * Return triple bond separation
     */
    public float getTB_DEV() {
        return TB_DEV;
    }

    /**
     * Return aromatic bond separation
     */
    public float getAB_DEV() {
        return AB_DEV;
    }

    /**
     * Return wedge bond separation
     */
    public float getWB_DEV() {
        return WB_DEV;
    }

    /**
     * Return atom font
     */
    public Font getAtomFont() {
        return atomFont;
    }

    /**
     * Return bond color
     */
    public Color getBondColor() {
        return bondColor;
    }

    /**
     * Return flag that indicates whether atom labels should display
     */
    public boolean getShowAtomLabels() {
        return showAtomLabels;
    }

    /**
     * Return flag that indicates whether to paint the atom according to its type
     */
    public boolean getColorByAtomType() {
        return colorByAtomType;
    }

    /**
     * Return color scheme
     */
    public int getColorScheme() {
        return colorScheme;
    }

    /**
     * Return color to paint selected atoms
     */
    public Color getSelectColor() {
        return selectColor;
    }

    /**
     * Return color to paint hightlighted atoms
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Return atom background color
     */
    public Color getAtombg() {
        return atombg;
    }

    /**
     * @return the policy (in boolean) whether hidden atoms are pickable
     */
    public boolean getPickHiddenAtom() {
        return pickHiddenAtom;
    }

    /**
     * @return the policy (in boolean) whether hidden bonds are pickable
     */
    public boolean getPickHiddenBond() {
        return pickHiddenBond;
    }

    // setter section:

    /**
     * Set hydrogen treatment
     *
     * @param hTreatment int  one of ( SHOW_ALL_H, SHOW_NO_H, SHOW_NON_C_H )
     */
    public void setHTreatment(int hTreatment) {
        this.hTreatment = hTreatment;
    }

    /**
     * Set double bond separation
     *
     * @param dbDev float
     */
    public void setDB_DEV(float dbDev) {
        DB_DEV = dbDev;
    }

    /**
     * Set triple bond separation
     *
     * @param tbDev float
     */
    public void setTB_DEV(float tbDev) {
        TB_DEV = tbDev;
    }

    /**
     * Set aromatic bond separation
     *
     * @param abDev float
     */
    public void setAB_DEV(float abDev) {
        AB_DEV = abDev;
    }

    /**
     * Set wedge bond separation
     *
     * @param wbDev float
     */
    public void setWB_DEV(float wbDev) {
        WB_DEV = wbDev;
    }

    /**
     * Set atom font
     *
     * @param font Font to paint atom label
     */
    public void setAtomFont(Font font) {
        atomFont = font;
    }

    /**
     * Set bond color
     *
     * @param color Color to draw bond
     */
    public void setBondColor(Color color) {
        bondColor = color;
    }

    /**
     * Set flag to indicate whether atom labels should display or not
     *
     * @param bool boolean
     */
    public void setShowAtomLabels(boolean bool) {
        showAtomLabels = bool;
    }

    /**
     * Set flag to indicate whether to paint the atom according to its type
     * As data member colorByAtomType, this method is redundent, but
     * is kept to be compatible with pre-existing code
     *
     * @param bool boolean
     */
    public void setColorByAtomType(boolean bool) {
        colorByAtomType = bool;

        if (bool) {
            setColorScheme(COLOR_BY_ATOM_TYPE);
        } else {
            setColorScheme(ALL_ATOMS_ONE_COLOR);
        }
    }

    /**
     * Set color scheme
     *
     * @param colorScheme int
     */
    public void setColorScheme(int colorScheme) {
        switch (colorScheme) {
            case COLOR_BY_ATOM_TYPE:
                this.colorScheme = colorScheme;
                colorByAtomType = true;

                break;

            case ALL_ATOMS_ONE_COLOR:
                this.colorScheme = colorScheme;
                colorByAtomType = false;

            default:
                this.colorScheme = ALL_ATOMS_ONE_COLOR;
                colorByAtomType = false;

                break;
        }
    }

    /**
     * Set color to paint selected atoms
     *
     * @param color Color
     */
    public void setSelectColor(Color color) {
        selectColor = color;
    }

    /**
     * Set color to paint hightlighted atoms
     *
     * @param color Color
     */
    public void setHighlightColor(Color color) {
        highlightColor = color;
    }

    /**
     * Set atom background color
     *
     * @param color Color
     */
    public void setAtombg(Color color) {
        atombg = color;
    }

    /**
     * Set the policy whether hidden atoms are pickable
     *
     * @param pickHiddenAtom the policy whether hidden atoms are pickable
     */
    public void setPickHiddenAtom(boolean pickHiddenAtom) {
        this.pickHiddenAtom = pickHiddenAtom;
    }

    /**
     * Set the policy whether hidden bonds are pickable
     *
     * @param pickHiddenBond the policy whether hidden bonds are pickable
     */
    public void setPickHiddenBond(boolean pickHiddenBond) {
        this.pickHiddenBond = pickHiddenBond;
    }

    /**
     * Set the findRadius
     *
     * @param findRadius the upper bound of distance between a mouse click
     *                   location and the atom (or bond) when the atom (or bond) is a candidate
     *                   of the picked object.
     */
    public void setFindRadius(float findRadius) {
        this.findRadius = findRadius;
        ;
    }

    /**
     * Set the pickPreference policy
     *
     * @param pickPreference one of the picking policies among ATOM_FIRST,
     *                       BOND_FIRST, SHORTEST_WIN, ATOM_ONLY and BOND_ONLY
     * @throw IllegalArgumentException if argument pickPreference is not one of the valid values
     */
    public void setPickPreference(int pickPreference) {
        if ((pickPreference < PICK_ATOM_FIRST) ||
                (pickPreference > PICK_BOND_ONLY)) {
            String errorMsg =
                    "Argument to org.jscience.chemistry.gui.extended.molecule.Renderer::setPickPreference(int) is " +
                            "not one of PICK_ATOM_FIRST, PICK_BOND_FIRST, SHORTEST_WIN, PICK_ATOM_ONLY " +
                            "and PICK_BOND_ONLY defined in org.jscience.chemistry.gui.extended.molecule.Renderer. ";
            throw (new IllegalArgumentException(errorMsg));
        } else {
            this.pickPreference = pickPreference;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object copy() {
        return this;
    }
} //end of Renderer class
