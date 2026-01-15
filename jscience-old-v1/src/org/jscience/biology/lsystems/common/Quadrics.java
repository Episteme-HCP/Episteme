/*
 *        @(#)Quadrics.java 1.6 98/11/09 16:50:31
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
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class Quadrics extends Object {
/**
     * Creates a new Quadrics object.
     */
    Quadrics() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param lowr DOCUMENT ME!
     * @param highr DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param xdivisions DOCUMENT ME!
     * @param ydivisions DOCUMENT ME!
     * @param outside DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GeomBuffer cylinder(double lowr, double highr, double height,
        int xdivisions, int ydivisions, boolean outside) {
        double r;
        double sign;

        /*
         *     Cylinder is created by extruding the unit circle along the Z+
         * axis. The number of layers is controlled by ydivisions (which is
         * actually zdivisions). For each consecutive sample points along the
         * unit circle, we also sample along the height of the cylinder. Quads
         * are created along the Z direction. When the unit circle at the base is
         * completed, we also obtain the layers of quads along the height of the
         * cylinder.
         *
         *      Texture coordinates are created in a straight forward cylindrical
         * mapping of the texture map. The texture is wrapped from the back of
         * the cylinder.
         */
        if (outside) {
            sign = 1.0;
        } else {
            sign = -1.0;
        }

        //Compute the deltas
        double dtheta = (2.0 * Math.PI) / xdivisions;
        double dr = (highr - lowr) / ydivisions;
        double dz = height / ydivisions;
        double znormal = (lowr - highr) / height;
        double du = 1.0 / xdivisions;
        double dv = 1.0 / ydivisions;

        //Initialize geometry buffer.
        GeomBuffer gbuf = new GeomBuffer(xdivisions * ydivisions * 4);

        double s = 0.0;
        double t = 0.0;

        for (int i = 0; i < xdivisions; i++) {
            double px;
            double py;
            double qx;
            double qy;
            double z;

            // (px,py) and (qx,qy) are consecutive sample points along the
            // unit circle. We will create quads along the Z+ direction.
            px = -Math.sin(i * dtheta);
            py = Math.cos(i * dtheta);
            qx = -Math.sin((i + 1) * dtheta);
            qy = Math.cos((i + 1) * dtheta);

            // Initialize z,r,t
            z = (-1.0 * height) / 2.0;
            r = lowr;
            t = 0.0;

            gbuf.begin(GeomBuffer.QUAD_STRIP);

            // For each consecutive two unit circle points,
            // we obtain the layers of quads along the Z direction. Number of
            // layers depends on ydivisions.
            for (int j = 0; j <= ydivisions; j++, z += dz, r += dr, t += dv) {
                if ((j == ydivisions) && (highr == 0)) {
                    if (outside) {
                        gbuf.normal3d(0.0, 0.0, znormal * sign);
                        gbuf.texCoord2d(s, t);
                        gbuf.vertex3d(px * r, py * r, z);
                        gbuf.normal3d(0.0, 0.0, znormal * sign);
                        gbuf.texCoord2d(s + du, t);
                        gbuf.vertex3d(qx * r, qy * r, z);
                    } else {
                        gbuf.normal3d(0.0, 0.0, znormal * sign);
                        gbuf.texCoord2d(s, t);
                        gbuf.vertex3d(qx * r, qy * r, z);
                        gbuf.normal3d(0.0, 0.0, znormal * sign);
                        gbuf.texCoord2d(s + du, t);
                        gbuf.vertex3d(px * r, py * r, z);
                    }
                } else {
                    if (outside) {
                        gbuf.normal3d(px * sign, py * sign, znormal * sign);
                        gbuf.texCoord2d(s, t);
                        gbuf.vertex3d(px * r, py * r, z);
                        gbuf.normal3d(qx * sign, qy * sign, znormal * sign);
                        gbuf.texCoord2d(s + du, t);
                        gbuf.vertex3d(qx * r, qy * r, z);
                    } else {
                        gbuf.normal3d(qx * sign, qy * sign, znormal * sign);
                        gbuf.texCoord2d(s, t);
                        gbuf.vertex3d(qx * r, qy * r, z);
                        gbuf.normal3d(px * sign, py * sign, znormal * sign);
                        gbuf.texCoord2d(s + du, t);
                        gbuf.vertex3d(px * r, py * r, z);
                    }
                }
            }

            gbuf.end();
            s += du;
        }

        return gbuf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param xdivisions DOCUMENT ME!
     * @param outside DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GeomBuffer disk(double r, int xdivisions, boolean outside) {
        double theta;
        double dtheta;
        double sign;
        double sinTheta;
        double cosTheta;
        int i;

        /*
         *     Disk is created by evaluating points along the unit circle. Let
         * theta be the angle about the Z axis. Then, for each theta, we
         * obtain (cos(theta), sin(theta)) = (x,y) sample points. We create quad
         * strips (or fan strips) from these sample points.
         *
         *    Texture coordinates of the disk is gotten from a unit circle
         * centered at (0.5, 0.5) in s,t space. Thus, portions of a texture is not
         * used.
         */
        if (outside) {
            sign = 1.0;
        } else {
            sign = -1.0;
        }

        dtheta = (2.0 * Math.PI) / xdivisions;

        GeomBuffer gbuf = new GeomBuffer(xdivisions * 4);

        gbuf.begin(GeomBuffer.QUAD_STRIP);

        if (outside) {
            for (i = 0; i <= xdivisions; i++) {
                theta = i * dtheta;
                sinTheta = Math.sin(theta);
                cosTheta = Math.cos(theta);

                // First point of quad
                gbuf.normal3d(0.0, 0.0, 1.0 * sign);

                // Texture coord is centered at (0.5, 0.5) in s,t space.
                gbuf.texCoord2d(0.5 + (sinTheta * 0.5), 0.5 + (cosTheta * 0.5));
                gbuf.vertex3d(r * sinTheta, r * cosTheta, 0.0);

                // Second point
                gbuf.normal3d(0.0, 0.0, 1.0 * sign);
                gbuf.texCoord2d(0.5, 0.5);
                gbuf.vertex3d(0.0, 0.0, 0.0);
            }
        } else {
            for (i = xdivisions; i >= 0; i--) {
                theta = i * dtheta;
                sinTheta = Math.sin(theta);
                cosTheta = Math.cos(theta);
                gbuf.normal3d(0.0, 0.0, 1.0 * sign);
                gbuf.texCoord2d(0.5 - (sinTheta * 0.5), 0.5 + (cosTheta * 0.5));
                gbuf.vertex3d(r * sinTheta, r * cosTheta, 0.0);
                gbuf.normal3d(0.0, 0.0, 1.0 * sign);
                gbuf.texCoord2d(0.5, 0.5);
                gbuf.vertex3d(0.0, 0.0, 0.0);
            }
        }

        gbuf.end();

        return gbuf;
    }
}
