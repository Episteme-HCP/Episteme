package org.jscience.chemistry.gui.extended.graphics3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Shape3D;


// Doesn't work
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IndexedCylinder {
    /** DOCUMENT ME! */
    float[] verts;

    /** DOCUMENT ME! */
    float[] normals;

    /** DOCUMENT ME! */
    int[] idx;

    /** DOCUMENT ME! */
    int[] normidx;

    /** DOCUMENT ME! */
    IndexedQuadArray quad = null;

    /** DOCUMENT ME! */
    float div = 3.0f;

    /** DOCUMENT ME! */
    Shape3D shape;

/**
     * Creates a new IndexedCylinder object.
     *
     * @param radius  DOCUMENT ME!
     * @param length  DOCUMENT ME!
     * @param quality DOCUMENT ME!
     * @param a       DOCUMENT ME!
     */
    public IndexedCylinder(float radius, float length, int quality, Appearance a) {
        //System.out.println("Radius: "+radius);
        if (quality < 3) {
            quality = 3;
        }

        div = (float) quality;

        verts = new float[quality * 6];
        normals = new float[quality * 3];
        normidx = new int[quality * 4];
        idx = new int[quality * 4];

        double inc = (2.0 * Math.PI) / (double) div;

        for (int i = 0; i < quality; i++) {
            float z = radius * (float) Math.sin((double) i * inc);
            float x = radius * (float) Math.cos((double) i * inc);
            verts[3 * i] = x;
            verts[(3 * i) + 1] = 0.0f;
            verts[(3 * i) + 2] = z;

            System.out.println("coor: " + verts[3 * i] + " " +
                verts[(3 * i) + 1] + " " + verts[(3 * i) + 2]);
            verts[3 * (i + quality)] = x;
            verts[(3 * (i + quality)) + 1] = length;
            verts[(3 * (i + quality)) + 2] = z;

            //System.out.println("coor: "+verts[3*(i+quality)]+" "+
            //  verts[3*(i+quality)+1]+" "+
            //  verts[3*(i+quality)+2]);
            normals[3 * i] = (float) Math.sin(((double) i * inc) + (0.5 * inc));
            normals[(3 * i) + 1] = 0.0f;
            normals[(3 * i) + 2] = (float) Math.cos(((double) i * inc) +
                    (0.5 * inc));
            System.out.println("nx/ny/nz: " + normals[3 * i] + "/" +
                normals[(3 * i) + 1] + "/" + normals[(3 * i) + 2]);

            normidx[i * 4] = i;
            normidx[(i * 4) + 1] = i;
            normidx[(i * 4) + 2] = i;
            normidx[(i * 4) + 3] = i;

            idx[i * 4] = i;
            idx[(i * 4) + 1] = i + quality;
            idx[(i * 4) + 2] = i + quality + 1;
            idx[(i * 4) + 3] = i + 1;
        }

        idx[((quality - 1) * 4) + 1] = 0;
        idx[((quality - 1) * 4) + 2] = quality;

        normidx[((quality - 1) * 4) + 1] = 0;
        normidx[((quality - 1) * 4) + 2] = 0;

        quad = new IndexedQuadArray(quality * 2,
                IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS,
                quality * 4);
        quad.setCoordinates(0, verts);
        quad.setCoordinateIndices(0, idx);

        quad.setNormals(0, normals);
        quad.setNormalIndices(0, normidx);

        shape = new Shape3D(quad, a);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Shape3D getShape() {
        return shape;
    }
}
