/*
 *        @(#)GeomBuffer.java 1.14 98/11/23 13:45:05
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.biology.lsystems.common;

//import com.sun.j3d.utils.geometry.*;

import javax.media.j3d.*;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * GeomBuffer allows OpenGL-like input of geometry data. It outputs
 * Java 3D geometry array objects. This utility is to simplify porting
 * of OpenGL programs to Java 3D.
 * <p/>
 * Here is a sample code that use this utility to create some quads.
 * <P><blockquote><pre>
 * <p/>
 *     GeomBuffer gbuf = new GeomBuffer(100);
 *     gbuf.begin(GeomBuffer.QUADS);
 * <p/>
 *     for (int i = 0; i < 5; i++){
 *       gbuf.normal3d(0.0, 1.0, 0.0);
 *       gbuf.vertex3d(1.0, 1.0, 0.0);
 * <p/>
 *       gbuf.normal3d(0.0, 1.0, 0.0);
 *       gbuf.vertex3d(0.0, 1.0, 0.0);
 * <p/>
 *       gbuf.normal3d(0.0, 1.0, 0.0);
 *       gbuf.vertex3d(0.0, 0.0, 0.0);
 * <p/>
 *       gbuf.normal3d(0.0, 1.0, 0.0);
 *       gbuf.vertex3d(1.0, 0.0, 0.0);
 *     }
 *     gbuf.end();
 *     Shape3D shape = new Shape3D(gbuf.getGeom(GeomBuffer.GENERATE_NORMALS));
 * </pre></blockquote>
 * Notice, that you only need to specify some upperbound on the number of
 * points you'll use at the beginning (100 in this case).
 * <p/>
 * Currently, you are limited to one primitive type per geom buffer. Future
 * versions will add support for mixed primitive types.
 */
class GeomBuffer extends Object {
    //Supported Primitives
    static final int QUAD_STRIP = 0x01;
    static final int TRIANGLES = 0x02;
    static final int QUADS = 0x04;
    static final int GENERATE_NORMALS = 0x01;
    static final int GENERATE_TEXTURE_COORDS = 0x02;
    static final int debug = 0;
    private int flags;
    Point3f[] pts = null;
    Vector3f[] normals = null;
    Point2f[] tcoords = null;
    int currVertCnt;
    int currPrimCnt;
    int[] currPrimType = null;
    int[] currPrimStartVertex = null;
    int[] currPrimEndVertex = null;
    GeometryArray geometry;
    int numVerts = 0;
    int numTris = 0;

    /**
     * Creates a geometry buffer of given number of vertices
     *
     * @param numVerts total number of vertices to allocate by this buffer.
     *                 This is an upper bound estimate.
     */
    GeomBuffer(int numVerts) {
        pts = new Point3f[numVerts];
        normals = new Vector3f[numVerts];
        tcoords = new Point2f[numVerts];

        // max primitives is numV/3
        currPrimType = new int[numVerts / 3];
        currPrimStartVertex = new int[numVerts / 3];
        currPrimEndVertex = new int[numVerts / 3];
        currVertCnt = 0;
        currPrimCnt = 0;
    }

    /*
    * Returns a Java 3D geometry array from the geometry buffer. You need to
    * call begin, vertex3d, end, etc. before calling this, of course.
    *
    * @param format vertex format.
    */
    GeometryArray getGeom(int format) {
        GeometryArray obj;
        flags = format;

        numTris = 0;

        //Switch based on first primitive.
        switch (currPrimType[0]) {
            case TRIANGLES:
                obj = processTriangles();

                if ((flags & Primitive.ENABLE_GEOMETRY_PICKING) != 0) {
                    obj.setCapability(Geometry.ALLOW_INTERSECT);
                }

                return obj;

            case QUADS:
                obj = processQuads();

                if ((flags & Primitive.ENABLE_GEOMETRY_PICKING) != 0) {
                    obj.setCapability(Geometry.ALLOW_INTERSECT);
                }

                return obj;

            case QUAD_STRIP:
                obj = processQuadStrips();

                if ((flags & Primitive.ENABLE_GEOMETRY_PICKING) != 0) {
                    obj.setCapability(Geometry.ALLOW_INTERSECT);
                }

                return obj;
        }

        return null;
    }

    /**
     * Begins a new primitive given the primitive type.
     *
     * @param prim the primitive type (listed above).
     */
    void begin(int prim) {
        if (debug >= 1) {
            System.out.println("quad");
        }

        currPrimType[currPrimCnt] = prim;
        currPrimStartVertex[currPrimCnt] = currVertCnt;
    }

