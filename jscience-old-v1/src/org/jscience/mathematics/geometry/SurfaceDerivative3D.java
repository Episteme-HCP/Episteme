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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

/**
 * �R���� : �Ȗʂ̓���?��l��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ����Ȗ� P �̂���p���??[�^�l (u, v) �ɂ�����
 * �Ȗ�?�̓_ P(u, v) �̒l (�뎟����?��l) d0?A
 * U/V ���ꂼ��̕��̈ꎟ�Γ���?��̒l du/dv?A
 * U/V ���ꂼ��̕��̓񎟕Γ���?��̒l duu/dvv?A
 * UV ���̈ꎟ?�?��Γ���?��̒l duv
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 */

public class SurfaceDerivative3D extends SurfaceDerivative {
    /**
     * �Ȗ�?�̓_ (�뎟����?��l) ?B
     */
    private final Point3D d0;

    /**
     * U ���̈ꎟ�Γ���?��l?B
     */
    private final Vector3D du;

    /**
     * V ���̈ꎟ�Γ���?��l?B
     */
    private final Vector3D dv;

    /**
     * U ���̓񎟕Γ���?��l?B
     */
    private final Vector3D duu;

    /**
     * UV ���̈ꎟ?�?��Γ���?��l?B
     */
    private final Vector3D duv;

    /**
     * V ���̓񎟕Γ���?��l?B
     */
    private final Vector3D dvv;

    /**
     * �e�Γ���?��̒l��^���ăI�u�W�F�N�g��?\�z����
     *
     * @param d0  �Ȗ�?�̓_ (�뎟����?��l)
     * @param du  U ���̈ꎟ�Γ���?��l
     * @param dv  V ���̈ꎟ�Γ���?��l
     * @param duu U ���̓񎟕Γ���?��l
     * @param duv UV ���̈ꎟ?�?��Γ���?��l
     * @param dvv V ���̓񎟕Γ���?��l
     */
    public SurfaceDerivative3D(Point3D d0,
                               Vector3D du,
                               Vector3D dv,
                               Vector3D duu,
                               Vector3D duv,
                               Vector3D dvv) {
        super();
        this.d0 = d0;
        this.du = du;
        this.dv = dv;
        this.duu = duu;
        this.duv = duv;
        this.dvv = dvv;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�̋Ȗ�?�̓_ (�뎟����?��l) ��Ԃ�?B
     *
     * @return �Ȗ�?�̓_ (�뎟����?��l)
     */
    public Point3D d0() {
        return d0;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�� U ���̈ꎟ�Γ���?��l��Ԃ�?B
     *
     * @return U ���̈ꎟ�Γ���?��l
     */
    public Vector3D du() {
        return du;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�� V ���̈ꎟ�Γ���?��l��Ԃ�?B
     *
     * @return V ���̈ꎟ�Γ���?��l
     */
    public Vector3D dv() {
        return dv;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�� U ���̓񎟕Γ���?��l��Ԃ�?B
     *
     * @return U ���̓񎟕Γ���?��l
     */
    public Vector3D duu() {
        return duu;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�� UV ���̈ꎟ?�?��Γ���?��l��Ԃ�?B
     *
     * @return UV ���̈ꎟ?�?��Γ���?��l
     */
    public Vector3D duv() {
        return duv;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�� V ���̓񎟕Γ���?��l��Ԃ�?B
     *
     * @return V ���̓񎟕Γ���?��l
     */
    public Vector3D dvv() {
        return dvv;
    }

    /**
     * ���̓���?��l�I�u�W�F�N�g�̎�ȗ���Ԃ�?B
     *
     * @return ��ȗ��Ǝ���
     */
    public SurfaceCurvature3D principalCurvature() {
        Vector3D UNV;
        double EEE, FFF, GGG;
        double LLL, MMM, NNN;
        double[] AAA = new double[3];
        int icnt;            // number of solution of the 2nd degree polynomial
        double XI, ETA;            // ratio of Xu & Xv
        double[] pcv;            // principle curvature [0]:max [1]:min
        Vector3D[] dir = new Vector3D[2];
        // direction of principle curvature [0]:max [1]:min
        int i;
        double m_eps = MachineEpsilon.DOUBLE;
        DoublePolynomial poly;

        UNV = du.crossProduct(dv).unitized();
        LLL = UNV.x() * duu.x() + UNV.y() * duu.y() + UNV.z() * duu.z();
        MMM = UNV.x() * duv.x() + UNV.y() * duv.y() + UNV.z() * duv.z();
        NNN = UNV.x() * dvv.x() + UNV.y() * dvv.y() + UNV.z() * dvv.z();

        EEE = du.x() * du.x() + du.y() * du.y() + du.z() * du.z();
        FFF = du.x() * dv.x() + du.y() * dv.y() + du.z() * dv.z();
        GGG = dv.x() * dv.x() + dv.y() * dv.y() + dv.z() * dv.z();

        /*
        * principal curvatures	- make & resolve the 2nd degree equation
        */
        AAA[2] = EEE * GGG - FFF * FFF;
        if (Math.abs(AAA[2]) < m_eps) {
            pcv = new double[2];
            pcv[0] = pcv[1] = 0.0;
            dir[0] = dir[1] = Vector3D.zeroVector;
            return new SurfaceCurvature3D(pcv, dir);
        }
        AAA[1] = 2.0 * FFF * MMM - EEE * NNN - GGG * LLL;
        AAA[0] = LLL * NNN - MMM * MMM;

        try {
            poly = new DoublePolynomial(AAA);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
        pcv = GeometryPrivateUtils.getRootsIfQuadric(poly);
        if ((pcv.length != 2) || (CurveCurvature.identical(pcv[0], pcv[1]))) {
            if (pcv.length != 2) pcv = new double[2];
            /* umbilic point */
            pcv[0] = pcv[1] = -AAA[1] / (2.0 * AAA[2]);
            dir[0] = dir[1] = Vector3D.zeroVector;
            return new SurfaceCurvature3D(pcv, dir);
        }

        /*
        * principal directions
        */
        if ((Math.abs(MMM) < m_eps) && (Math.abs(FFF) < m_eps)) {
            /*
            * special case (when the execution reachs here, EEE & GGG are NOT zero)
            */
            double pcv_val = LLL / EEE;

            if (Math.abs(pcv[0] - pcv_val) < Math.abs(pcv[1] - pcv_val)) {
                dir[0] = du;
                dir[1] = dv;
            } else {
                dir[0] = dv;
                dir[1] = du;
            }
            for (i = 0; i < 2; i++)
                dir[i] = dir[i].unitized();

        } else {
            /*
            * general case
            */
            for (i = 0; i < 2; i++) {
                if (Math.abs(MMM - pcv[i] * FFF) > m_eps) {
                    XI = 1.0;
                    ETA = -(LLL - pcv[i] * EEE) / (MMM - pcv[i] * FFF);
                    dir[i] = new LiteralVector3D(XI * du.x() + ETA * dv.x(),
                            XI * du.y() + ETA * dv.y(),
                            XI * du.z() + ETA * dv.z());
                } else {
                    dir[i] = dv;
                }
                dir[i] = dir[i].unitized();
            }
        }

        if (Math.abs(pcv[0]) < Math.abs(pcv[1])) {
            double swap1;
            Vector3D swap2;
            swap1 = pcv[0];
            pcv[0] = pcv[1];
            pcv[1] = swap1;
            swap2 = dir[0];
            dir[0] = dir[1];
            dir[1] = swap2;
        }
        return new SurfaceCurvature3D(pcv, dir);
    }
}

