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

package org.jscience.astronomy;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.mathematics.geometry.LiteralVector3D;
import org.jscience.mathematics.geometry.Rotation3D;
import org.jscience.mathematics.geometry.Vector3D;

import javax.vecmath.Point3d;


/**
 * The AstronomyUtils class provides useful time computation related
 * methods.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//There exist five fixed points in the CR3BP (Circular Restricted Three-Body Problem). Also known as Lagrangian, or Libration points, these points represent points in space where an unmoving particle would remain for all time. These fixed points are all points where all six phase variables time derivatives are zero. In other words, the libration points, are solutions to the equations of motion for the spacecraft such that the x, y, and z positions and x, y, and z velocities are all 0.
public final class AstronomyUtils extends Object {
    //http://en.wikipedia.org/wiki/Lagrangian_point
    //http://www.geom.uiuc.edu/~megraw/MATH1/lib.html

    //To find the location of the 3 libration points lying on the earth-sun axis (L1, L2, and L3),
    //one must solve for the x coordinate of each. This boils down to solving a quintic polynomial
    //equation. It can be proven that each polynomial has one and only one root for mu in the range
    //of 0 to 1.

    //mu is the ratio of mass

    //this is my first try for an implementation
    //it relies on polynomial roots which is perhaps not very accurate
    //I didn't have the time to check so this method may not work
    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     * @param body2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Point3d getLagrangeL1(AstralBody body1, AstralBody body2)
        throws Exception {
        // -((1-mu)*(1-mu+x)2)+mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly;

        // -((1-mu)*(1-mu+x)2)+mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly2;
        double mu;
        Complex[] roots;

        if (body1.getMass() > body2.getMass()) {
            mu = body2.getMass() / body1.getMass();
        } else {
            mu = body1.getMass() / body2.getMass();
        }

        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { -(1 -
                    mu) }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(
                    new DoublePolynomial(new double[] { 1 - mu, 1 })));
        poly = new DoublePolynomial(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { mu }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { mu, 1 })));
        poly = (DoublePolynomial) poly.add(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { 0, 1 }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { 1 - mu, 1 }))
                                                                                                                                                           .multiply((DoublePolynomial) new DoublePolynomial(
                        new double[] { mu, 1 }).multiply(new DoublePolynomial(
                            new double[] { mu, 1 }))));
        poly = (DoublePolynomial) poly.add(poly2);
        roots = poly.roots();

        //there should be only one real root
        if (roots.length != 1) {
            throw new Exception("Unable to compute the lagrange point.");
        }

        return new Point3d(body1.getPosition().getPrimitiveElement(0) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(0) -
            body1.getPosition().getPrimitiveElement(0))),
            body1.getPosition().getPrimitiveElement(1) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(1) -
            body1.getPosition().getPrimitiveElement(1))),
            body1.getPosition().getPrimitiveElement(2) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(2) -
            body1.getPosition().getPrimitiveElement(2))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     * @param body2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Point3d getLagrangeL2(AstralBody body1, AstralBody body2)
        throws Exception {
        // -((1-mu)*(1-mu+x)2)-mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly;

        // -((1-mu)*(1-mu+x)2)-mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly2;
        double mu;
        Complex[] roots;

        if (body1.getMass() > body2.getMass()) {
            mu = body2.getMass() / body1.getMass();
        } else {
            mu = body1.getMass() / body2.getMass();
        }

        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { -(1 -
                    mu) }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(
                    new DoublePolynomial(new double[] { 1 - mu, 1 })));
        poly = new DoublePolynomial(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { mu }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { mu, 1 })));
        poly = (DoublePolynomial) poly.subtract(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { 0, 1 }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { 1 - mu, 1 }))
                                                                                                                                                           .multiply((DoublePolynomial) new DoublePolynomial(
                        new double[] { mu, 1 }).multiply(new DoublePolynomial(
                            new double[] { mu, 1 }))));
        poly = (DoublePolynomial) poly.add(poly2);
        roots = poly.roots();

        //there should be only one real root
        if (roots.length != 1) {
            throw new Exception("Unable to compute the lagrange point.");
        }

        return new Point3d(body1.getPosition().getPrimitiveElement(0) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(0) -
            body1.getPosition().getPrimitiveElement(0))),
            body1.getPosition().getPrimitiveElement(1) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(1) -
            body1.getPosition().getPrimitiveElement(1))),
            body1.getPosition().getPrimitiveElement(2) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(2) -
            body1.getPosition().getPrimitiveElement(2))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     * @param body2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Point3d getLagrangeL3(AstralBody body1, AstralBody body2)
        throws Exception {
        // (1-mu)*(1-mu+x)2+mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly;

        // (1-mu)*(1-mu+x)2+mu*(mu+x)2+x*(1-mu+x)2(mu+x)2 = 0
        DoublePolynomial poly2;
        double mu;
        Complex[] roots;

        if (body1.getMass() > body2.getMass()) {
            mu = body2.getMass() / body1.getMass();
        } else {
            mu = body1.getMass() / body2.getMass();
        }

        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { 1 - mu }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { 1 - mu, 1 })));
        poly = new DoublePolynomial(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { mu }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { mu, 1 })));
        poly = (DoublePolynomial) poly.add(poly2);
        poly2 = (DoublePolynomial) new DoublePolynomial(new double[] { 0, 1 }).multiply((DoublePolynomial) new DoublePolynomial(
                    new double[] { 1 - mu, 1 }).multiply(new DoublePolynomial(
                        new double[] { 1 - mu, 1 }))
                                                                                                                                                           .multiply((DoublePolynomial) new DoublePolynomial(
                        new double[] { mu, 1 }).multiply(new DoublePolynomial(
                            new double[] { mu, 1 }))));
        poly = (DoublePolynomial) poly.add(poly2);
        roots = poly.roots();

        //there should be only one real root
        if (roots.length != 1) {
            throw new Exception("Unable to compute the lagrange point.");
        }

        return new Point3d(body1.getPosition().getPrimitiveElement(0) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(0) -
            body1.getPosition().getPrimitiveElement(0))),
            body1.getPosition().getPrimitiveElement(1) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(1) -
            body1.getPosition().getPrimitiveElement(1))),
            body1.getPosition().getPrimitiveElement(2) +
            (roots[0].real() * (body2.getPosition().getPrimitiveElement(2) -
            body1.getPosition().getPrimitiveElement(2))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     * @param body2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Point3d getLagrangeL4(AstralBody body1, AstralBody body2)
        throws Exception {
        //Two solutions fall out easily; each forms and equilateral triangle with the earth and the sun in the plane of rotation
        Point3d result;

        if (body1.getMass() > body2.getMass()) {
            //body2 revolves around body1
            //velocity vector of body1 and axis from body1 to body2 defines the plane of rotation P
             Double3Vector relativeSpeed = new Double3Vector((Double3Vector)body2.getVelocity());
            relativeSpeed.subtract((Double3Vector)body1.getVelocity());
            Double3Vector vectorFrom1To2 = new Double3Vector((Double3Vector)body2.getPosition());
            vectorFrom1To2.subtract((Double3Vector)body1.getPosition());
            Double3Vector vectorFrom2ToVDT = new Double3Vector((Double3Vector)body2.getPosition());
            vectorFrom2ToVDT.add(relativeSpeed);
            //compute cross product of  vectorFrom1To2  and vectorFrom2ToVDT
             //we have a normal vector to the plane
               Double3Vector crossproduct = vectorFrom1To2.multiply(vectorFrom2ToVDT);
            //L5 is in P at +60 degrees clockwise from body1 and at distance d(body1, body2)
            //to get it now rotate vectorFrom1To2 60degrees clockwise
            //lagrangian is the point you get by applying the new vector onto point 1
            Rotation3D rotation3D = new Rotation3D(new LiteralVector3D(crossproduct.x, crossproduct.y, crossproduct.z), -Math.PI/3);
            Vector3D resultVector = rotation3D.applyTo(new LiteralVector3D(vectorFrom1To2.x, vectorFrom1To2.y, vectorFrom1To2.z));
            result = new Point3d(resultVector.x() + ((Double3Vector)body1.getPosition()).x, resultVector.y() + ((Double3Vector)body1.getPosition()).y, resultVector.z() + ((Double3Vector)body1.getPosition()).z);
        } else {
            //body1 revolves around body2
            //velocity vector of body2 and axis from body2 to body1 defines the plane of rotation P
            //L5 is in P at +60 degrees counterclockwise from body1 and at distance d(body1, body2)
            Double3Vector relativeSpeed = new Double3Vector((Double3Vector)body1.getVelocity());
            relativeSpeed.subtract((Double3Vector)body2.getVelocity());
            Double3Vector vectorFrom2To1 = new Double3Vector((Double3Vector)body1.getPosition());
            vectorFrom2To1.subtract((Double3Vector)body2.getPosition());
            Double3Vector vectorFrom1ToVDT = new Double3Vector((Double3Vector)body1.getPosition());
            vectorFrom1ToVDT.add(relativeSpeed);
            //compute cross product of  vectorFrom2To1 and vectorFrom1ToVDT
             //we have a normal vector to the plane
               Double3Vector crossproduct = vectorFrom2To1.multiply(vectorFrom1ToVDT);
            //L5 is in P at +60 degrees clockwise from body2 and at distance d(body1, body2)
            //to get it now rotate vectorFrom1To2 60degrees clockwise
            //lagrangian is the point you get by applying the new vector onto point 2
            Rotation3D rotation3D = new Rotation3D(new LiteralVector3D(crossproduct.x, crossproduct.y, crossproduct.z), -Math.PI/3);
            Vector3D resultVector = rotation3D.applyTo(new LiteralVector3D(vectorFrom2To1.x, vectorFrom2To1.y, vectorFrom2To1.z));
            result = new Point3d(resultVector.x() + ((Double3Vector)body2.getPosition()).x, resultVector.y() + ((Double3Vector)body2.getPosition()).y, resultVector.z() + ((Double3Vector)body2.getPosition()).z);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     * @param body2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Point3d getLagrangeL5(AstralBody body1, AstralBody body2)
        throws Exception {
        //Two solutions fall out easily; each forms and equilateral triangle with the earth and the sun in the plane of rotation
        Point3d result;

        if (body1.getMass() > body2.getMass()) {
            //body2 revolves around body1
            //velocity vector of body1 and axis from body1 to body2 defines the plane of rotation P
             Double3Vector relativeSpeed = new Double3Vector((Double3Vector)body2.getVelocity());
            relativeSpeed.subtract((Double3Vector)body1.getVelocity());
            Double3Vector vectorFrom1To2 = new Double3Vector((Double3Vector)body2.getPosition());
            vectorFrom1To2.subtract((Double3Vector)body1.getPosition());
            Double3Vector vectorFrom2ToVDT = new Double3Vector((Double3Vector)body2.getPosition());
            vectorFrom2ToVDT.add(relativeSpeed);
            //compute cross product of  vectorFrom1To2  and vectorFrom2ToVDT
             //we have a normal vector to the plane
               Double3Vector crossproduct = vectorFrom1To2.multiply(vectorFrom2ToVDT);
            //L5 is in P at +60 degrees counterclockwise from body1 and at distance d(body1, body2)
            //to get it now rotate vectorFrom1To2 60degrees counterclockwise
            //lagrangian is the point you get by applying the new vector onto point 1
            Rotation3D rotation3D = new Rotation3D(new LiteralVector3D(crossproduct.x, crossproduct.y, crossproduct.z), Math.PI/3);
            Vector3D resultVector = rotation3D.applyTo(new LiteralVector3D(vectorFrom1To2.x, vectorFrom1To2.y, vectorFrom1To2.z));
            result = new Point3d(resultVector.x() + ((Double3Vector)body1.getPosition()).x, resultVector.y() + ((Double3Vector)body1.getPosition()).y, resultVector.z() + ((Double3Vector)body1.getPosition()).z);
        } else {
            //body1 revolves around body2
            //velocity vector of body2 and axis from body2 to body1 defines the plane of rotation P
            //L5 is in P at +60 degrees counterclockwise from body1 and at distance d(body1, body2)
            Double3Vector relativeSpeed = new Double3Vector((Double3Vector)body1.getVelocity());
            relativeSpeed.subtract((Double3Vector)body2.getVelocity());
            Double3Vector vectorFrom2To1 = new Double3Vector((Double3Vector)body1.getPosition());
            vectorFrom2To1.subtract((Double3Vector)body2.getPosition());
            Double3Vector vectorFrom1ToVDT = new Double3Vector((Double3Vector)body1.getPosition());
            vectorFrom1ToVDT.add(relativeSpeed);
            //compute cross product of  vectorFrom2To1 and vectorFrom1ToVDT
             //we have a normal vector to the plane
               Double3Vector crossproduct = vectorFrom2To1.multiply(vectorFrom1ToVDT);
            //L5 is in P at +60 degrees counterclockwise from body2 and at distance d(body1, body2)
            //to get it now rotate vectorFrom1To2 60degrees counterclockwise
            //lagrangian is the point you get by applying the new vector onto point 2
            Rotation3D rotation3D = new Rotation3D(new LiteralVector3D(crossproduct.x, crossproduct.y, crossproduct.z), Math.PI/3);
            Vector3D resultVector = rotation3D.applyTo(new LiteralVector3D(vectorFrom2To1.x, vectorFrom2To1.y, vectorFrom2To1.z));
            result = new Point3d(resultVector.x() + ((Double3Vector)body2.getPosition()).x, resultVector.y() + ((Double3Vector)body2.getPosition()).y, resultVector.z() + ((Double3Vector)body2.getPosition()).z);
        }

        return result;
    }
}
