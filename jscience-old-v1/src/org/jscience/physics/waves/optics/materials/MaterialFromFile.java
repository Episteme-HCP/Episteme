/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.materials;

import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MaterialFromFile {
/**
     * Creates a new MaterialFromFile object.
     */
    public MaterialFromFile() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    static public void readFile(File f) {
        BufferedReader bf;
        Material m = null;

        if (f.exists() && f.canRead()) {
            try {
                bf = new BufferedReader(new FileReader(f));

                //String s = null;
                do {
                    m = readMaterial(bf);

                    if (m != null) {
                        System.out.println(m.getName());
                    }
                } while (m != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param bf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static private Material readMaterial(BufferedReader bf)
        throws IOException {
        String s = bf.readLine();

        if (s != null) {
            Material mat = new Material();
            mat.setName(s);

            s = bf.readLine();

            while ((s != null) && !s.equals("")) {
                try {
                    mat.addParameterSet(s);
                } catch (IllegalDimensionException e) {
                    System.out.println(mat.getName() + ": " + e.toString());
                } catch (InvalidMethodTypeException e) {
                    System.out.println("Unknown method: " + e.name);
                } catch (NumberFormatException e) {
                    System.out.println(mat.getName() + ": " +
                        "NumberFormatException");
                }

                s = bf.readLine();
            }

            return mat;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static void main(String[] args) {
        File f = new File((new File("Materials.txt")).getAbsolutePath());

        readFile(f);
    }
}
