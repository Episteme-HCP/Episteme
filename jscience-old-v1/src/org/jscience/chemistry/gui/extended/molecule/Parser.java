package org.jscience.chemistry.gui.extended.molecule;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Properties;
import java.util.Vector;

/**
 * Reads and parses a Tripos Java Molecule (JMol) stream into a JMol class.
 *
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Original version
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Replaced molecule package, from jmol package
 *         Substituted text: JMol to Molecule, most jmol to mol and some jmol to molecule
 *         date: 14 Aug 97
 */
public class Parser {
    static StreamTokenizer sTok;

    /**
     * Parse the molecule header.
     *
     * @param mol the molecule class being created
     * @throws IOException
     */
    private static void handleMolecule(Molecule mol) throws IOException {
        if (sTok.nextToken() != '{') {
            return;
        }

        sTok.nextToken();
        mol.id = (int) sTok.nval;
        sTok.nextToken();
        mol.setName(sTok.sval);

        ignoreRestOfEntry();
    }

    /**
     * Parse the atom section.
     *
     * @param mol the molecule class being created
     * @throws IOException
     */
    private static void handleAtoms(Molecule mol) throws IOException {
        int id;
        String name;
        float x;
        float y;
        float z;
        int charge;

        while (true) {
            if (sTok.nextToken() != '{') {
                sTok.pushBack();

                break;
            }

            sTok.nextToken();
            id = (int) sTok.nval;
            sTok.nextToken();
            name = sTok.sval;
            sTok.nextToken();
            x = (float) sTok.nval;
            sTok.nextToken();
            y = (float) sTok.nval;
            sTok.nextToken();
            z = (float) sTok.nval;
            sTok.nextToken();
            charge = (int) sTok.nval;

            Atom a = new Atom(id, name, x, y, z, x, y, z, charge, null);
            addProps(a.getAttributes());
            mol.addAtom(a);

            if (ignoreRestOfEntry() == StreamTokenizer.TT_EOF) {
                break;
            }
        }
    }

    /**
     * This routine add the properties string pairs to the
     * property parameter passed in
     */
    private static void addProps(Properties prop) throws IOException {
        int token;
        String key;
        String value;

        token = sTok.nextToken();

        while ((token != StreamTokenizer.TT_EOF) && (token != '}')) {
            // Get the key
            if ((sTok.ttype == StreamTokenizer.TT_WORD) || (token == '"')) {
                key = sTok.sval;
            } else {
                key = Double.toString(sTok.nval);
            }

            token = sTok.nextToken();

            // Get the value
            if ((token != StreamTokenizer.TT_EOF) && (token != '}')) {
                if ((sTok.ttype == StreamTokenizer.TT_WORD) || (token == '"')) {
                    value = sTok.sval;
                } else {
                    value = Double.toString(sTok.nval);
                }

                token = sTok.nextToken();
            } else {
                value = "1";
            }

            prop.put(key, value);
        }

        // Save the EOF or } for the ignoreRestOfEntry() call
        sTok.pushBack();
    }

    /**
     * Parse the bond section.
     *
     * @param mol the molecule class being created
     * @throws IOException
     */
    private static void handleBonds(Molecule mol) throws IOException {
        int id;
        int type;
        int from;
        int to;
        int ringId;

        while (true) {
            if (sTok.nextToken() != '{') {
                sTok.pushBack();

                break;
            }

            sTok.nextToken();
            id = (int) sTok.nval;
            sTok.nextToken();
            type = (int) sTok.nval;
            sTok.nextToken();
            from = (int) sTok.nval;
            sTok.nextToken();
            to = (int) sTok.nval;
            sTok.nextToken();
            ringId = (int) sTok.nval;

            Integer f;
            Integer t;
            f = (Integer) mol.atomIdToIndex.get(new Integer(from));
            t = (Integer) mol.atomIdToIndex.get(new Integer(to));

            Atom a1 = mol.myAtoms.getAtom(f.intValue());
            Atom a2 = mol.myAtoms.getAtom(t.intValue());

            Bond b = new Bond(id, type, a1, a2, ringId);

            addProps(b.getAttributes());

            mol.addBond(b);

            if (ignoreRestOfEntry() == StreamTokenizer.TT_EOF) {
                break;
            }
        }
    }

