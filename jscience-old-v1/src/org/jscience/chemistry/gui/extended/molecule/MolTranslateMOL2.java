//+-------------------------------------------------------------+
//|                                                             |
//|                                                             |
//|    (c) Copyright 1997 Tripos, Inc. All Rights Reserved      |
//|                                                             |
//|                                                             |
//+-------------------------------------------------------------+
package org.jscience.chemistry.gui.extended.molecule;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * Translates Tripos MOL2 files to/from Tripos Java Molecule format.
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 *         Change history:<br>
 *         <ul>
 *         <li> Rewrote the mol2 reader SR 3/21/97
 *         </ul>
 * @version 0.6 24 Mar 97
 * @see Molecule
 *      <p/>
 *      Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *      Eliminated AMIDE bond handling
 *      date: 20 Aug 1997
 */
public class MolTranslateMOL2 {
    /**
     * Translates from MOL2 to Molecule
     * If the InputStrean is from a multi-mol2 file,
     * only one molecule will be read at a time.
     *
     * @param in MOL2 input stream
     */
    public static Molecule toMolecule(InputStream in) {
        Molecule mol = new Molecule();

        DataInputStream dinp = new DataInputStream(in);
        StringTokenizer tok;
        String line = null;
        boolean atomDone = false;
        boolean bondDone = false;

        try {
            while (((line = dinp.readLine()) != null) && !atomDone &&
                    !bondDone) {
                line = line.trim(); // remove leading and trailing blanks

                if (line.startsWith("@<TRIPOS>MOLECULE")) {
                    // read the name of the molecule
                    line = dinp.readLine().trim();
                    mol.setName(line);

                    continue;
                }

                if (line.startsWith("@<TRIPOS>ATOM")) { // read the atoms
                    atomDone = true;

                    while ((line = dinp.readLine()) != null) {
                        line = line.trim();
                        tok = new StringTokenizer(line);

                        //System.out.println("Number of tokens: "+tok.countTokens());
                        if (tok.countTokens() < 6) {
                            break;
                        }

                        tok.nextToken(); // skip the first one

                        String atomName = tok.nextToken();
                        Float x;
                        Float y;
                        Float z;

                        try {
                            x = new Float(tok.nextToken());
                            y = new Float(tok.nextToken());
                            z = new Float(tok.nextToken());
                        } catch (NumberFormatException nfe) {
                            break;

                            // ??
                        }

                        String type = tok.nextToken();

                        //System.out.println("type/x/y/z: "+type+"/"+x+"/"+y+"/"+z);
                        Atom a = new Atom(x.floatValue(), y.floatValue(),
                                z.floatValue());
                        a.setType(type);
                        mol.addAtom(a);
                    }
                }

                if (line.startsWith("@<TRIPOS>BOND")) { // read the bonds
                    bondDone = true;

                    while ((line = dinp.readLine()) != null) {
                        line = line.trim();
                        tok = new StringTokenizer(line);

                        //System.out.println("Number of tokens: "+tok.countTokens());
                        if (tok.countTokens() < 4) {
                            break;
                        }

                        tok.nextToken(); // skip the first one

                        Integer from;
                        Integer to;

                        try {
                            from = new Integer(Integer.parseInt(tok.nextToken()) -
                                    1);
                            to = new Integer(Integer.parseInt(tok.nextToken()) -
                                    1);
                        } catch (NumberFormatException nfe) {
                            break;

                            // ??
                        }

                        String stype = tok.nextToken();
                        int btype = 0;

                        if (stype.equals("1")) {
                            btype = Bond.SINGLE;
                        } else if (stype.equals("2")) {
                            btype = Bond.DOUBLE;
                        } else if (stype.equals("3")) {
                            btype = Bond.TRIPLE;
                        } else if (stype.equalsIgnoreCase("ar")) {
                            btype = Bond.AROMATIC;
                        }
                        // eliminate AMIDE bond according to our plan, Z.Xie, 8/4/97
                        // else if (stype.equalsIgnoreCase("am")) btype = Bond.AMIDE;
                        else if (stype.equalsIgnoreCase("am")) {
                            btype = Bond.SINGLE;
                        } else if (stype.equalsIgnoreCase("du")) {
                            btype = Bond.ANY;
                        } else if (stype.equalsIgnoreCase("un")) {
                            btype = Bond.ANY;
                        }

                        if (btype != 0) {
                            //System.out.println("bond type/from/to: "+
                            //		btype+"/"+from+"/"+to);
                            Atom a1 = mol.myAtoms.getAtom(from.intValue());
                            Atom a2 = mol.myAtoms.getAtom(to.intValue());
                            Bond b = new Bond(a1, a2, btype);
                            mol.addBond(b);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            // ??
        }

        return mol;
    }

    /**
     * Translates from Molecule to MOL2
     *
     * @param in molecule to translate
     */
    public static String toMOL2(Molecule in) {
        String mol2 = new String();

        String ret = "\n"; // in case you want to change these values
        String tab = "\t";

        mol2 += ("#" + tab + "Name: Query" + ret);
        mol2 += ("#" + tab + "Creating user name: MolTranslate" + ret);
        mol2 += ("#" + tab + "Creation time: unknown" + ret);
        mol2 += ret;
        mol2 += ("#" + tab + "Modifying user name: MolTranslate" + ret);
        mol2 += ("#" + tab + "Modification time: unknown" + ret);
        mol2 += ret;
        mol2 += ("@<TRIPOS>MOLECULE" + ret);

        if ((in.name != null) && (in.name.length() > 0)) {
            mol2 += (in.name + ret);
        } else {
            mol2 += ("none" + ret);
        }

        mol2 += ("" + in.numAtoms + " " + in.numBonds + ret);
        mol2 += ("SMALL" + ret);
        mol2 += ("NO_CHARGES" + ret);
        mol2 += ret;
        mol2 += ("@<TRIPOS>ATOM" + ret);

        int hnum = 0;
        int onum = 0;

        for (int i = 0; i < in.numAtoms; i++) {
            Atom a = in.myAtoms.getAtom(i);
            mol2 += ("" + (i + 1) + tab + a.name);

            if (a.name.equals("H")) {
                hnum++;
                mol2 += ("" + hnum);
            } else {
                onum++;
                mol2 += ("" + onum);
            }

            mol2 += (tab + a.x + tab + a.y + tab + a.z + tab + a.type + ret);
        }

        mol2 += ("@<TRIPOS>BOND" + ret);

        for (int i = 0; i < in.numBonds; i++) {
            Bond b = in.myBonds.getBond(i);
            int a1 = in.myAtoms.indexOf(b.a1) + 1;
            int a2 = in.myAtoms.indexOf(b.a2) + 1;
            mol2 += ("" + (i + 1) + tab + a1 + tab + a2 + tab);

            switch (b.type) {
                default:
                case Bond.SINGLE:
                case Bond.DOUBLE:
                case Bond.TRIPLE:
                    mol2 += b.type;

                    break;

                case Bond.AROMATIC:
                    mol2 += "ar";

                    break;

                    // eliminate AMIDE bond according to our plan, Z.Xie, 8/4/97

                    /*
                      case Bond.AMIDE:
                      mol2 += "am";
                      break;
                    */
                case Bond.WEDGE:
                case Bond.DASH:
                    mol2 += "1";

                    break;

                case Bond.ANY:
                    mol2 += "un";
            }

            mol2 += ret;
        }

        return mol2;
    }
} // end of class MolTranslateMOL2