    /**
     * End of primitive.
     */
    void end() {
        if (debug >= 1) {
            System.out.println("end");
        }

        currPrimEndVertex[currPrimCnt] = currVertCnt;
        currPrimCnt++;
    }

    void vertex3d(double x, double y, double z) {
        if (debug >= 2) {
            System.out.println("v " + x + " " + y + " " + z);
        }

        pts[currVertCnt] = new Point3f((float) x, (float) y, (float) z);
        currVertCnt++;
    }

    void normal3d(double x, double y, double z) {
        if (debug >= 2) {
            System.out.println("n " + x + " " + y + " " + z);
        }

        double sum = (x * x) + (y * y) + (z * z);

        if (Math.abs(sum - 1.0) > 0.001) {
            if (debug >= 2) {
                System.out.println("normalizing");
            }

            double root = Math.sqrt(sum);

            if (root > 0.000001) {
                x /= root;
                y /= root;
                z /= root;
            } else {
                y = z = 0.0;
                x = 1.0;
            }
        }

        normals[currVertCnt] = new Vector3f((float) x, (float) y, (float) z);
    }

    void texCoord2d(double s, double t) {
        if (debug >= 2) {
            System.out.println("t " + s + " " + t);
        }

        tcoords[currVertCnt] = new Point2f((float) s, (float) t);
    }

    /**
     * Returns the Java 3D geometry gotten from calling getGeom.
     */
    GeometryArray getComputedGeometry() {
        return geometry;
    }

    int getNumTris() {
        return numTris;
    }

    int getNumVerts() {
        return numVerts;
    }

    private GeometryArray processQuadStrips() {
        GeometryArray obj = null;
        int i;
        int totalVerts = 0;

        // Calculate how many vertices needed to hold all of the individual quads
        int[] stripCounts = new int[currPrimCnt];

        for (i = 0; i < currPrimCnt; i++) {
            stripCounts[i] = currPrimEndVertex[i] - currPrimStartVertex[i];
            totalVerts += stripCounts[i];
        }

        if (debug >= 1) {
            System.out.println("totalVerts " + totalVerts);
        }

        int tsaFlags = TriangleStripArray.COORDINATES;

        if ((flags & GENERATE_NORMALS) != 0) {
            tsaFlags |= TriangleStripArray.NORMALS;
        }

        if ((flags & GENERATE_TEXTURE_COORDS) != 0) {
            tsaFlags |= TriangleStripArray.TEXTURE_COORDINATE_2;
        }

        // Create GeometryArray to pass back
        obj = new TriangleStripArray(totalVerts, tsaFlags, stripCounts);

        // Allocate space to store new vertex info
        Point3f[] newpts = new Point3f[totalVerts];
        Vector3f[] newnormals = new Vector3f[totalVerts];
        Point2f[] newtcoords = new Point2f[totalVerts];
        int currVert = 0;

        // Repeat for each Quad Strip
        for (i = 0; i < currPrimCnt; i++) {
            // Output order for these quad arrays same as java 3d triangle strips
            for (int j = currPrimStartVertex[i]; j < currPrimEndVertex[i];
                 j++) {
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j);
            }
        }

        numVerts = currVert;
        numTris += (totalVerts - (currPrimCnt * 2));

        obj.setCoordinates(0, newpts);

        if ((flags & GENERATE_NORMALS) != 0) {
            obj.setNormals(0, newnormals);
        }

        if ((flags & GENERATE_TEXTURE_COORDS) != 0) {
            obj.setTextureCoordinates(0, newtcoords);
        }

        geometry = obj;