    /**
     * Parse the ring section.
     *
     * @param mol the molecule class being created
     * @throws IOException
     */
    private static void handleRings(Molecule mol) throws IOException {
        int id;
        float x;
        float y;
        float z;
        int numAtoms;
        int[] atoms;

        while (true) {
            if (sTok.nextToken() != '{') {
                sTok.pushBack();

                break;
            }

            sTok.nextToken();
            id = (int) sTok.nval;
            sTok.nextToken();
            x = (float) sTok.nval;
            sTok.nextToken();
            y = (float) sTok.nval;
            sTok.nextToken();
            z = (float) sTok.nval;
            sTok.nextToken();
            numAtoms = (int) sTok.nval;

            if (numAtoms > 0) {
                atoms = new int[numAtoms];

                for (int i = 0; i < numAtoms; i++) {
                    sTok.nextToken();
                    atoms[i] = (int) sTok.nval;
                }
            } else {
                atoms = null;
            }

            mol.addRing(new Ring(id, x, y, z, x, y, z, numAtoms, atoms));

            if (ignoreRestOfEntry() == StreamTokenizer.TT_EOF) {
                break;
            }
        }
    }

    /**
     * Parse the properties section.
     *
     * @param mol the molecule class being created
     * @throws IOException
     */
    private static void handleProperties(Molecule mol)
            throws IOException {
        String name;
        String value;

        while (true) {
            if (sTok.nextToken() != '{') {
                sTok.pushBack();

                break;
            }

            sTok.nextToken();
            name = sTok.sval;
            sTok.nextToken();
            value = sTok.sval;

            mol.addProperty(name, value);

            if (ignoreRestOfEntry() == StreamTokenizer.TT_EOF) {
                break;
            }
        }
    }

    /**
     * Skip the rest of an entry (bound by an ending brace).
     *
     * @throws IOException
     */
    private static int ignoreRestOfEntry() throws IOException {
        int token;

        while ((token = sTok.nextToken()) != StreamTokenizer.TT_EOF)

            if (sTok.ttype == '}') {
                break;
            }

        if (token == StreamTokenizer.TT_EOF) {
            sTok.pushBack();
        }

        return token;
    }

    /**
     * Read and parse the input Molecule stream.
     *
     * @param in the input molecule stream
     * @throws IOException
     */
    public static Vector parseMolecule(InputStream in) {
        Vector mols = new Vector();
        Molecule mol = new Molecule();

        sTok = new StreamTokenizer(new DataInputStream(in));
        sTok.whitespaceChars('=', '=');
        sTok.wordChars('_', '_');
        sTok.wordChars('(', '(');
        sTok.wordChars(')', ')');
        sTok.wordChars(',', ',');

        try {
            while (sTok.nextToken() != StreamTokenizer.TT_EOF) {
                if (sTok.sval == null) {
                    continue;
                }

                if (sTok.sval.equalsIgnoreCase("MOLECULE")) {
                    handleMolecule(mol);
                } else if (sTok.sval.equalsIgnoreCase("ATOMS")) {
                    handleAtoms(mol);
                } else if (sTok.sval.equalsIgnoreCase("BONDS")) {
                    handleBonds(mol);
                } else if (sTok.sval.equalsIgnoreCase("RINGS")) {
                    handleRings(mol);
                } else if (sTok.sval.equalsIgnoreCase("PROPERTIES")) {
                    handleProperties(mol);
                } else if (sTok.sval.equalsIgnoreCase("END_MOLECULE")) {
                    mols.addElement(mol);
                    mol = new Molecule();
                }
            }
        } catch (IOException e) {
            System.err.println(e);

            return null;
        }

        return mols;
    }
}