        return obj;
    }

    private GeometryArray processQuads() {
        GeometryArray obj = null;
        int i;
        int totalVerts = 0;

        for (i = 0; i < currPrimCnt; i++) {
            totalVerts += (currPrimEndVertex[i] - currPrimStartVertex[i]);
        }

        if (debug >= 1) {
            System.out.println("totalVerts " + totalVerts);
        }

        if (((flags & GENERATE_NORMALS) != 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) != 0)) {
            obj = new QuadArray(totalVerts,
                    QuadArray.COORDINATES | QuadArray.NORMALS |
                            QuadArray.TEXTURE_COORDINATE_2);
        } else if (((flags & GENERATE_NORMALS) == 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) != 0)) {
            obj = new QuadArray(totalVerts,
                    QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        } else if (((flags & GENERATE_NORMALS) != 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) == 0)) {
            obj = new QuadArray(totalVerts,
                    QuadArray.COORDINATES | QuadArray.NORMALS);
        } else {
            obj = new QuadArray(totalVerts, QuadArray.COORDINATES);
        }

        Point3f[] newpts = new Point3f[totalVerts];
        Vector3f[] newnormals = new Vector3f[totalVerts];
        Point2f[] newtcoords = new Point2f[totalVerts];
        int currVert = 0;

        if (debug > 1) {
            System.out.println("total prims " + currPrimCnt);
        }

        for (i = 0; i < currPrimCnt; i++) {
            if (debug > 1) {
                System.out.println("start " + currPrimStartVertex[i] + " end " +
                        currPrimEndVertex[i]);
            }

            for (int j = currPrimStartVertex[i];
                 j < (currPrimEndVertex[i] - 3); j += 4) {
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j);
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j + 1);
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j + 2);
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j + 3);
                numTris += 2;
            }
        }

        numVerts = currVert;

        obj.setCoordinates(0, newpts);

        if ((flags & GENERATE_NORMALS) != 0) {
            obj.setNormals(0, newnormals);
        }

        if ((flags & GENERATE_TEXTURE_COORDS) != 0) {
            obj.setTextureCoordinates(0, newtcoords);
        }

        geometry = obj;

        return obj;
    }

    private GeometryArray processTriangles() {
        GeometryArray obj = null;
        int i;
        int totalVerts = 0;

        for (i = 0; i < currPrimCnt; i++) {
            totalVerts += (currPrimEndVertex[i] - currPrimStartVertex[i]);
        }

        if (debug >= 1) {
            System.out.println("totalVerts " + totalVerts);
        }

        if (((flags & GENERATE_NORMALS) != 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) != 0)) {
            obj = new TriangleArray(totalVerts,
                    TriangleArray.COORDINATES | TriangleArray.NORMALS |
                            TriangleArray.TEXTURE_COORDINATE_2);
        } else if (((flags & GENERATE_NORMALS) == 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) != 0)) {
            obj = new TriangleArray(totalVerts,
                    TriangleArray.COORDINATES |
                            TriangleArray.TEXTURE_COORDINATE_2);
        } else if (((flags & GENERATE_NORMALS) != 0) &&
                ((flags & GENERATE_TEXTURE_COORDS) == 0)) {
            obj = new TriangleArray(totalVerts,
                    TriangleArray.COORDINATES | TriangleArray.NORMALS);
        } else {
            obj = new TriangleArray(totalVerts, TriangleArray.COORDINATES);
        }

        Point3f[] newpts = new Point3f[totalVerts];
        Vector3f[] newnormals = new Vector3f[totalVerts];
        Point2f[] newtcoords = new Point2f[totalVerts];
        int currVert = 0;

        for (i = 0; i < currPrimCnt; i++) {
            for (int j = currPrimStartVertex[i];
                 j < (currPrimEndVertex[i] - 2); j += 3) {
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j);
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j + 1);
                outVertex(newpts, newnormals, newtcoords, currVert++, pts,
                        normals, tcoords, j + 2);
                numTris += 1;
            }
        }

        numVerts = currVert;

        obj.setCoordinates(0, newpts);

        if ((flags & GENERATE_NORMALS) != 0) {
            obj.setNormals(0, newnormals);
        }

        if ((flags & GENERATE_TEXTURE_COORDS) != 0) {
            obj.setTextureCoordinates(0, newtcoords);
        }

        geometry = obj;

        return obj;
    }

    void outVertex(Point3f[] dpts, Vector3f[] dnormals, Point2f[] dtcoords,
                   int dloc, Point3f[] spts, Vector3f[] snormals, Point2f[] stcoords,
                   int sloc) {
        if (debug >= 1) {
            System.out.println("v " + spts[sloc].x + " " + spts[sloc].y + " " +
                    spts[sloc].z);
        }

        // PSP: Do we really need new points here?
        dpts[dloc] = new Point3f(spts[sloc]);

        if ((flags & GENERATE_NORMALS) != 0) {
            dnormals[dloc] = new Vector3f(snormals[sloc]);
        }

        if ((flags & GENERATE_TEXTURE_COORDS) != 0) {
            if (debug >= 2) {
                System.out.println("final out tcoord");
            }

            dtcoords[dloc] = new Point2f(stcoords[sloc]);
        }
    }
}
