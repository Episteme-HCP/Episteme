/*
 * �R���� : ��?�E�Ȗʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveBoundedSurface3D.java,v 1.3 2006/03/01 21:15:55 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �R���� : ��?�E�Ȗʂ�\���N���X?B
 * <p/>
 * ��?�E�Ȗʂ�?A����Ȗʂ⻂�?�̔C�ӂ̕�?�ň͂܂ꂽ�ꕔ��������L��Ƃ����L�Ȗʂł���?B
 * ����ꕔ��������L��Ƃ��邱�Ƃ�g���~���O?A
 * �L��Ƃ��镔������?�뫊E�Ƃ���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <ul>
 * <li> �g���~���O�̑�?ۂƂȂ��Ȗ� basisSurface
 * <li> �O���̋��E (�O��) ����?� outerBoundary
 * <li> �Ѥ�̋��E (���?A��) ����?�̃��X�g innerBoundaries
 * </ul>
 * ��ێ?����?B
 * </p>
 * <p/>
 * <a name="CONSTRAINTS">[��?��Ԃ�?S��?�?]</a>
 * </p>
 * <p/>
 * ��Ȗʂ͊J�����`���ł��邩?A�µ����
 * ��Ȗʂ������`����?�?��ɂ�
 * ���E�̂��ꂼ��͂��̃v���C�}���ȗL���Ԃ̓Ѥ�Ɏ�܂BĂ����̂Ƃ���?B
 * </p>
 * <p/>
 * ��Ȗʂ�?A��?�E�Ȗʂł͂Ȃ���̂Ƃ���?B
 * </p>
 * <p/>
 * ��?�E�Ȗʂł�?A �O���т��ꂼ��̓���?i?s���Ɍ�B�?�����L��Ƃ���?B
 * </p>
 * <p/>
 * �e���E��?A�R�����̕����`���̕�?���?� {@link CompositeCurve3D CompositeCurve3D} ��
 * �C���X�^���X�Ƃ��ė^�������̂Ƃ���?B
 * �����?A��?���?��?\?�����
 * �e�Z�O�?���g {@link CompositeCurveSegment3D CompositeCurveSegment3D}
 * �̕��?��?u��?�?� {@link SurfaceCurve3D SurfaceCurve3D} ����?�Ƃ���?v
 * �g������?� {@link TrimmedCurve3D TrimmedCurve3D} �ł����̂Ƃ���?B
 * </p>
 * <p/>
 * �e���E��?A�݂��Ɍ��B���?A���Ȍ�?������肵�Ȃ���̂Ƃ���?B
 * </p>
 * <p/>
 * basisSurface ����� outerBoundary �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * innerBoundaries �̗v�f��?��� 0 �ł�?\��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:55 $
 */
public class CurveBoundedSurface3D extends BoundedSurface3D {
    /**
     * ��Ȗ�?B
     *
     * @serial
     */
    private ParametricSurface3D basisSurface;

    /**
     * �O��?B
     *
     * @serial
     */
    private CompositeCurve3D outerBoundary;

    /**
     * ���̃��X�g?B
     * <p/>
     * �e�v�f�� CompositeCurve3D �ł����̂Ƃ���?B
     * </p>
     *
     * @serial
     */
    private Vector innerBoundaries;

    /**
     * �O��̕�Ȗʂ̃p���??[�^��Ԃł̂Q�����\��
     * <p/>
     * outerBoundary ���?�?�����?B
     * </p>
     *
     * @serial
     */
    private CompositeCurve2D outerBoundary2D;

    /**
     * ���̕�Ȗʂ̃p���??[�^��Ԃł̂Q�����\���̃��X�g
     * <p/>
     * innerBoundaries ���?�?�����?B
     * </p>
     *
     * @serial
     */
    private Vector innerBoundaries2D;

    /**
     * ���E�̂Q������Ԃł̑�?ݔ͈�?B
     *
     * @serial
     */
    private EnclosingBox2D enclosingBox2D;

    /**
     * ���̋�?�E�Ȗʂ̕�Ȗʂ�Ԃ�?B
     *
     * @return ��Ȗ�
     */
    public ParametricSurface3D basisSurface() {
        return this.basisSurface;
    }

    /**
     * ���̋�?�E�Ȗʂ̊O���̋��E (�O��) ��Ԃ�?B
     *
     * @return �O���̋��E (�O��)
     */
    public CompositeCurve3D outerBoundary() {
        return this.outerBoundary;
    }

    /**
     * ���̋�?�E�Ȗʂ̓Ѥ�̋��E (���) ��?���Ԃ�?B
     *
     * @return �Ѥ�̋��E (���) ��?�
     */
    public int numberOfInnerBoundaries() {
        return this.innerBoundaries.size();
    }

    /**
     * ���̋�?�E�Ȗʂ� i �Ԗڂ̓Ѥ�̋��E (���) ��Ԃ�?B
     *
     * @return i �Ԗڂ̓Ѥ�̋��E (���)
     */
    public CompositeCurve3D innerBoundary(int i) {
        return (CompositeCurve3D) this.innerBoundaries.elementAt(i);
    }

    /**
     * ���̋�?�E�Ȗʂ̊O��̂Q�����\����Ԃ�?B
     *
     * @return �O��̂Q�����\��
     */
    public CompositeCurve2D outerBoundary2D() {
        return this.outerBoundary2D;
    }

    /**
     * ���̋�?�E�Ȗʂ� i �Ԗڂ̓Ѥ�̋��E (���) �̂Q�����\����Ԃ�?B
     *
     * @return i �Ԗڂ̓Ѥ�̋��E (���) �̂Q�����\��
     */
    public CompositeCurve2D innerBoundary2D(int i) {
        return (CompositeCurve2D) this.innerBoundaries2D.elementAt(i);
    }

    /**
     * ���̋�?�E�Ȗʂ̋��E�̂Q������Ԃł̑�?ݔ͈͂�Ԃ�?B
     *
     * @return ���E�̂Q������Ԃł̑�?ݔ͈�
     */
    public EnclosingBox2D enclosingBox2D() {
        return this.enclosingBox2D;
    }

    /**
     * �^����ꂽ��?���?�?A���̋�?�E�Ȗʂ̋��E�Ƃ��đÓ����ǂ����𒲂ׂ�?B
     *
     * @param compositeCurve ��?���?�
     * @return �^����ꂽ��?���?�E�Ƃ��đÓ��ł���� null?A�����łȂ���Ζ��_��w�E���镶����
     */
    private static String
    compositeCurveIsValidForBoundary(CompositeCurve3D compositeCurve,
                                     ParametricSurface3D basisSurface) {
        /*
        * �O���ь�?X�̓���?\��
        *
        * CompositeCurve3D		// ���Ă��Ȃ���΂Ȃ�Ȃ�
        *     -> CompositeCurveSegment3D[]
        *       -> TrimmedCurve3D	// SurfaceCurve3D �� BoundedCurve3D ����Ȃ�����
        *	   -> SurfaceCurve3D
        *	     -> ParametricCurve3D
        *	     -> ParametricCurve2D
        */
        if (compositeCurve.isClosed() != true)
            return "A composite curve is not closed";

        for (int i = 0; i < compositeCurve.nSegments(); i++) {
            CompositeCurveSegment3D segment = compositeCurve.segmentAt(i);

            // parent should be TrimmedCurve3D
            TrimmedCurve3D parent;
            try {
                parent = (TrimmedCurve3D) segment.parentCurve();
            } catch (ClassCastException exp) {
                return "The parent of a segment is not TrimmedCurve3D";
            }

            // basis should be SurfaceCurve3D
            SurfaceCurve3D basis;
            try {
                basis = (SurfaceCurve3D) parent.basisCurve();
            } catch (ClassCastException exp) {
                return "The basis of the parent of a segment is not SurfaceCurve3D";
            }

            if (basis.basisSurface() != basisSurface)
                return "The basis surface of a segment is not same as given surface";
        }

        return null;
    }

    /**
     * �R�����̕�?���?�Ƃ��ė^����ꂽ���E�̊e�Z�O�?���g�Ɋ܂܂��Q����?�񂩂�
     * �Q�����̕�?���?��?�?�����?B
     *
     * @param boundary3D ���E
     * @return ���E��\������Q�����̕�?���?�
     */
    private static CompositeCurve2D make2DCurve(CompositeCurve3D boundary3D) {
        int nSegments = boundary3D.nSegments();
        BoundedCurve2D[] segments2D = new BoundedCurve2D[nSegments];
        boolean[] senses2D = new boolean[nSegments];

        for (int i = 0; i < nSegments; i++) {
            CompositeCurveSegment3D segment = boundary3D.segmentAt(i);
            TrimmedCurve3D parent = (TrimmedCurve3D) segment.parentCurve();
            SurfaceCurve3D basis = (SurfaceCurve3D) parent.basisCurve();

            segments2D[i] = new TrimmedCurve2D(basis.curve2d(),
                    parent.tParam1(),
                    parent.tParam2(),
                    parent.senseAgreement());
            senses2D[i] = segment.sameSense();
        }

        return new CompositeCurve2D(segments2D, senses2D);
    }

    /**
     * ���̋�?�E�Ȗʂ̋��E�̕�Ȗʂ̃p���??[�^��Ԃɂ����鑶?ݔ͈͂�?�߂�?B
     * <p/>
     * �?�߂���?ݔ͈͂�?A���̃C���X�^���X��
     * {@ #enclosingBox2D enclosingBox2D} �t�B?[���h
     * ��?ݒ肷��?B
     * </p>
     */
    private void makeEnclosingBox2D() {
        EnclosingBox2D box;
        Point2D[] points =
                new Point2D[(this.innerBoundaries2D.size() + 1) * 2];

        ToleranceForDistance tolD =
                this.getToleranceForDistanceAsObject();

        box = this.outerBoundary2D.toPolyline(tolD).enclosingBox();
        points[0] = box.min();
        points[1] = box.max();

        for (int i = 0; i < this.innerBoundaries2D.size(); i++) {
            CompositeCurve2D inner =
                    (CompositeCurve2D) this.innerBoundaries2D.elementAt(i);
            box = inner.toPolyline(tolD).enclosingBox();
            points[(i + 1) * 2] = box.min();
            points[(i + 1) * 2 + 1] = box.max();
        }

        this.enclosingBox2D = new EnclosingBox2D(points);
    }

    /**
     * ��Ȗ�?A�O��Ɠ��̃��X�g��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basisSurface    ��Ȗ�
     * @param outerBoundary   �O��
     * @param innerBoundaries ���̃��X�g
     * @see InvalidArgumentValueException
     */
    public CurveBoundedSurface3D(ParametricSurface3D basisSurface,
                                 CompositeCurve3D outerBoundary,
                                 Vector innerBoundaries) {
        // ��Ȗʂ̒�`
        if (basisSurface.type() == CURVE_BOUNDED_SURFACE_3D)
            throw new InvalidArgumentValueException("The basis surface is CurveBoundedSurface3D");
        this.basisSurface = basisSurface;

        // �O��̒�`
        String message;

        if ((message = compositeCurveIsValidForBoundary(outerBoundary,
                basisSurface)) != null)
            throw new InvalidArgumentValueException(message);
        this.outerBoundary = outerBoundary;
        this.outerBoundary2D = make2DCurve(outerBoundary);

        // ���̒�`
        this.innerBoundaries = new Vector();
        this.innerBoundaries2D = new Vector();

        if (innerBoundaries != null) {
            for (Enumeration e = innerBoundaries.elements();
                 e.hasMoreElements();) {
                try {
                    CompositeCurve3D inner =
                            (CompositeCurve3D) e.nextElement();
                    if ((message = compositeCurveIsValidForBoundary(inner,
                            basisSurface)) != null)
                        throw new InvalidArgumentValueException(message);
                    this.innerBoundaries.addElement(inner);
                    this.innerBoundaries2D.addElement(make2DCurve(inner));
                } catch (ClassCastException exp) {
                    throw new InvalidArgumentValueException("An inner boundary is not CompositeCurve3D");
                }
            }
        }

        // ���E�̑�?ݔ͈͂�?�߂�
        makeEnclosingBox2D();
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @return U ���̃p���??[�^��`��
     * @see ImproperOperationException
     */
    ParameterDomain getUParameterDomain() {
        throw new ImproperOperationException();    // getUParameterDomain
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @return V ���̃p���??[�^��`��
     * @see ImproperOperationException
     */
    ParameterDomain getVParameterDomain() {
        throw new ImproperOperationException();    // getVParameterDomain
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#CURVE_BOUNDED_SURFACE_3D ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D}
     */
    int type() {
        return CURVE_BOUNDED_SURFACE_3D;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ��Ȗʂ����R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.basisSurface.isFreeform();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam,
                               double vParam) {
        if (this.contains(uParam, vParam) != true)
            throw new ParameterOutOfRange();

        return this.basisSurface.coordinates(uParam, vParam);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �����ł�?ڃx�N�g���Ƃ�?A�p���??[�^ U/V �̊e?X�ɂ��Ă̈ꎟ�Γ���?��ł���?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g��?A
     * ��Ԗڂ̗v�f�ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g����܂�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam,
                                    double vParam) {
        if (this.contains(uParam, vParam) != true)
            throw new ParameterOutOfRange();

        return this.basisSurface.tangentVector(uParam, vParam);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam,
                                          double vParam) {
        if (this.contains(uParam, vParam) != true)
            throw new ParameterOutOfRange();

        return this.basisSurface.evaluation(uParam, vParam);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * �^����ꂽ�_���炱�̋Ȗʂ̕�Ȗʂւ̓��e�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋Ȗʂւ̓��e�_�Ƃ��Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        PointOnSurface3D[] pointsOnBasisSurface =
                this.basisSurface.projectFrom(point);

        if (pointsOnBasisSurface == null)
            return new PointOnSurface3D[0];

        if (pointsOnBasisSurface.length == 0)
            return pointsOnBasisSurface;

        Vector innerPoints = new Vector();
        PointOnSurface3D pos;

        for (int i = 0; i < pointsOnBasisSurface.length; i++) {
            double uParam =
                    basisSurface.uParameterDomain().wrap(pointsOnBasisSurface[i].uParameter());
            double vParam =
                    basisSurface.vParameterDomain().wrap(pointsOnBasisSurface[i].vParameter());
            Point2D point2D = Point2D.of(uParam, vParam);
            if (this.contains(point2D) == true) {
                pos = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                innerPoints.addElement(pos);
            }
        }

        PointOnSurface3D[] results =
                new PointOnSurface3D[innerPoints.size()];
        innerPoints.copyInto(results);
        return results;
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ImproperOperationException
     */
    public Mesh3D toMesh(ToleranceForDistance tol) {
        throw new ImproperOperationException();    // toMesh
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ImproperOperationException
     */
    public Mesh3D toMesh(ParameterSection uPint,
                         ParameterSection vPint,
                         ToleranceForDistance tol) {
        throw new ImproperOperationException();    // toMesh
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see ImproperOperationException
     */
    public BsplineSurface3D toBsplineSurface(ParameterSection uPint,
                                             ParameterSection vPint) {
        throw new ImproperOperationException();    // toBsplineSurface
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate);

        return this.selectInternalIntersections(results, false);
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(ParametricCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate);

        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Conic3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̗L��̈��O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗʂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ���?������?��ɂ��Ă�?A��?� (IntersectionCurve3D) ���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ�?ڂ����?��ɂ��Ă�?A��_ (IntersectionPoint3D) ���Ԃ邱�Ƃ�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results;
        int basisType = this.basisSurface.type();
        if ((basisType == ParametricSurface3D.SURFACE_OF_LINEAR_EXTRUSION_3D) ||
                (basisType == ParametricSurface3D.SURFACE_OF_REVOLUTION_3D)) {
            Point2D[] minMax = this.enclosingBox2D.toArray();
            ParameterSection uPint =
                    new ParameterSection(minMax[0].x(), (minMax[1].x() - minMax[0].x()));
            ParameterSection vPint =
                    new ParameterSection(minMax[0].y(), (minMax[1].y() - minMax[0].y()));
            results = ((SweptSurface3D) this.basisSurface).intersect(uPint, vPint, mate);
        } else {
            results = this.basisSurface.intersect(mate);
        }

        return this.trimIntersectionsWithBoundaries(mate, results, false);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     * @see ImproperOperationException
     */
    public BsplineSurface3D offsetByBsplineSurface(ParameterSection uPint,
                                                   ParameterSection vPint,
                                                   double magni,
                                                   int side,
                                                   ToleranceForDistance tol) {
        throw new ImproperOperationException();    // offsetByBsplineSurface
    }

    /**
     * �t�B���b�g��w��̃p���??[�^��ԂŃg���~���O����?B
     *
     * @param mate           ���̋Ȗ�
     * @param radius         �t�B���b�g�̔��a
     * @param fllt3          �t�B���b�g�̒�?S�̋O?�
     * @param flltT          �t�B���b�g�̂��̋Ȗʂ̕�ȖʂƂ�?�?G�_�̋O?�
     * @param flltM          �t�B���b�g�̑��̋ȖʂƂ�?�?G�_�̋O?�
     * @param isOpenT        �g���~���O����t�B���b�g�� this ?�̕\�����J���Ă��邩�ۂ�
     * @param sectionOfFlltT flltT �̃p���??[�^��`��̋��
     * @param crossBoundary  �g���~���O��Ԃ� (�R�����I�ɕ���) �t�B���b�g�̎���ׂ����ۂ�
     * @param spT            �g���~���O�̊J�n�p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @param ipT            �g���~���O�̑?���p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @return �g���~���O��̃t�B���b�g
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     */
    private FilletObject3D
    trimFillet(BoundedSurface3D mate,
               double radius,
               Polyline3D fllt3,
               Polyline2D flltT,
               Polyline2D flltM,
               boolean isOpenT,
               ParameterSection sectionOfFlltT,
               boolean crossBoundary,
               double spT,
               double ipT) {
        double epT = spT + ipT;

        if ((crossBoundary == true) && (isOpenT == true)) {
            spT = wrapParameterIntoOpenSection(spT, sectionOfFlltT);
            epT = wrapParameterIntoOpenSection(epT, sectionOfFlltT);
        }

        ParameterSection pint = new ParameterSection(spT, (epT - spT));
        ToleranceForDistance tolD = this.getToleranceForDistanceAsObject();

        Polyline3D curve3 = fllt3.toPolyline(pint, tolD);
        Polyline2D curveT = flltT.toPolyline(pint, tolD);
        Polyline2D curveM = flltM.toPolyline(pint, tolD);

        FilletSection3D[] sections = new FilletSection3D[curve3.nPoints()];

        for (int i = 0; i < curve3.nPoints(); i++) {
            PointOnGeometry3D pogT = new PointOnSurface3D(this, curveT.pointAt(i), doCheckDebug);
            PointOnGeometry3D pogM = new PointOnSurface3D(mate, curveM.pointAt(i), doCheckDebug);
            sections[i] = new FilletSection3D(radius, curve3.pointAt(i), pogT, pogM);
        }

        return new FilletObject3D(sections);
    }

    /**
     * ���̋Ȗʂ̕�ȖʂƑ��̋Ȗʂ̃t�B���b�g��?A
     * ���̋Ȗʂ̋��E�Ńg���~���O����?B
     * <p/>
     * �^����ꂽ�t�B���b�g�ɂ���?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��镔��������t�B���b�g�Ƃ��ĕԂ�?B
     * </p>
     *
     * @param mate    ���̋Ȗ�
     * @param fillets ��ȖʂƂ̃t�B���b�g���Z�œ���ꂽ�t�B���b�g
     * @return ���̋Ȗʂ̋��E�Ńg���~���O�����t�B���b�g�̔z��
     * @see #getIntersectionsWithBoundary(CompositeCurve2D,ParametricCurve2D)
     * @see CurveBoundedSurface3D.IntersectionWithBoundaryInfo
     * @see #containsWithWrapping(Point2D)
     * @see #trimFillet(BoundedSurface3D,double,Polyline3D,Polyline2D,Polyline2D,boolean,ParameterSection,boolean,double,double)
     */
    FilletObject3D[]
    trimFilletsWithBoundaries(BoundedSurface3D mate,
                              double radius,
                              FilletObject3D[] fillets) {
        Vector results = new Vector();

        // final �ł���̂�?AiwbiBothEnds ������ comparator ����Q?Ƃ���邽��
        final IntersectionWithBoundaryInfo[] iwbiBothEnds =
                new IntersectionWithBoundaryInfo[2];

        // ���E�Ƃ̌�_��?u�傫��?v���?�?�̃p���??[�^�l�Ŕ��f����I�u�W�F�N�g
        ListSorter.ObjectComparator comparator =
                new ListSorter.ObjectComparator() {
                    public boolean latterIsGreaterThanFormer(java.lang.Object former,
                                                             java.lang.Object latter) {
                        IntersectionWithBoundaryInfo f = (IntersectionWithBoundaryInfo) former;
                        IntersectionWithBoundaryInfo l = (IntersectionWithBoundaryInfo) latter;
                        if (f == l)
                            return false;

                        if ((f == iwbiBothEnds[0]) || (l == iwbiBothEnds[1]))
                            return true;

                        if ((l == iwbiBothEnds[0]) || (f == iwbiBothEnds[1]))
                            return false;

                        return (f.curveParameter < l.curveParameter) ? true : false;
                    }
                };

        /*
        * �t�B���b�g�̂��ꂼ��ɂ���
        */
        for (int i = 0; i < fillets.length; i++) {
            FilletObject3D theFillet = fillets[i];

            /*
            * �Ȗ�?�̃p���??[�^��?�𓾂�
            */
            Polyline3D fllt3 = theFillet.curveOfCenter();
            Polyline2D flltT = theFillet.curveOnSurface1();
            Polyline2D flltM = theFillet.curveOnSurface2();

            boolean isOpen3 = true;    // !!!
            boolean isOpenT = true;    // !!!
            boolean isOpenM = true;    // !!!

            ParameterDomain domainOfFlltT = flltT.parameterDomain();
            ParameterSection sectionOfFlltT = domainOfFlltT.section();

            /*
            * ���E�Ƃ̌�_�𓾂�
            */
            Vector listOfIntersectionsWithBoundaries = new Vector();

            for (int j = -1; j < this.innerBoundaries2D.size(); j++) {
                CompositeCurve2D aBoundary;

                if (j == -1)
                    aBoundary = this.outerBoundary2D;
                else
                    aBoundary =
                            (CompositeCurve2D) this.innerBoundaries2D.elementAt(j);

                IntersectionPoint2D[] intsWithBoundary =
                        getIntersectionsWithBoundary(aBoundary, flltT);

                for (int k = 0; k < intsWithBoundary.length; k++) {
                    IntersectionWithBoundaryInfo iwbi =
                            new IntersectionWithBoundaryInfo(j,
                                    intsWithBoundary[k].pointOnCurve1().parameter(),
                                    intsWithBoundary[k].pointOnCurve2().parameter());
                    listOfIntersectionsWithBoundaries.addElement(iwbi);
                }
            }

            /*
            * ��_��?���?�����
            */
            iwbiBothEnds[0] = null;
            iwbiBothEnds[1] = null;
            boolean addEndPoints = false;

            if (isOpen3 == true) {
                /*
                * ��?�J���Ă���Ȃ��?A���̗��[�뫊E�Ƃ̌�_�ɉB���
                */
                if (flltT.isFinite() == true) {
                    iwbiBothEnds[0] =
                            new IntersectionWithBoundaryInfo(-100, 0.0,
                                    sectionOfFlltT.start());
                    iwbiBothEnds[1] =
                            new IntersectionWithBoundaryInfo(-100, 0.0,
                                    sectionOfFlltT.end());

                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[0]);
                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[1]);
                    addEndPoints = true;
                }
            } else if (listOfIntersectionsWithBoundaries.size() == 1) {
                /*
                * ��?���Ă���?A���E�Ƃ̌�_����Ȃ��?A
                * ���̌�_��Ⴄ��_�Ƃ��ĉB���
                */
                IntersectionWithBoundaryInfo iwbi =
                        (IntersectionWithBoundaryInfo) listOfIntersectionsWithBoundaries.elementAt(0);
                IntersectionWithBoundaryInfo iwbi2 =
                        new IntersectionWithBoundaryInfo(iwbi.boundaryIndex,
                                iwbi.boundaryParameter,
                                iwbi.curveParameter);
                listOfIntersectionsWithBoundaries.addElement(iwbi2);
            }

            /*
            * ���E�Ƃ̌�_���Ȃ�?�?�?A
            * ��?�?�̂����_ (�J�n�_) �����E�̓Ք�ɂ����?A
            * ���̌�?�⻂̂܂�?u?o�͂����?�̃��X�g?v�ɉB���
            */
            if (listOfIntersectionsWithBoundaries.size() == 0) {
                double aParameter = (flltT.isFinite() == true) ?
                        sectionOfFlltT.start() : 0.0;
                if (this.containsWithWrapping(flltT.coordinates(aParameter)) == true) {
                    results.addElement(theFillet);
                }
                continue;
            }

            /*
            * �ȉ�?A���E�Ƃ̌�_������?�?�
            */

            /*
            * ���E�Ƃ̌�_���?�?�̃p���??[�^�l�Ń\?[�g����
            */
            ListSorter.doSorting(listOfIntersectionsWithBoundaries, comparator);

            int nIntervals = listOfIntersectionsWithBoundaries.size() - 1;

            /*
            * �ׂ�?���?u���E�Ƃ̌�_?v�̒��_�����E�̓Ք�ɂ����?A
            * ���̋�Ԃ�c����̂Ƃ���
            */
            Vector listOfTrimmingIntervals = new Vector();
            TrimmingInterval trimmingInterval = null;

            int sIdx;
            int eIdx;
            IntersectionWithBoundaryInfo sIwb;
            IntersectionWithBoundaryInfo eIwb;

            double sp;
            double ip;
            double mp;

            boolean crossBoundary;

            sIdx = 0;
            sIwb = (IntersectionWithBoundaryInfo)
                    listOfIntersectionsWithBoundaries.elementAt(0);

            for (int j = 1; j <= nIntervals; j++) {
                sp = sIwb.curveParameter;
                if ((isOpen3 == true) || (j < nIntervals)) {
                    eIdx = j;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(j);
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;

                    if (((sIwb == iwbiBothEnds[0]) ||
                            (eIwb == iwbiBothEnds[1])) &&
                            (Math.abs(ip) < getToleranceForParameter())) {
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                } else {
                    eIdx = 0;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(0);
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfFlltT.increase();

                    if (Math.abs(ip) < getToleranceForParameter()) {
                        // no cross boundary
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                }

                mp = sp + (ip / 2.0);

                if (addEndPoints == true) {
                    if (j == 1) {
                        mp = sp;
                    } else if (j == nIntervals) {
                        mp = sp + ip;
                    }
                }

                if ((crossBoundary == true) && (isOpenT == true)) {
                    if (mp < sectionOfFlltT.lower())
                        mp += sectionOfFlltT.absIncrease();
                    if (mp > sectionOfFlltT.upper())
                        mp -= sectionOfFlltT.absIncrease();
                }

                if (this.containsWithWrapping(flltT.coordinates(mp)) == true) {
                    if ((trimmingInterval != null) &&
                            (trimmingInterval.eIdx == sIdx)) {
                        trimmingInterval.eIdx = eIdx;
                    } else {
                        trimmingInterval = new TrimmingInterval(sIdx, eIdx);
                        listOfTrimmingIntervals.addElement(trimmingInterval);
                    }
                }

                sIdx = eIdx;
                sIwb = eIwb;
            }

            if ((nIntervals = listOfTrimmingIntervals.size()) > 1) {
                TrimmingInterval head =
                        (TrimmingInterval) listOfTrimmingIntervals.firstElement();
                TrimmingInterval tail =
                        (TrimmingInterval) listOfTrimmingIntervals.lastElement();
                if (head.sIdx == tail.eIdx) {
                    head.sIdx = tail.sIdx;
                    nIntervals--;
                }
            }

            /*
            * ��?��?u�c���Ɣ��f�������?v�Ńg���~���O����
            */
            for (int j = 0; j < nIntervals; j++) {
                trimmingInterval = (TrimmingInterval) listOfTrimmingIntervals.elementAt(j);
                sIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.sIdx);
                eIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.eIdx);

                sp = sIwb.curveParameter;
                if (trimmingInterval.sIdx < trimmingInterval.eIdx) {
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;
                } else {
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfFlltT.increase();
                }

                if (ip < getToleranceForParameter())
                    continue;

                FilletObject3D theTrimmedFillet =
                        trimFillet(mate, radius,
                                fllt3, flltT, flltM, isOpenT, sectionOfFlltT,
                                crossBoundary, sp, ip);

                /*
                * �g���~���O�������ʂ̒[�_��?A���̋Ȗʂ̋��E?�ɂ���Ȃ��?A
                * ���̒[�_����Z�Ń��t�@�C������
                */
                // KOKO
                ;

                /*
                * �g���~���O������Ԃ�?u?o�͂����?�̃��X�g?v�ɉB���
                */
                results.addElement(theTrimmedFillet);
            }

        }

        /*
        * ?u?o�͂���t�B���b�g�̃��X�g?v��Ԃ�
        */
        fillets = new FilletObject3D[results.size()];
        results.copyInto(fillets);
        return fillets;
    }

    /**
     * ���̗L�ȖʂƑ��̗L�Ȗʂ̃t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param side1  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param mate   ���̋Ȗ�
     * @param side2  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    public FilletObject3D[] fillet(int side1, BoundedSurface3D mate, int side2, double radius)
            throws IndefiniteSolutionException {
        ParameterSection uPint =
                new ParameterSection(enclosingBox2D.min().x(),
                        (enclosingBox2D.max().x() - enclosingBox2D.min().x()));
        ParameterSection vPint =
                new ParameterSection(enclosingBox2D.min().y(),
                        (enclosingBox2D.max().y() - enclosingBox2D.min().y()));

        RectangularTrimmedSurface3D thisTrimmedBasis =
                new RectangularTrimmedSurface3D(this.basisSurface, uPint, vPint);

        FilletObject3D[] results = mate.fillet(side2, thisTrimmedBasis, side1, radius);
        for (int i = 0; i < results.length; i++) {
            FilletSection3D[] sections = results[i].sections();
            for (int j = 0; j < sections.length; j++) {
                PointOnSurface3D posT = sections[j].pointOnSurface2();
                PointOnSurface3D pos =
                        new PointOnSurface3D(this,
                                thisTrimmedBasis.toBasisUParameter(posT.uParameter()),
                                thisTrimmedBasis.toBasisVParameter(posT.vParameter()),
                                doCheckDebug);
                sections[j] = new FilletSection3D(radius, sections[j].center(), pos,
                        sections[j].pointOnSurface1());
            }
            results[i] = new FilletObject3D(sections);
        }

        return this.trimFilletsWithBoundaries(mate, radius, results);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
     * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
     * ��B�?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uSect1 ���̋Ȗʂ� U ���̃p���??[�^���
     * @param vSect1 ���̋Ȗʂ� V ���̃p���??[�^���
     * @param side1  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param mate   ���̋Ȗ�
     * @param uSect2 ���̋Ȗʂ� U ���̃p���??[�^���
     * @param vSect2 ���̋Ȗʂ� V ���̃p���??[�^���
     * @param side2  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     * @see ImproperOperationException
     */
    public FilletObject3D[] fillet(ParameterSection uSect1, ParameterSection vSect1, int side1,
                                   ParametricSurface3D mate,
                                   ParameterSection uSect2, ParameterSection vSect2, int side2,
                                   double radius)
            throws IndefiniteSolutionException {
        throw new ImproperOperationException();        // fillet
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ���?ۃ?�\�b�h?B
    * <p>
    * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
    * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
    * ��B�?A���܂̂Ƃ���?A?��
    * ImproperOperationException �̗�O��?�����?B
    * </p>
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    * @see	ImproperOperationException
    */
    public ParametricCurve3D uIsoParametricCurve(double parameter) {
        throw new ImproperOperationException();    // uIsoParametricCurve
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ���?ۃ?�\�b�h?B
    * <p>
    * ��?�E�Ȗʂ̃p���??[�^��`��͈�ʂɋ�`�ł͂Ȃ��̂�?A
    * �Ȗʂ���`�̃p���??[�^��`���?���Ƃ�O��Ƃ������̃?�\�b�h�͗?�_�I�ɈӖ���?���Ȃ�?B
    * ��B�?A���܂̂Ƃ���?A?��
    * ImproperOperationException �̗�O��?�����?B
    * </p>
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    * @see	ImproperOperationException
    */
    public ParametricCurve3D vIsoParametricCurve(double parameter) {
        throw new ImproperOperationException();    // vIsoParametricCurve
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� this ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    protected synchronized ParametricSurface3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        ParametricSurface3D tBasisSurface =
                this.basisSurface.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        CompositeCurve3D tOuterBoundary = (CompositeCurve3D)
                this.outerBoundary.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Vector tInnerBoundaries = new Vector();
        for (Enumeration e = this.innerBoundaries.elements(); e.hasMoreElements();) {
            CompositeCurve3D inner = (CompositeCurve3D) e.nextElement();
            tInnerBoundaries.addElement(inner.transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries));
        }

        return new CurveBoundedSurface3D(tBasisSurface,
                tOuterBoundary,
                tInnerBoundaries);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     */
    protected void output(PrintWriter writer,
                          int indent) {
        String indent_tab = this.makeIndent(indent);

        writer.println(indent_tab + getClassName());

        // basisSurface
        writer.println(indent_tab + "\tbasisSurface");
        this.basisSurface.output(writer, indent + 1);

        // outerBoundary
        writer.println(indent_tab + "\touterBoundary");
        this.outerBoundary.output(writer, indent + 1);

        // innerBoundaries
        if (this.innerBoundaries.size() > 0) {
            writer.println(indent_tab + "\tinnerBoundaries");
            for (Enumeration e = this.innerBoundaries.elements();
                 e.hasMoreElements();) {
                CompositeCurve3D inner =
                        (CompositeCurve3D) e.nextElement();
                inner.output(writer, indent + 1);
            }
        }

        writer.println(indent_tab + "End");
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     *
     * @param ints       ��Ȗʂɑ΂����_
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ��_�����E�ɓѤ�ɂ���� true?A�����łȂ���� false
     * @see #containsWithWrapping(double,double)
     * @see #selectInternalIntersections(IntersectionPoint3D[],boolean)
     */
    private boolean
    intersectionIsInternal(IntersectionPoint3D ints,
                           boolean doExchange) {
        PointOnSurface3D pointOnSurface;
        if (doExchange == false) {
            pointOnSurface = (PointOnSurface3D) ints.pointOnGeometry1();
        } else {
            pointOnSurface = (PointOnSurface3D) ints.pointOnGeometry2();
        }
        return this.containsWithWrapping(pointOnSurface.uParameter(),
                pointOnSurface.vParameter());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_��?A���̋Ȗʂɑ΂����_�ɕϊ�����?B
     *
     * @param ints       ��Ȗʂɑ΂����_
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������_
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionCurve3D,boolean)
     * @see #selectInternalIntersections(IntersectionPoint3D[],boolean)
     */
    private IntersectionPoint3D
    changeTargetOfIntersection(IntersectionPoint3D ints,
                               boolean doExchange) {
        PointOnGeometry3D pog1 = ints.pointOnGeometry1();
        PointOnGeometry3D pog2 = ints.pointOnGeometry2();
        PointOnSurface3D pos;
        double uParam;
        double vParam;
        if (doExchange == false) {
            pos = (PointOnSurface3D) pog1;
            uParam = basisSurface.uParameterDomain().wrap(pos.uParameter());
            vParam = basisSurface.vParameterDomain().wrap(pos.vParameter());
            pog1 = new PointOnSurface3D(this,
                    uParam,
                    vParam,
                    doCheckDebug);
        } else {
            pos = (PointOnSurface3D) pog2;
            uParam = basisSurface.uParameterDomain().wrap(pos.uParameter());
            vParam = basisSurface.vParameterDomain().wrap(pos.vParameter());
            pog2 = new PointOnSurface3D(this,
                    uParam,
                    vParam,
                    doCheckDebug);
        }
        return new IntersectionPoint3D(ints.coordinates(),
                pog1, pog2, doCheckDebug);

    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��?��?A���̋Ȗʂɑ΂����?�ɕϊ�����?B
     *
     * @param ints       ��Ȗʂɑ΂����?�
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������?�
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     */
    private IntersectionCurve3D
    changeTargetOfIntersection(IntersectionCurve3D ints,
                               boolean doExchange) {
        ParametricSurface3D basisSurface1;
        ParametricSurface3D basisSurface2;
        if (doExchange == false) {
            basisSurface1 = this;
            basisSurface2 = ints.basisSurface2();
        } else {
            basisSurface1 = ints.basisSurface1();
            basisSurface2 = this;
        }
        return new IntersectionCurve3D(ints.curve3d(),
                basisSurface1, ints.curve2d1(),
                basisSurface2, ints.curve2d2(),
                ints.masterRepresentation());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_�̓��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ����_��Ԃ�?B
     * <p/>
     * ���ʂƂ��ē������_��?A
     * ���̑�?ۂƃp���??[�^�l�����̋Ȗʂɑ΂����̂ɕϊ�����Ă���?B
     * </p>
     *
     * @param intersections ��ȖʂƂ̌�_���Z�œ���ꂽ��_�̔z��
     * @param doExchange    ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ���̋Ȗʂ̋��E�̓Ѥ�ɂ����_�̔z��
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     */
    IntersectionPoint3D[]
    selectInternalIntersections(IntersectionPoint3D[] intersections,
                                boolean doExchange) {
        Vector innerPoints = new Vector();
        IntersectionPoint3D ints;

        for (int i = 0; i < intersections.length; i++) {
            if (this.intersectionIsInternal(intersections[i], doExchange) == true) {
                ints = this.changeTargetOfIntersection(intersections[i], doExchange);
                innerPoints.addElement(ints);
            }
        }

        IntersectionPoint3D[] results =
                new IntersectionPoint3D[innerPoints.size()];
        innerPoints.copyInto(results);
        return results;
    }

    /**
     * ��Ȗ�?�̋�?�Ƃ��̋Ȗʂ̋��E�Ƃ̌�_��\���Ք�N���X?B
     */
    private class IntersectionWithBoundaryInfo {
        /**
         * ���E�̔�?�?B
         * <p/>
         * (- 1) �ł����?A�O���\��?B
         * </p>
         */
        int boundaryIndex;

        /**
         * ���E?�̃p���??[�^�l
         */
        double boundaryParameter;

        /**
         * ��Ȗ�?�̋�?�?�̃p���??[�^�l
         */
        double curveParameter;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        IntersectionWithBoundaryInfo() {
        }

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param boundaryIndex     ���E�̔�?�
         * @param boundaryParameter ���E?�̃p���??[�^�l
         * @param curveParameter    ��Ȗ�?�̋�?�?�̃p���??[�^�l
         */
        IntersectionWithBoundaryInfo(int boundaryIndex,
                                     double boundaryParameter,
                                     double curveParameter) {
            this.boundaryIndex = boundaryIndex;
            this.boundaryParameter = boundaryParameter;
            this.curveParameter = curveParameter;
        }
    }

    /**
     * ��Ȗ�?�̋�?�뫊E�Ƃ̌�_�Ńg���~���O�����Ԃ�\���Ք�N���X?B
     */
    private class TrimmingInterval {
        /**
         * �J�n�_�̃��X�g��ł̈ʒu?B
         */
        int sIdx;

        /**
         * ?I���_�̃��X�g��ł̈ʒu?B
         */
        int eIdx;

        /**
         * �J�n�_?^?I���_�̃��X�g��ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param sIdx �J�n�_�̃��X�g��ł̈ʒu
         * @param eIdx ?I���_�̃��X�g��ł̈ʒu
         */
        TrimmingInterval(int sIdx,
                         int eIdx) {
            this.sIdx = sIdx;
            this.eIdx = eIdx;
        }
    }

    /**
     * �^����ꂽ��?�􉽓I�ɕ��Ă����?A�ʑ��I�ɂ�����`���ɂ���?B
     *
     * @param theIntersection ��?�
     * @return �ʑ��I�ȊJ��?�Ԃ�􉽓I�ȊJ��?�Ԃ�?��킹����?�
     */
    private IntersectionCurve3D
    makeIntersectionClose(IntersectionCurve3D theIntersection) {
        boolean changed = false;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        if ((curve3d.isClosed() == true) &&
                (curve3d.isPeriodic() != true) &&
                (curve3d.type() == ParametricCurve3D.POLYLINE_3D)) {
            Polyline3D polyline3d = (Polyline3D) curve3d;
            int nPoints = polyline3d.nPoints() - 1;
            Point3D[] points = new Point3D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline3d.pointAt(i);
            curve3d = new Polyline3D(points, true);
            changed = true;
        }

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        if ((curve2d1.isClosed() == true) &&
                (curve2d1.isPeriodic() != true) &&
                ((curve2d1 instanceof Polyline2D) == true)) {
            Polyline2D polyline2d = (Polyline2D) curve2d1;
            int nPoints = polyline2d.nPoints() - 1;
            Point2D[] points = new Point2D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline2d.pointAt(i);
            curve2d1 = new Polyline2D(points, true);
            changed = true;
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        if ((curve2d2.isClosed() == true) &&
                (curve2d2.isPeriodic() != true) &&
                ((curve2d2 instanceof Polyline2D) == true)) {
            Polyline2D polyline2d = (Polyline2D) curve2d2;
            int nPoints = polyline2d.nPoints() - 1;
            Point2D[] points = new Point2D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline2d.pointAt(i);
            curve2d2 = new Polyline2D(points, true);
            changed = true;
        }

        if (changed != true)
            return theIntersection;

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ����_�̂����?�?�ł̃p���??[�^�l��?�߂�?B
     * <p/>
     * point3d �� curve3d ��?�BĂ����̂Ƃ���?A
     * point3d �� curve3d �ł̃p���??[�^�l��Ԃ�?B
     * </p>
     * <p/>
     * �p���??[�^�l�����܂��?�߂��Ȃ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d ��?�
     * @param point3d �_
     * @return �p���??[�^�l
     * @see FatalException
     * @see ParametricCurve3D#hasPolyline()
     * @see BoundedCurve3D#toPolyline(ToleranceForDistance)
     * @see ParametricCurve3D#nearestProjectFrom(Point3D)
     */
    private double getParameterWithCurve3D(ParametricCurve3D curve3d,
                                           Point3D point3d) {
        PointOnCurve3D nearProj3d;
        double nearDist;
        PointOnCurve3D nearestProj3d = null;
        double nearestDist = Double.NaN;

        PointOnCurve3D lowerPoint = null;
        PointOnCurve3D upperPoint = null;
        if (curve3d.isFinite() && curve3d.isOpen()) {
            lowerPoint = new PointOnCurve3D(curve3d, curve3d.parameterDomain().section().lower());
            upperPoint = new PointOnCurve3D(curve3d, curve3d.parameterDomain().section().upper());
        }

        Polyline3D polyline3d = null;
        if (curve3d.hasPolyline() == true) {
            // curve3d �͗L�ł���Ɍ��܂BĂ���
            BoundedCurve3D bounded3d = (BoundedCurve3D) curve3d;
            polyline3d = bounded3d.toPolyline(getToleranceForDistanceAsObject());
        }

        nearProj3d = curve3d.nearestProjectFrom(point3d);
        if (nearProj3d != null) {
            nearDist = nearProj3d.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = nearProj3d;
                nearestDist = nearDist;
            }
        }

        if (lowerPoint != null) {
            nearDist = lowerPoint.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = lowerPoint;
                nearestDist = nearDist;
            }
        }
        if (upperPoint != null) {
            nearDist = upperPoint.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = upperPoint;
                nearestDist = nearDist;
            }
        }
        if (polyline3d != null) {
            for (int i = 0; i < polyline3d.nPoints(); i++) {
                nearDist = polyline3d.pointAt(i).distance(point3d);
                if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                    nearestProj3d = (PointOnCurve3D) polyline3d.pointAt(i);
                    nearestDist = nearDist;
                }
            }
        }

        if (nearestProj3d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection.");

        return nearestProj3d.parameter();
    }

    /**
     * ����_��?u����Ȗ�?�̂����?�?v?�ł̃p���??[�^�l��?�߂�?B
     * <p/>
     * curve2d �� surface3d ?�̋�?� C �̃p���??[�^��Ԃł̂Q�����\���ł����̂Ƃ���?B
     * </p>
     * <p/>
     * point3d �� C ��?�BĂ����̂Ƃ���?A
     * point3d �� C �ł̃p���??[�^�l��Ԃ�?B
     * </p>
     * <p/>
     * �p���??[�^�l�����܂��?�߂��Ȃ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @param surface3d �Ȗ�
     * @param curve2d   ��?�
     * @param point3d   �_
     * @return �p���??[�^�l
     * @see FatalException
     * @see ParametricSurface3D#nearestProjectFrom(Point3D)
     * @see ParametricCurve2D#hasPolyline()
     * @see BoundedCurve2D#toPolyline(ToleranceForDistance)
     * @see ParametricCurve2D#nearestProjectFrom(Point2D)
     */
    private double getParameterWithCurveOnSurface3D(ParametricSurface3D surface3d,
                                                    ParametricCurve2D curve2d,
                                                    Point3D point3d) {
        PointOnSurface3D nearestProj3d = surface3d.nearestProjectFrom(point3d);
        if (nearestProj3d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection in 3d.");
        double[] param3d = nearestProj3d.parameters();

        PointOnCurve2D lowerPoint = null;
        PointOnCurve2D upperPoint = null;
        if (curve2d.isFinite() && curve2d.isOpen()) {
            lowerPoint = new PointOnCurve2D(curve2d, curve2d.parameterDomain().section().lower());
            upperPoint = new PointOnCurve2D(curve2d, curve2d.parameterDomain().section().upper());
        }

        Polyline2D polyline2d = null;
        if (curve2d.hasPolyline() == true) {
            // curve2d �͗L�ł���Ɍ��܂BĂ���
            BoundedCurve2D bounded2d = (BoundedCurve2D) curve2d;
            polyline2d = bounded2d.toPolyline(getToleranceForDistanceAsObject());
        }

        int nU = 1;
        double dU = Double.NaN;
        int nV = 1;
        double dV = Double.NaN;

        if (surface3d.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (surface3d.isUPeriodic() == true) {
                nU = 3;
                dU = surface3d.uParameterDomain().section().increase();
            }

            if (surface3d.isVPeriodic() == true) {
                nV = 3;
                dV = surface3d.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        Point2D point2d;
        PointOnCurve2D nearProj2d;
        double nearDist;
        PointOnCurve2D nearestProj2d = null;
        double nearestDist = Double.NaN;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = param3d[0] - dU;
                    break;
                case 2:
                    pU = param3d[0] + dU;
                    break;
                default:
                    pU = param3d[0];
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = param3d[1] - dV;
                        break;
                    case 2:
                        pV = param3d[1] + dV;
                        break;
                    default:
                        pV = param3d[1];
                        break;
                }

                point2d = Point2D.of(pU, pV);
                nearProj2d = curve2d.nearestProjectFrom(point2d);
                if (nearProj2d != null) {
                    nearDist = nearProj2d.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = nearProj2d;
                        nearestDist = nearDist;
                    }
                }

                if (lowerPoint != null) {
                    nearDist = lowerPoint.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = lowerPoint;
                        nearestDist = nearDist;
                    }
                }

                if (upperPoint != null) {
                    nearDist = upperPoint.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = upperPoint;
                        nearestDist = nearDist;
                    }
                }

                if (polyline2d != null) {
                    for (int i = 0; i < polyline2d.nPoints(); i++) {
                        nearDist = polyline2d.pointAt(i).distance(point2d);
                        if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                            nearestProj2d = (PointOnCurve2D) polyline2d.pointAt(i);
                            nearestDist = nearDist;
                        }
                    }
                }
            }
        }

        if (nearestProj2d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection in 2d.");

        return nearestProj2d.parameter();
    }

    /**
     * ���̋Ȗʂ̋��E��?u��Ȗʂɑ΂���?v��?�̌�_��?�߂�?B
     *
     * @param boundaryCurve ���̋Ȗʂ̋��E
     * @param intsT         ��Ȗʂɑ΂����?�̂Q�����\��
     * @return boundaryCurve �� intsT �̌�_�̔z��
     * @see #trimIntersectionsWithBoundaries(ParametricSurface3D,SurfaceSurfaceInterference3D[],boolean)
     */
    private IntersectionPoint2D[]
    getIntersectionsWithBoundary(CompositeCurve2D boundaryCurve,
                                 ParametricCurve2D intsT) {
        int nU = 1;
        double dU = Double.NaN;

        int nV = 1;
        double dV = Double.NaN;

        if (basisSurface.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (basisSurface.isUPeriodic() == true) {
                nU = 3;
                dU = basisSurface.uParameterDomain().section().increase();
            }

            if (basisSurface.isVPeriodic() == true) {
                nV = 3;
                dV = basisSurface.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        CompositeCurve2D tBoundaryCurve;
        IntersectionPoint2D[] intsWithBoundary;
        Vector intsWithBoundaryList = new Vector();
        int nInts = 0;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = -dU;
                    break;
                case 2:
                    pU = dU;
                    break;
                default:
                    pU = 0.0;
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = -dV;
                        break;
                    case 2:
                        pV = dV;
                        break;
                    default:
                        pV = 0.0;
                        break;
                }

                if ((iU == 0) && (iV == 0)) {
                    tBoundaryCurve = boundaryCurve;
                } else {
                    CartesianTransformationOperator2D transformer =
                            new CartesianTransformationOperator2D(null, null,
                                    Point2D.of(pU, pV),
                                    1.0);
                    tBoundaryCurve = (CompositeCurve2D) boundaryCurve.transformBy(transformer, null);
                }
                intsWithBoundary = tBoundaryCurve.intersect(intsT);

                if (intsWithBoundary.length > 0) {
                    intsWithBoundaryList.addElement(intsWithBoundary);
                    nInts += intsWithBoundary.length;
                }
            }
        }

        IntersectionPoint2D[] result = new IntersectionPoint2D[nInts];
        int iResult = 0;
        for (Enumeration e = intsWithBoundaryList.elements(); e.hasMoreElements();) {
            intsWithBoundary = (IntersectionPoint2D[]) e.nextElement();
            for (int i = 0; i < intsWithBoundary.length; i++)
                result[iResult++] = intsWithBoundary[i];
        }

        return result;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A����J������`��̒��ɓ��悤�Ɋۂ߂�?B
     *
     * @param param   �p���??[�^�l
     * @param section (�J������`���) �p���??[�^���
     */
    private double wrapParameterIntoOpenSection(double param,
                                                ParameterSection section) {
        while (param < section.lower())
            param += section.absIncrease();
        while (param > section.upper())
            param -= section.absIncrease();

        return param;
    }

    /**
     * �^����ꂽ��?��g���~���O����̂�?A?K��ɓ���q����悤�ɂ��ăg���~���O����?B
     * <p/>
     * curve �͊J������?��?A(sp &gt; ep) �ł��邱�Ƃ�z�肵�Ă���?B
     * </p>
     *
     * @param curve (�J����) ��?�
     * @param sp    �g���~���O���Ďc����Ԃ̊J�n�p���??[�^�l
     * @param ep    �g���~���O���Ďc����Ԃ�?I���p���??[�^�l
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     */
    private ParametricCurve2D connectHeadToTail(ParametricCurve2D curve,
                                                double sp,
                                                double ep) {
        ParameterSection section = curve.parameterDomain().section();

        double sp1 = wrapParameterIntoOpenSection(sp, section);
        double ep1 = section.upper();
        double sp2 = section.lower();
        double ep2 = wrapParameterIntoOpenSection(ep, section);

        Point2D lowerCoord = curve.coordinates(section.lower());
        Point2D upperCoord = curve.coordinates(section.upper());
        Vector2D period = upperCoord.subtract(lowerCoord);
        CartesianTransformationOperator2D transformer =
                new CartesianTransformationOperator2D(null, null,
                        period.toPoint2D(),
                        1.0);

        ParametricCurve2D curve1 = curve;
        ParametricCurve2D curve2 = curve.transformBy(transformer, null);

        TrimmedCurve2D tCurve1 = new TrimmedCurve2D(curve1, sp1, ep1, true);
        TrimmedCurve2D tCurve2 = new TrimmedCurve2D(curve2, sp2, ep2, true);
        CompositeCurveSegment2D[] segments =
                new CompositeCurveSegment2D[2];
        segments[0] = new CompositeCurveSegment2D(TransitionCode.CONTINUOUS,
                true, tCurve1);
        segments[1] = new CompositeCurveSegment2D(TransitionCode.DISCONTINUOUS,
                true, tCurve2);
        return new CompositeCurve2D(segments, false);
    }

    /**
     * �|�����C��������?\?����ꂽ��?��w��̃p���??[�^��ԂŃg���~���O����?B
     *
     * @param doExchange      ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @param theIntersection �g���~���O�����?�
     * @param isOpenT         �g���~���O�����?�� this ?�̕\�����J���Ă��邩�ۂ�
     * @param isOpenM         �g���~���O�����?�� mate ?�̕\�����J���Ă��邩�ۂ�
     * @param crossBoundary   �g���~���O��Ԃ� (�R�����I�ɕ���) ��?�̎���ׂ����ۂ�
     * @param spT             �g���~���O�̊J�n�p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @param ipT             �g���~���O�̑?���p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @return �g���~���O��̌�?�
     * @see #trimIntersection(boolean,IntersectionCurve3D,ParametricCurve2D,ParameterSection,boolean,boolean,boolean,double,double)
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     * @see #connectHeadToTail(ParametricCurve2D,double,double)
     */
    private IntersectionCurve3D
    trimIntersection2(boolean doExchange,
                      IntersectionCurve3D theIntersection,
                      boolean isOpenT,
                      boolean isOpenM,
                      boolean crossBoundary,
                      double spT,
                      double ipT) {
        double epT = spT + ipT;

        ParameterSection section;
        double sp;
        double ep;
        boolean isOpen;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        sp = spT;
        ep = epT;
        curve3d = new TrimmedCurve3D(curve3d, sp, ep, true);

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        sp = spT;
        ep = epT;
        isOpen = (doExchange == false) ? isOpenT : isOpenM;
        if ((crossBoundary == true) && (isOpen == true)) {
            section = curve2d1.parameterDomain().section();
            sp = wrapParameterIntoOpenSection(sp, section);
            ep = wrapParameterIntoOpenSection(ep, section);
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d1 = new TrimmedCurve2D(curve2d1, sp, ep, true);
        } else {
            curve2d1 = connectHeadToTail(curve2d1, sp, ep);
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        sp = spT;
        ep = epT;
        isOpen = (doExchange == false) ? isOpenM : isOpenT;
        if ((crossBoundary == true) && (isOpen == true)) {
            section = curve2d2.parameterDomain().section();
            sp = wrapParameterIntoOpenSection(sp, section);
            ep = wrapParameterIntoOpenSection(ep, section);
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d2 = new TrimmedCurve2D(curve2d2, sp, ep, true);
        } else {
            curve2d2 = connectHeadToTail(curve2d2, sp, ep);
        }

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ��?��w��̃p���??[�^��ԂŃg���~���O����?B
     *
     * @param doExchange      ��?��basisSurface1,2��귂��邩�ǂ���
     * @param theIntersection �g���~���O�����?�
     * @param intsT           �g���~���O�����?�� this ?�̕\��
     * @param sectionOfIntsT  intsT �̃p���??[�^��`��̋��
     * @param isOpenT         �g���~���O����t�B���b�g�� this ?�̕\�����J���Ă��邩�ۂ�
     * @param isOpenM         �g���~���O����t�B���b�g�� mate ?�̕\�����J���Ă��邩�ۂ�
     * @param crossBoundary   �g���~���O��Ԃ� (�R�����I�ɕ���) ��?�̎���ׂ����ۂ�
     * @param spT             �g���~���O�̊J�n�p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @param ipT             �g���~���O�̑?���p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @return �g���~���O��̌�?�
     * @see ParametricCurve3D#isComposedOfOnlyPolylines()
     * @see ParametricCurve2D#isComposedOfOnlyPolylines()
     * @see #trimIntersection2(boolean,IntersectionCurve3D,boolean,boolean,boolean,double,double)
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     * @see #getParameterWithCurve3D(ParametricCurve3D,Point3D)
     * @see #getParameterWithCurveOnSurface3D(ParametricSurface3D,ParametricCurve2D,Point3D)
     * @see #connectHeadToTail(ParametricCurve2D,double,double)
     */
    private IntersectionCurve3D
    trimIntersection(boolean doExchange,
                     IntersectionCurve3D theIntersection,
                     ParametricCurve2D intsT,
                     ParameterSection sectionOfIntsT,
                     boolean isOpenT,
                     boolean isOpenM,
                     boolean crossBoundary,
                     double spT,
                     double ipT) {
        if ((theIntersection.curve3d().isComposedOfOnlyPolylines() == true) &&
                (theIntersection.curve2d1().isComposedOfOnlyPolylines() == true) &&
                (theIntersection.curve2d2().isComposedOfOnlyPolylines() == true)) {
            ParameterSection section3d =
                    theIntersection.curve3d().parameterDomain().section();
            ParameterSection section2d1 =
                    theIntersection.curve2d1().parameterDomain().section();
            ParameterSection section2d2 =
                    theIntersection.curve2d2().parameterDomain().section();

            if ((section3d.identical(section2d1) == true) &&
                    (section3d.identical(section2d2) == true))
                return trimIntersection2(doExchange,
                        theIntersection,
                        isOpenT, isOpenM,
                        crossBoundary, spT, ipT);
        }

        double epT = spT + ipT;

        if ((crossBoundary == true) && (isOpenT == true)) {
            spT = wrapParameterIntoOpenSection(spT, sectionOfIntsT);
            epT = wrapParameterIntoOpenSection(epT, sectionOfIntsT);
        }

        Point2D spnt2d = intsT.coordinates(spT);
        Point2D epnt2d = intsT.coordinates(epT);
        Point3D spnt3d = this.basisSurface.coordinates(spnt2d.x(), spnt2d.y());
        Point3D epnt3d = this.basisSurface.coordinates(epnt2d.x(), epnt2d.y());

        double sp;
        double ep;
        boolean isOpen;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        sp = getParameterWithCurve3D(curve3d, spnt3d);
        ep = getParameterWithCurve3D(curve3d, epnt3d);
        curve3d = new TrimmedCurve3D(curve3d, sp, ep, true);

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        if (doExchange == false) {
            sp = spT;
            ep = epT;
            isOpen = isOpenT;
        } else {
            sp = getParameterWithCurveOnSurface3D(theIntersection.basisSurface1(),
                    curve2d1, spnt3d);
            ep = getParameterWithCurveOnSurface3D(theIntersection.basisSurface1(),
                    curve2d1, epnt3d);
            isOpen = isOpenM;
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d1 = new TrimmedCurve2D(curve2d1, sp, ep, true);
        } else {
            curve2d1 = connectHeadToTail(curve2d1, sp, ep);
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        if (doExchange == false) {
            sp = getParameterWithCurveOnSurface3D(theIntersection.basisSurface2(),
                    curve2d2, spnt3d);
            ep = getParameterWithCurveOnSurface3D(theIntersection.basisSurface2(),
                    curve2d2, epnt3d);
            isOpen = isOpenM;
        } else {
            sp = spT;
            ep = epT;
            isOpen = isOpenT;
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d2 = new TrimmedCurve2D(curve2d2, sp, ep, true);
        } else {
            curve2d2 = connectHeadToTail(curve2d2, sp, ep);
        }

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ���̋Ȗʂ̕�Ȗʂ̃p���??[�^���?�̋�?��
     * ��Ȗʂ̃v���C�}���ȗL���ԓ�Ɉړ�����?B
     *
     * @param curve ��Ȗʂ̃p���??[�^���?�̋�?�
     * @return ��Ȗʂ̃v���C�}���ȗL���ԓ�ɂ����?�
     */
    private ParametricCurve2D
    moveIntoPrimarySections(ParametricCurve2D curve) {
        double lower = curve.parameterDomain().section().lower();
        double upper = curve.parameterDomain().section().upper();
        double middle = (lower + upper) / 2.0;
        Point2D lowerPoint = curve.coordinates(lower);
        Point2D upperPoint = curve.coordinates(upper);
        Point2D middlePoint = curve.coordinates(middle);

        int nU = 1;
        double dU = Double.NaN;

        int nV = 1;
        double dV = Double.NaN;

        if (basisSurface.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (basisSurface.isUPeriodic() == true) {
                nU = 3;
                dU = basisSurface.uParameterDomain().section().increase();
            }

            if (basisSurface.isVPeriodic() == true) {
                nV = 3;
                dV = basisSurface.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        CartesianTransformationOperator2D transformer;
        Point2D tLowerPoint;
        Point2D tUpperPoint;
        Point2D tMiddlePoint;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = -dU;
                    break;
                case 2:
                    pU = dU;
                    break;
                default:
                    pU = 0.0;
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = -dV;
                        break;
                    case 2:
                        pV = dV;
                        break;
                    default:
                        pV = 0.0;
                        break;
                }

                if ((iU == 0) && (iV == 0)) {
                    transformer = null;
                    tLowerPoint = lowerPoint;
                    tUpperPoint = upperPoint;
                    tMiddlePoint = middlePoint;
                } else {
                    transformer =
                            new CartesianTransformationOperator2D(null, null,
                                    Point2D.of(pU, pV),
                                    1.0);
                    tLowerPoint = lowerPoint.transformBy(transformer, null);
                    tUpperPoint = upperPoint.transformBy(transformer, null);
                    tMiddlePoint = middlePoint.transformBy(transformer, null);
                }

                if ((contains(tLowerPoint) == true) &&
                        (contains(tUpperPoint) == true) &&
                        (contains(tMiddlePoint) == true)) {
                    if (transformer == null)
                        return curve;
                    else
                        return curve.transformBy(transformer, null);
                }
            }
        }

        return null;
    }

    /**
     * ���̋Ȗʂ̕�Ȗʂɑ΂���p���??[�^�l��?��?��
     * ���̋Ȗʂɑ΂���p���??[�^�l��?��?�ɕϊ�����?B
     *
     * @param ints       ���̋Ȗʂ̕�Ȗʂɑ΂���p���??[�^�l��?��?�
     * @param doExchange ��?�� pointOnSurface1/2 ��귂��邩
     * @return ���̋Ȗʂɑ΂���p���??[�^�l��?��?�
     * @see #moveIntoPrimarySections(ParametricCurve2D)
     */
    private IntersectionCurve3D
    changeParameterSpaceOfIntersection(IntersectionCurve3D ints,
                                       boolean doExchange) {
        ParametricCurve2D curve2d1 = ints.curve2d1();
        ParametricCurve2D curve2d2 = ints.curve2d2();
        if (doExchange == false) {
            curve2d1 = moveIntoPrimarySections(curve2d1);
        } else {
            curve2d2 = moveIntoPrimarySections(curve2d2);
        }
        return new IntersectionCurve3D(ints.curve3d(),
                ints.basisSurface1(), curve2d1,
                ints.basisSurface2(), curve2d2,
                ints.masterRepresentation());
    }

    /**
     * ���̋Ȗʂ̕�ȖʂƑ��̋Ȗʂ̌�?��?A
     * ���̋Ȗʂ̋��E�Ńg���~���O����?B
     * <p/>
     * �^����ꂽ��?�ɂ���?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��镔���������?�Ƃ��ĕԂ�?B
     * </p>
     *
     * @param mate          ���̋Ȗ�
     * @param intersections ��ȖʂƂ̌�?�Z�œ���ꂽ��?�̔z��
     * @param doExchange    ��?�� pointOnSurface1/2 ��귂��邩
     * @return ���̋Ȗʂ̋��E�Ńg���~���O������?�̔z��
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionCurve3D,boolean)
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #makeIntersectionClose(IntersectionCurve3D)
     * @see #getIntersectionsWithBoundary(CompositeCurve2D,ParametricCurve2D)
     * @see CurveBoundedSurface3D.IntersectionWithBoundaryInfo
     * @see #containsWithWrapping(Point2D)
     * @see #trimIntersection(boolean,IntersectionCurve3D,ParametricCurve2D,ParameterSection,boolean,boolean,boolean,double,double)
     * @see #changeParameterSpaceOfIntersection(IntersectionCurve3D,boolean)
     */
    SurfaceSurfaceInterference3D[]
    trimIntersectionsWithBoundaries(ParametricSurface3D mate,
                                    SurfaceSurfaceInterference3D[] intersections,
                                    boolean doExchange) {
        // ��?̑�?ۂ��Ȗʂ��玩?g�ɕ�?X����
        for (int i = 0; i < intersections.length; i++) {
            if (intersections[i].isIntersectionPoint() == true) {
                // ��_
                IntersectionPoint3D ints =
                        intersections[i].toIntersectionPoint();
                intersections[i] = changeTargetOfIntersection(ints, doExchange);
            } else {
                // ��?�
                IntersectionCurve3D ints =
                        intersections[i].toIntersectionCurve();
                intersections[i] = changeTargetOfIntersection(ints, doExchange);
            }
        }

        Vector results = new Vector();

        // final �ł���̂�?AiwbiBothEnds ������ comparator ����Q?Ƃ���邽��
        final IntersectionWithBoundaryInfo[] iwbiBothEnds =
                new IntersectionWithBoundaryInfo[2];

        // ���E�Ƃ̌�_��?u�傫��?v���?�?�̃p���??[�^�l�Ŕ��f����I�u�W�F�N�g
        ListSorter.ObjectComparator comparator =
                new ListSorter.ObjectComparator() {
                    public boolean latterIsGreaterThanFormer(java.lang.Object former,
                                                             java.lang.Object latter) {
                        IntersectionWithBoundaryInfo f = (IntersectionWithBoundaryInfo) former;
                        IntersectionWithBoundaryInfo l = (IntersectionWithBoundaryInfo) latter;
                        if (f == l)
                            return false;

                        if ((f == iwbiBothEnds[0]) || (l == iwbiBothEnds[1]))
                            return true;

                        if ((l == iwbiBothEnds[0]) || (f == iwbiBothEnds[1]))
                            return false;

                        return (f.curveParameter < l.curveParameter) ? true : false;
                    }
                };

        /*
        * ��?̂��ꂼ��ɂ���
        */
        for (int i = 0; i < intersections.length; i++) {
            /*
            * ��_��?�?�?A���ꂪ���E�̓Ѥ�ɂ���Ȃ�� results �ɉB���
            */
            if (intersections[i].isIntersectionPoint() == true) {
                IntersectionPoint3D ints =
                        intersections[i].toIntersectionPoint();
                if (this.intersectionIsInternal(ints, doExchange) == true)
                    results.addElement(ints);
                continue;
            }

            /*
            * �ȉ�?A��?��?�?�
            */
            IntersectionCurve3D theIntersection =
                    intersections[i].toIntersectionCurve();
            theIntersection = makeIntersectionClose(theIntersection);

            /*
            * �Ȗ�?�̃p���??[�^��?�𓾂�
            */
            ParametricCurve2D intsT;
            ParametricCurve2D intsM;

            if (doExchange == false) {
                intsT = theIntersection.curve2d1();
                intsM = theIntersection.curve2d2();
            } else {
                intsT = theIntersection.curve2d2();
                intsM = theIntersection.curve2d1();
            }

            boolean isOpen3 = theIntersection.curve3d().isOpen();
            boolean isOpenT = intsT.isOpen();
            boolean isOpenM = intsM.isOpen();

            ParameterDomain domainOfIntsT = intsT.parameterDomain();
            ParameterSection sectionOfIntsT = domainOfIntsT.section();

            /*
            * ���E�Ƃ̌�_�𓾂�
            */
            Vector listOfIntersectionsWithBoundaries = new Vector();

            for (int j = -1; j < this.innerBoundaries2D.size(); j++) {
                CompositeCurve2D aBoundary;

                if (j == -1)
                    aBoundary = this.outerBoundary2D;
                else
                    aBoundary =
                            (CompositeCurve2D) this.innerBoundaries2D.elementAt(j);

                IntersectionPoint2D[] intsWithBoundary =
                        getIntersectionsWithBoundary(aBoundary, intsT);

                for (int k = 0; k < intsWithBoundary.length; k++) {
                    IntersectionWithBoundaryInfo iwbi =
                            new IntersectionWithBoundaryInfo(j,
                                    intsWithBoundary[k].pointOnCurve1().parameter(),
                                    intsWithBoundary[k].pointOnCurve2().parameter());
                    listOfIntersectionsWithBoundaries.addElement(iwbi);
                }
            }

            /*
            * ��_��?���?�����
            */
            iwbiBothEnds[0] = null;
            iwbiBothEnds[1] = null;
            boolean addEndPoints = false;

            if (isOpen3 == true) {
                /*
                * ��?�J���Ă���Ȃ��?A���̗��[�뫊E�Ƃ̌�_�ɉB���
                */
                if (intsT.isFinite() == true) {
                    iwbiBothEnds[0] =
                            new IntersectionWithBoundaryInfo(-100, 0.0,
                                    sectionOfIntsT.start());
                    iwbiBothEnds[1] =
                            new IntersectionWithBoundaryInfo(-100, 0.0,
                                    sectionOfIntsT.end());

                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[0]);
                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[1]);
                    addEndPoints = true;
                }
            } else if (listOfIntersectionsWithBoundaries.size() == 1) {
                /*
                * ��?���Ă���?A���E�Ƃ̌�_����Ȃ��?A
                * ���̌�_��Ⴄ��_�Ƃ��ĉB���
                */
                IntersectionWithBoundaryInfo iwbi =
                        (IntersectionWithBoundaryInfo) listOfIntersectionsWithBoundaries.elementAt(0);
                IntersectionWithBoundaryInfo iwbi2 =
                        new IntersectionWithBoundaryInfo(iwbi.boundaryIndex,
                                iwbi.boundaryParameter,
                                iwbi.curveParameter);
                listOfIntersectionsWithBoundaries.addElement(iwbi2);
            }

            /*
            * ���E�Ƃ̌�_���Ȃ�?�?�?A
            * ��?�?�̂����_ (�J�n�_) �����E�̓Ք�ɂ����?A
            * ���̌�?�⻂̂܂�?u?o�͂����?�̃��X�g?v�ɉB���
            */
            if (listOfIntersectionsWithBoundaries.size() == 0) {
                double aParameter = (intsT.isFinite() == true) ?
                        sectionOfIntsT.start() : 0.0;
                if (this.containsWithWrapping(intsT.coordinates(aParameter)) == true) {
                    results.addElement(changeParameterSpaceOfIntersection(theIntersection, doExchange));
                }
                continue;
            }

            /*
            * �ȉ�?A���E�Ƃ̌�_������?�?�
            */

            /*
            * ���E�Ƃ̌�_���?�?�̃p���??[�^�l�Ń\?[�g����
            */
            ListSorter.doSorting(listOfIntersectionsWithBoundaries, comparator);

            int nIntervals;
            if (isOpen3 == true) {
                nIntervals = listOfIntersectionsWithBoundaries.size() - 1;
            } else {
                nIntervals = listOfIntersectionsWithBoundaries.size();
            }

            /*
            * �ׂ�?���?u���E�Ƃ̌�_?v�̒��_�����E�̓Ք�ɂ����?A
            * ���̋�Ԃ�c����̂Ƃ���
            */
            Vector listOfTrimmingIntervals = new Vector();
            TrimmingInterval trimmingInterval = null;

            int sIdx;
            int eIdx;
            IntersectionWithBoundaryInfo sIwb;
            IntersectionWithBoundaryInfo eIwb;

            double sp;
            double ip;
            double mp;

            boolean crossBoundary;

            sIdx = 0;
            sIwb = (IntersectionWithBoundaryInfo)
                    listOfIntersectionsWithBoundaries.elementAt(0);

            for (int j = 1; j <= nIntervals; j++) {
                sp = sIwb.curveParameter;
                if ((isOpen3 == true) || (j < nIntervals)) {
                    eIdx = j;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(j);
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;

                    if (((sIwb == iwbiBothEnds[0]) ||
                            (eIwb == iwbiBothEnds[1])) &&
                            (Math.abs(ip) < getToleranceForParameter())) {
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                } else {
                    eIdx = 0;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(0);
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfIntsT.increase();

                    if (Math.abs(ip) < getToleranceForParameter()) {
                        // no cross boundary
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                }

                mp = sp + (ip / 2.0);

                if (addEndPoints == true) {
                    if (j == 1) {
                        mp = sp;
                    } else if (j == nIntervals) {
                        mp = sp + ip;
                    }
                }

                if ((crossBoundary == true) && (isOpenT == true)) {
                    if (mp < sectionOfIntsT.lower())
                        mp += sectionOfIntsT.absIncrease();
                    if (mp > sectionOfIntsT.upper())
                        mp -= sectionOfIntsT.absIncrease();
                }

                if (this.containsWithWrapping(intsT.coordinates(mp)) == true) {
                    if ((trimmingInterval != null) &&
                            (trimmingInterval.eIdx == sIdx)) {
                        trimmingInterval.eIdx = eIdx;
                    } else {
                        trimmingInterval = new TrimmingInterval(sIdx, eIdx);
                        listOfTrimmingIntervals.addElement(trimmingInterval);
                    }
                }

                sIdx = eIdx;
                sIwb = eIwb;
            }

            if ((nIntervals = listOfTrimmingIntervals.size()) > 1) {
                TrimmingInterval head =
                        (TrimmingInterval) listOfTrimmingIntervals.firstElement();
                TrimmingInterval tail =
                        (TrimmingInterval) listOfTrimmingIntervals.lastElement();
                if (head.sIdx == tail.eIdx) {
                    head.sIdx = tail.sIdx;
                    nIntervals--;
                }
            }

            /*
            * ��?��?u�c���Ɣ��f�������?v�Ńg���~���O����
            */
            for (int j = 0; j < nIntervals; j++) {
                trimmingInterval = (TrimmingInterval) listOfTrimmingIntervals.elementAt(j);
                sIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.sIdx);
                eIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.eIdx);

                sp = sIwb.curveParameter;
                if (trimmingInterval.sIdx < trimmingInterval.eIdx) {
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;
                } else {
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfIntsT.increase();
                }

                if (ip < getToleranceForParameter())
                    continue;

                IntersectionCurve3D theTrimmedIntersection =
                        trimIntersection(doExchange,
                                theIntersection, intsT, sectionOfIntsT,
                                isOpenT, isOpenM,
                                crossBoundary, sp, ip);

                /*
                * �g���~���O�������ʂ̒[�_��?A���̋Ȗʂ̋��E?�ɂ���Ȃ��?A
                * ���̒[�_����Z�Ń��t�@�C������
                */
                // KOKO
                ;

                /*
                * �g���~���O������Ԃ�?u?o�͂����?�̃��X�g?v�ɉB���
                */
                results.addElement(changeParameterSpaceOfIntersection(theTrimmedIntersection,
                        doExchange));
            }
        }

        /*
        * ?u?o�͂����?�̃��X�g?v��Ԃ�
        */
        intersections = new SurfaceSurfaceInterference3D[results.size()];
        results.copyInto(intersections);
        return intersections;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̗L��̈�Ńg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����_�Q��Ԃ�?B
     * <p/>
     * ?��?���ʂƂ��ē�����_�Q�͈�ʂ�?A�ʑ��I�ɂ�􉽓I�ɂ�?A�i�q?�ł͂Ȃ�?B
     * </p>
     * <p/>
     * scalingFactor ��?A��͗p�ł͂Ȃ�?A?o�͗p�̈�?��ł���?B
     * scalingFactor �ɂ�?A�v�f?� 2 �̔z���^����?B
     * scalingFactor[0] �ɂ� U ����?k�ڔ{��?A
     * scalingFactor[1] �ɂ� V ����?k�ڔ{�����Ԃ�?B
     * �����̒l�͉��炩��?�Βl�ł͂Ȃ�?A
     * �p���??[�^��?i�ޑ��x T �ɑ΂���?A
     * U/V �����ɂ��Ď��?�ŋȖ�?�̓_��?i�ޑ��x Pu/Pv ��\�����Βl�ł���?B
     * �܂�?A�p���??[�^�� T ����?i�ނ�?A
     * ���?�ł̋Ȗ�?�̓_�� U ���ł� Pu (scalingFactor[0])?A
     * V ���ł� Pv (scalingFactor[1]) ����?i�ނ��Ƃ�\���Ă���?B
     * T �̑傫���͖�������Ȃ��̂�?A���̒l��Q?Ƃ���?ۂɂ�?A
     * scalingFactor[0] �� scalingFactor[1] �̔䂾����p����ׂ��ł���?B
     * �Ȃ�?A�����̒l�͂����܂ł�ڈł���?A�����ȑ��x����̂ł͂Ȃ�?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ� Vector �Ɋ܂܂��e�v�f��
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ���҂ł���?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�͎�����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     * @see UnsupportedOperationException
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        throw new UnsupportedOperationException();    // toNonStructuredPoints
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �p���??[�^�l�����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean contains(double uParam,
                            double vParam) {
        return this.contains(Point2D.of(uParam, vParam));
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * point2D �����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param point2D (u, v) �p���??[�^�l
     * @return point2D �����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean contains(Point2D point2D) {
        try {
            if (point2D.isIn(this.outerBoundary2D) != true) {
                return false;
            }

            for (Enumeration e = innerBoundaries2D.elements(); e.hasMoreElements();) {
                if (point2D.isInsideOf((ParametricCurve2D) e.nextElement()) == true) {
                    return false;
                }
            }
        } catch (OpenCurveException exp) {
            ; // �N���蓾�Ȃ��͂�
        }

        return true;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l���Ȗʂ̃p���??[�^��`��� wrap �������?A
     * {@link #contains(double,double) contains(double, double)}
     * ��Ă�?o��?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �p���??[�^�l�����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    private boolean containsWithWrapping(double uParam,
                                         double vParam) {
        return this.contains(basisSurface.uParameterDomain().wrap(uParam),
                basisSurface.vParameterDomain().wrap(vParam));
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l���Ȗʂ̃p���??[�^��`��� wrap �������?A
     * {@link #contains(double,double) contains(double, double)}
     * ��Ă�?o��?B
     * </p>
     *
     * @param point2D (u, v) �p���??[�^�l
     * @return point2D �����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    private boolean containsWithWrapping(Point2D point2D) {
        return this.contains(basisSurface.uParameterDomain().wrap(point2D.x()),
                basisSurface.vParameterDomain().wrap(point2D.y()));
    }

    /**
     * ���E��?�񂪂�����?�_��\���Ք�N���X?B
     *
     * @see #toSetOfTriangles(double,double)
     */
    private class SurfacePointWithBoundaryInfo extends PointOnSurface3D {
        /**
         * ���E�̔�?�?B
         * <p/>
         * (- 1) �ł����?A�O���\��?B
         * </p>
         *
         * @serial
         */
        int boundaryNumber;

        /**
         * ���E?�ł̂��̓_�̔�?�?B
         *
         * @serial
         */
        int pointNumber;

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param basisSurface   �_��?�BĂ���Ȗ�
         * @param uParam         �Ȗ�?�ł̓_�� U ���̃p���??[�^�l
         * @param vParam         �Ȗ�?�ł̓_�� V ���̃p���??[�^�l
         * @param boundaryNumber ���E�̔�?�
         * @param pointNumber    ���E?�ł̂��̓_�̔�?�
         */
        SurfacePointWithBoundaryInfo(ParametricSurface3D basisSurface,
                                     double uParam,
                                     double vParam,
                                     int boundaryNumber,
                                     int pointNumber) {
            super(basisSurface, uParam, vParam);
            this.boundaryNumber = boundaryNumber;
            this.pointNumber = pointNumber;
        }

        /**
         * ����Ɨד��m���ۂ�?A��Ԃ�?B
         *
         * @param mate ����
         * @return mate ���ׂȂ� true?A�����łȂ���� false
         */
        boolean isNeighborOf(SurfacePointWithBoundaryInfo mate,
                             Polyline2D outerPolyline2D,
                             Vector innerPolylines2D) {
            if (this.boundaryNumber != mate.boundaryNumber)
                return false;

            if ((this.pointNumber == (mate.pointNumber - 1)) ||
                    (this.pointNumber == (mate.pointNumber + 1)))
                return true;

            if ((this.pointNumber != 0) &&
                    (mate.pointNumber != 0))
                return false;

            Polyline2D boundary =
                    (this.boundaryNumber == -1)
                            ? outerPolyline2D
                            : (Polyline2D) innerPolylines2D.elementAt(this.boundaryNumber);

            if (this.pointNumber == 0) {
                if (mate.pointNumber == (boundary.nPoints() - 2))
                    return true;
            } else {
                if (this.pointNumber == (boundary.nPoints() - 2))
                    return true;
            }

            return false;
        }
    }

    /**
     * �Ȗ�?�̓񒸓_�싂ԂQ������?��?��?B
     *
     * @param v1 ���_ 1
     * @param v2 ���_ 2
     * @return �񒸓_�싂�?�
     * @see #findWrongEdge(SetOfTriangles3D.Vertex,SetOfTriangles3D.Vertex,SetOfTriangles3D.Face[])
     * @see #findWrongEdge2(SetOfTriangles3D.Vertex,SetOfTriangles3D.Vertex,SetOfTriangles3D.Edge,SetOfTriangles3D.Face[])
     */
    private BoundedLine2D makeBoundedLine(SetOfTriangles3D.Vertex v1,
                                          SetOfTriangles3D.Vertex v2) {
        SurfacePointWithBoundaryInfo c1 =
                (SurfacePointWithBoundaryInfo) v1.getCoordinates();
        SurfacePointWithBoundaryInfo c2 =
                (SurfacePointWithBoundaryInfo) v2.getCoordinates();

        return new BoundedLine2D(Point2D.of(c1.parameters()),
                Point2D.of(c2.parameters()));
    }

    /**
     * ?��?��?�?��?s�Ȃ��Ă��Ȃ����Ƃ���O�̓Ք�N���X?B
     *
     * @see #findWrongEdge(SetOfTriangles3D.Vertex,SetOfTriangles3D.Vertex,SetOfTriangles3D.Face[])
     * @see #findWrongEdge2(SetOfTriangles3D.Vertex,SetOfTriangles3D.Vertex,SetOfTriangles3D.Edge,SetOfTriangles3D.Face[])
     * @see #flipDiagonalsIn(SetOfTriangles3D,Polyline2D,Vector)
     */
    private class SomeThingWrong extends Exception {
        /**
         * ?־��^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        public SomeThingWrong() {
            super();
        }
    }

    /**
     * �񒸓_�̊Ԃ�?u�܂�����?v�쩂���?B
     * <p/>
     * �܂����ӂ��Ȃ����?Anull ��Ԃ�?B
     * </p>
     *
     * @param v1     ���_ 1
     * @param v2     ���_ 2
     * @param v1Face ���_ 1 ��?u�܂�����?v�̊Ԃɂ���� (?o�͗p?A�v�f?� 1)
     * @return �񒸓_�̊Ԃ�?u�܂�����?v
     * @throws SomeThingWrong ������
     * @see #flipDiagonalsIn(SetOfTriangles3D,Polyline2D,Vector)
     */
    private SetOfTriangles3D.Edge findWrongEdge(SetOfTriangles3D.Vertex v1,
                                                SetOfTriangles3D.Vertex v2,
                                                SetOfTriangles3D.Face[] v1Face)
            throws SomeThingWrong {
        SetOfTriangles3D.Edge wrongEdge = null;

        SetOfTriangles3D.Face[] faces1 = v1.getFacesInCCW();
        SetOfTriangles3D.Face[] faces2 = v2.getFacesInCCW();

        /*
        * v1 �� v2 �����ʂ̖ʂ�?�Ȃ��?A?u�܂�����?v�͂Ȃ�
        */
        for (int i1 = 0; i1 < faces1.length; i1++) {
            if (faces1[i1] == null) continue;

            for (int i2 = 0; i2 < faces2.length; i2++) {
                if (faces2[i2] == null) continue;

                if (faces1[i1].isIdentWith(faces2[i2]) == true)
                    return null;
            }
        }

        /*
        * v1 �̖ʂ� v2 �̖ʂ����ʂ̕ӂ�?�Ȃ��?A���̕ӂ�?u�܂�����?v�ł���
        */
        BoundedLine2D Abln = makeBoundedLine(v1, v2);
        SetOfTriangles3D.Edge[] edges1;
        SetOfTriangles3D.Edge[] edges2;
        SetOfTriangles3D.Vertex[] mates;

        for (int i1 = 0; i1 < faces1.length; i1++) {
            if (faces1[i1] == null) continue;

            edges1 = faces1[i1].getEdgesInCCW();

            for (int i2 = 0; i2 < faces2.length; i2++) {
                if (faces2[i2] == null) continue;

                edges2 = faces2[i2].getEdgesInCCW();

                for (int j1 = 0; j1 < 3; j1++) {
                    for (int j2 = 0; j2 < 3; j2++) {
                        if (edges1[j1].isIdentWith(edges2[j2]) != true)
                            continue;

                        // edges1[j1] �� edges2[j2] �͓�����

                        mates = edges1[j1].getVerticesOfStartEnd();

                        if ((v1.isIdentWith(mates[0]) == true) ||
                                (v1.isIdentWith(mates[1]) == true) ||
                                (v2.isIdentWith(mates[0]) == true) ||
                                (v2.isIdentWith(mates[1]) == true))
                            continue;

                        try {
                            if (Abln.intersect1(makeBoundedLine(mates[0], mates[1])) != null) {
                                if (v1Face != null)
                                    v1Face[0] = faces1[i1];
                                return edges1[j1];
                            }
                        } catch (IndefiniteSolutionException e) {
                            ;
                        }
                    }
                }
            }
        }

        /*
        * v1 �̖ʂ̕ӂ�?Av1 ���� v2 �ւ̒�?�ƌ����̂������?A
        * ���̕ӂ�?u�܂�����?v�ł���
        */
        for (int i1 = 0; i1 < faces1.length; i1++) {
            if (faces1[i1] == null) continue;

            edges1 = faces1[i1].getEdgesInCCW();

            for (int j1 = 0; j1 < 3; j1++) {
                mates = edges1[j1].getVerticesOfStartEnd();

                if ((v1.isIdentWith(mates[0]) == true) ||
                        (v1.isIdentWith(mates[1]) == true))
                    continue;

                try {
                    if (Abln.intersect1(makeBoundedLine(mates[0], mates[1])) != null) {
                        if (v1Face != null)
                            v1Face[0] = faces1[i1];
                        return edges1[j1];
                    }
                } catch (IndefiniteSolutionException e) {
                    ;
                }
            }
        }

        throw new SomeThingWrong();
    }

    /**
     * �񒸓_�̊Ԃ�?u���̂܂�����?v�쩂���?B
     *
     * @param v1        ���_ 1
     * @param v2        ���_ 2
     * @param wrongEdge �܂�����
     * @param nearFace  ���_ 1 ��?u�܂�����?v�̊Ԃɂ���� (��?o�͗p?A�v�f?� 1)
     * @return �񒸓_�̊Ԃ�?u���̂܂�����?v
     * @throws SomeThingWrong ������
     * @see #flipDiagonalsIn(SetOfTriangles3D,Polyline2D,Vector)
     */
    private SetOfTriangles3D.Edge findWrongEdge2(SetOfTriangles3D.Vertex v1,
                                                 SetOfTriangles3D.Vertex v2,
                                                 SetOfTriangles3D.Edge wrongEdge,
                                                 SetOfTriangles3D.Face[] nearFace)
            throws SomeThingWrong {
        /*
        *              |\                        v1 : I : vertex 1
        *   v1         | \                       v2 : I : vertex 2
        *   +          |  \          v2          e1 : I : previous wrong_edge
        *         f1   |   \         +           e2 : O : new wrong_edge
        *              |    \                    f1 : I : previous near_face
        *              | f2  \                   f2 : O : new near_face
        *              |      \
        *             e1       e2
        */

        SetOfTriangles3D.Face[] wrongEdgeFaces = wrongEdge.getFacesOfLeftRight();
        SetOfTriangles3D.Face farFace =
                (nearFace[0].isIdentWith(wrongEdgeFaces[0]) == true)
                        ? wrongEdgeFaces[1] : wrongEdgeFaces[0];

        if (farFace == null)
            throw new SomeThingWrong();

        BoundedLine2D Abln = makeBoundedLine(v1, v2);

        SetOfTriangles3D.Edge[] farFaceEdges = farFace.getEdgesInCCW();

        for (int i = 0; i < 3; i++) {
            if (wrongEdge.isIdentWith(farFaceEdges[i]) == true)
                continue;

            SetOfTriangles3D.Vertex[] mates =
                    farFaceEdges[i].getVerticesOfStartEnd();

            if ((v2.isIdentWith(mates[0]) == true) ||
                    (v2.isIdentWith(mates[1]) == true))
                throw new SomeThingWrong();

            try {
                if (Abln.intersect1(makeBoundedLine(mates[0], mates[1])) != null) {
                    nearFace[0] = farFace;
                    return farFaceEdges[i];
                }
            } catch (IndefiniteSolutionException e) {
                ;
            }
        }

        throw new SomeThingWrong();
    }

    /**
     * �ӂ����ւ����邩�ۂ�?A��Ԃ�?B
     *
     * @param edge ��
     * @return ���ւ������ true?A�����łȂ���� false
     * @see #flipDiagonalsIn(SetOfTriangles3D,Polyline2D,Vector)
     */
    private boolean edgeCanBeFlipped(SetOfTriangles3D.Edge edge) {
        SetOfTriangles3D.Vertex[] vrtcs = edge.getVerticesOfStartEnd();
        SetOfTriangles3D.Face[] faces = edge.getFacesOfLeftRight();
        SurfacePointWithBoundaryInfo c;
        Point2D[] crds = new Point2D[3];

        for (int i = 0; i < 2; i++) {
            if (faces[i] == null)    // outer face
                return false;

            SetOfTriangles3D.Vertex[] faceVrtx = faces[i].getVerticesInCCW();

            for (int j = 0; j < 3; j++) {
                if ((faceVrtx[j].isIdentWith(vrtcs[0]) != true) &&
                        (faceVrtx[j].isIdentWith(vrtcs[1]) != true)) {
                    c = (SurfacePointWithBoundaryInfo) faceVrtx[j].getCoordinates();
                    crds[i] = Point2D.of(c.parameters());
                    break;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            c = (SurfacePointWithBoundaryInfo) vrtcs[i].getCoordinates();
            crds[2] = Point2D.of(c.parameters());
            if (Point2D.collinear(crds, 0, 2) != null)
                return false;
        }

        return true;
    }

    /**
     * ?��?�𑱂��ĂΞ�Ԃ̖��ʂ��ۂ�?A��Ԃ�?B
     *
     * @param wrongEdge      �܂�����
     * @param vertexPairList ?w?u�܂�����?v�̗��[�̒��_?x�̃��X�g
     * @return ���Ԃ̖��ʂȂ�� true?A�����łȂ���� false
     * @see #flipDiagonalsIn(SetOfTriangles3D,Polyline2D,Vector)
     */
    private boolean wasteOfTime(SetOfTriangles3D.Edge wrongEdge,
                                Vector vertexPairList) {
        SetOfTriangles3D.Vertex[] crntVertexPair = wrongEdge.getVerticesOfStartEnd();

        int nWastes = 0;
        for (Enumeration e = vertexPairList.elements(); e.hasMoreElements();) {
            SetOfTriangles3D.Vertex[] vertexPair =
                    (SetOfTriangles3D.Vertex[]) e.nextElement();
            if (((vertexPair[0].isIdentWith(crntVertexPair[0]) == true) &&
                    (vertexPair[1].isIdentWith(crntVertexPair[1]) == true)) ||
                    ((vertexPair[0].isIdentWith(crntVertexPair[1]) == true) &&
                            (vertexPair[1].isIdentWith(crntVertexPair[0]) == true)))
                nWastes++;
        }
        if (nWastes >= 20)
            return true;    // the boundary must self-intersect

        vertexPairList.addElement(crntVertexPair);
        return false;
    }

    /**
     * ���E�̌q�����?C?�����?B
     *
     * @param triangles �O�p�`��?W?�
     * @return ���܂�?C?��ł����� true?A?C?��ł��Ȃ���� false
     * @see #toSetOfTriangles(double,double)
     */
    private boolean flipDiagonalsIn(SetOfTriangles3D triangles,
                                    Polyline2D outerPolyline2D,
                                    Vector innerPolylines2D) {
        boolean success = true;
        Vector wrongConnectedVertexList = new Vector();

        /*
        * ���܂��q���BĂ��Ȃ����_�쩂���
        */
        for (Enumeration e = triangles.vertexElements(); e.hasMoreElements();) {
            SetOfTriangles3D.Vertex vrtx =
                    (SetOfTriangles3D.Vertex) e.nextElement();
            SurfacePointWithBoundaryInfo vrtxCoord =
                    (SurfacePointWithBoundaryInfo) vrtx.getCoordinates();

            if (vrtxCoord.boundaryNumber == (-2))    // ���E�̓Ѥ�̓_
                continue;

            int connectedNumber = 0;
            SetOfTriangles3D.Edge[] edges = vrtx.getEdgesInCCW();
            for (int i = 0; i < edges.length; i++) {
                SetOfTriangles3D.Vertex[] vrtcs = edges[i].getVerticesOfStartEnd();
                SetOfTriangles3D.Vertex mate
                        = (vrtx.isIdentWith(vrtcs[0]) != true) ? vrtcs[0] : vrtcs[1];
                SurfacePointWithBoundaryInfo mateCoord =
                        (SurfacePointWithBoundaryInfo) mate.getCoordinates();

                if (vrtxCoord.isNeighborOf(mateCoord, outerPolyline2D, innerPolylines2D) == true)
                    if (++connectedNumber == 2)
                        break;
            }

            for (int i = connectedNumber; i < 2; i++) {
                wrongConnectedVertexList.addElement(vrtx);
                // Debug
                // System.err.println("vrtx " + vrtxCoord.boundaryNumber +
                // 		   ", " + vrtxCoord.pointNumber);
            }
        }

        // Debug
        // System.err.println(wrongConnectedVertexList.size());

        /*
        * �܂����q�����?C?�
        */
        while (wrongConnectedVertexList.isEmpty() != true) {
            SetOfTriangles3D.Vertex v1 =
                    (SetOfTriangles3D.Vertex) wrongConnectedVertexList.elementAt(0);
            SurfacePointWithBoundaryInfo c1 =
                    (SurfacePointWithBoundaryInfo) v1.getCoordinates();

            SetOfTriangles3D.Vertex v2 = null;
            SurfacePointWithBoundaryInfo c2;
            int i;

            for (i = 1; i < wrongConnectedVertexList.size(); i++) {
                v2 = (SetOfTriangles3D.Vertex) wrongConnectedVertexList.elementAt(i);
                c2 = (SurfacePointWithBoundaryInfo) v2.getCoordinates();
                if (c1.isNeighborOf(c2, outerPolyline2D, innerPolylines2D) == true) {
                    try {
                        if (findWrongEdge(v1, v2, null) != null)
                            break;
                    } catch (SomeThingWrong e) {
                        success = false;
                    }
                }
            }

            if (i < wrongConnectedVertexList.size()) {
                /*
                * v1 ���� v2 �Ɍ��?A�܂����q�����?C?�
                */
                SetOfTriangles3D.Vertex vA = v1;
                SetOfTriangles3D.Vertex vB = v2;
                boolean reverse = false;
                int nFails = 0;
                Vector vertexPairList = new Vector();
                SetOfTriangles3D.Face[] nearFace = new SetOfTriangles3D.Face[1];

                while (true) {
                    SetOfTriangles3D.Edge wrongEdge;
                    try {
                        wrongEdge = findWrongEdge(vA, vB, nearFace);
                    } catch (SomeThingWrong e) {
                        wrongEdge = null;
                    }
                    if (wrongEdge == null)
                        break;

                    if (wasteOfTime(wrongEdge, vertexPairList) == true)
                        break;

                    if ((edgeCanBeFlipped(wrongEdge) == true) &&
                            (wrongEdge.flipDiagonal() != null)) {
                        nFails = 0;
                    } else {
                        while (true) {
                            try {
                                wrongEdge = findWrongEdge2(vA, vB, wrongEdge, nearFace);
                            } catch (SomeThingWrong e) {
                                wrongEdge = null;
                            }
                            if (wrongEdge == null)
                                break;

                            if ((edgeCanBeFlipped(wrongEdge) == true) &&
                                    (wrongEdge.flipDiagonal() != null)) {
                                nFails = 0;
                                break;
                            }
                        }

                        if (wrongEdge == null) {
                            if (++nFails == 2)
                                break;
                        }

                        if (reverse == false) {
                            vA = v2;
                            vB = v1;
                            reverse = true;
                        } else {
                            vA = v1;
                            vB = v2;
                            reverse = false;
                        }
                    }
                }

                wrongConnectedVertexList.removeElementAt(i);
            }

            wrongConnectedVertexList.removeElementAt(0);
        }

        return success;
    }

    /**
     * ���̋ȖʑS�̂�?A�^����ꂽ?��x�ŕ��ʋߎ�����O�p�`��?W?���Ԃ�?B
     *
     * @param tol4S �Ȗʕ�����?��x
     * @param tol4B ���E������?��x (�Q�����p���??[�^���?�ł̒l)
     * @return �O�p�`��?W?�
     */
    public SetOfTriangles3D toSetOfTriangles(double tol4S,
                                             double tol4B) {
        // �_�Q : PointOnSurface3D ��?W?�

        // ��Ȗʂ̋��E��܂ޗ̈��_�Q�ɕϊ�����
        ParameterSection uPint =
                new ParameterSection(enclosingBox2D.min().x(),
                        (enclosingBox2D.max().x() - enclosingBox2D.min().x()));
        ParameterSection vPint =
                new ParameterSection(enclosingBox2D.min().y(),
                        (enclosingBox2D.max().y() - enclosingBox2D.min().y()));

        double[] scalingFactor = new double[2];
        Vector pointsOnBasisSurface =
                this.basisSurface.toNonStructuredPoints(uPint, vPint, tol4S, scalingFactor);

        /*
        // Debug start
        for (Enumeration e = pointsOnBasisSurface.elements();
             e.hasMoreElements();) {
            PointOnSurface3D ppp = (PointOnSurface3D)e.nextElement();
            System.out.println(ppp.uParameter() + ", " + ppp.vParameter());
        }
        System.out.println("---");
        // Debug end
        */

        // �O���_�Q�ɕϊ�����
        ToleranceForDistance tol4Boundary = new ToleranceForDistance(tol4B);
        Polyline2D outerPolyline2D = this.outerBoundary2D.toPolyline(tol4Boundary);

        // ����_�Q�ɕϊ�����
        Vector innerPolyline2D = new Vector();
        for (Enumeration e = this.innerBoundaries2D.elements();
             e.hasMoreElements();) {
            CompositeCurve2D inner = (CompositeCurve2D) e.nextElement();
            innerPolyline2D.addElement(inner.toPolyline(tol4Boundary));
        }

        // ���E�̊O���̓_��?�?�����
        Vector innerPointsOnBasisSurface = new Vector();
        int jjj = 0;
        for (Enumeration e = pointsOnBasisSurface.elements();
             e.hasMoreElements();) {
            PointOnSurface3D point = (PointOnSurface3D) e.nextElement();
            Point2D point2D = Point2D.of(point.parameters());

            if (this.contains(point2D) == true) {
                SurfacePointWithBoundaryInfo innerPoint =
                        new SurfacePointWithBoundaryInfo(this.basisSurface,
                                point.uParameter(),
                                point.vParameter(), -2, -1);
                innerPointsOnBasisSurface.addElement(innerPoint);
            }
        }

        /*
        // Debug start
        for (Enumeration e = innerPointsOnBasisSurface.elements();
             e.hasMoreElements();) {
            PointOnSurface3D ppp = (PointOnSurface3D)e.nextElement();
            System.out.println(ppp.uParameter() + ", " + ppp.vParameter());
        }
        System.out.println("---");
        // Debug end
        */

        // �_�Q��}?[�W����
        pointsOnBasisSurface = innerPointsOnBasisSurface;
        for (int i = 0; i < (outerPolyline2D.nPoints() - 1); i++) {
            Point2D point2D = outerPolyline2D.pointAt(i);
            PointOnSurface3D point3D =
                    new SurfacePointWithBoundaryInfo(this.basisSurface,
                            point2D.x(), point2D.y(),
                            -1, i);
            pointsOnBasisSurface.addElement(point3D);
        }

        for (int j = 0; j < innerPolyline2D.size(); j++) {
            Polyline2D inner = (Polyline2D) innerPolyline2D.elementAt(j);
            for (int i = 0; i < (inner.nPoints() - 1); i++) {
                Point2D point2D = inner.pointAt(i);
                PointOnSurface3D point3D =
                        new SurfacePointWithBoundaryInfo(this.basisSurface,
                                point2D.x(), point2D.y(),
                                j, i);
                pointsOnBasisSurface.addElement(point3D);
            }
        }

        /*
        // Debug start
        for (Enumeration e = pointsOnBasisSurface.elements();
             e.hasMoreElements();) {
            PointOnSurface3D ppp = (PointOnSurface3D)e.nextElement();
            System.out.println(ppp.uParameter() + ", " + ppp.vParameter());
        }
        System.out.println("---");
        // Debug end
        */

        double uScale;
        double vScale;
        if (scalingFactor[0] < scalingFactor[1]) {
            uScale = scalingFactor[0] / scalingFactor[1];
            vScale = 1.0;
        } else {
            uScale = 1.0;
            vScale = scalingFactor[1] / scalingFactor[0];
        }

        SetOfTriangles3D triangles = null;
        int maxTrys = 5;
        int nTrys = 1;
        double radiusScale = VoronoiDiagram2D.radiusScaleDefault;

        while (true) {
            // �_�Q����O�p�`��?W?���?�?�����
            triangles =
                    new SetOfTriangles3D(pointsOnBasisSurface.elements(),
                            uScale, vScale, radiusScale);

            // ���E�̌q�����?C?�����?AOK �Ȃ� break
            if ((flipDiagonalsIn(triangles,
                    outerPolyline2D,
                    innerPolyline2D) == true) ||
                    (nTrys >= maxTrys))
                break;

            nTrys++;
            radiusScale *= 10.0;
        }

        // �s�v�ȎO�p�`��?�?�
        SurfacePointWithBoundaryInfo[] vpnts = new SurfacePointWithBoundaryInfo[3];
        Point2D[] vcrds2 = new Point2D[3];
        int[] order = new int[3];

        for (Enumeration e = triangles.faceElements(); e.hasMoreElements();) {
            SetOfTriangles3D.Face face =
                    (SetOfTriangles3D.Face) e.nextElement();
            SetOfTriangles3D.Vertex[] vrtcs = face.getVerticesInCCW();

            vpnts[0] = (SurfacePointWithBoundaryInfo) vrtcs[0].getCoordinates();

            int boundaryNumber = vpnts[0].boundaryNumber;
            if (boundaryNumber < -1)    // ���E�̓Ѥ�ɂ���_
                continue;

            vpnts[1] = (SurfacePointWithBoundaryInfo) vrtcs[1].getCoordinates();
            if (vpnts[1].boundaryNumber != boundaryNumber)    // ���E���Ⴄ
                continue;

            vpnts[2] = (SurfacePointWithBoundaryInfo) vrtcs[2].getCoordinates();
            if (vpnts[2].boundaryNumber != boundaryNumber)    // ���E���Ⴄ
                continue;

            for (int i = 0; i < 3; i++)
                vcrds2[i] = Point2D.of(vpnts[i].parameters());

            if (vpnts[0].pointNumber < vpnts[1].pointNumber) {
                order[0] = 0;
                order[1] = 1;
            } else {
                order[0] = 1;
                order[1] = 0;
            }

            if (vpnts[order[1]].pointNumber < vpnts[2].pointNumber) {
                order[2] = 2;
            } else {
                order[2] = order[1];
                if (vpnts[order[0]].pointNumber < vpnts[2].pointNumber) {
                    order[1] = 2;
                } else {
                    order[1] = order[0];
                    order[0] = 2;
                }
            }

            Vector2D edge0 = vcrds2[order[1]].subtract(vcrds2[order[0]]);
            Vector2D edge1 = vcrds2[order[2]].subtract(vcrds2[order[1]]);
            if (edge0.zOfCrossProduct(edge1) > 0.0)    // ���E�̓Ѥ
                continue;

            // face �͋��E�̊O���ɂ���
            face.setKilled(true);
        }

        return triangles;
    }

    // Main Programs for Debugging
    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String[] args) {
        Point3D[][] controlPoints = new Point3D[4][4];

        controlPoints[0][0] = Point3D.of(3.00, 5.00, 8.00);
        controlPoints[1][0] = Point3D.of(7.82963, 6.2941, 13.00);
        controlPoints[2][0] = Point3D.of(12.6593, 7.58819, 3.00);
        controlPoints[3][0] = Point3D.of(17.4889, 8.88229, 8.00);

        controlPoints[0][1] = Point3D.of(1.7059, 9.82963, 18.00);
        controlPoints[1][1] = Point3D.of(6.53553, 11.1237, 23.00);
        controlPoints[2][1] = Point3D.of(11.3652, 12.4178, 13.00);
        controlPoints[3][1] = Point3D.of(16.1948, 13.7119, 18.00);

        controlPoints[0][2] = Point3D.of(0.41181, 14.6593, 18.00);
        controlPoints[1][2] = Point3D.of(5.24144, 15.9534, 23.00);
        controlPoints[2][2] = Point3D.of(10.0711, 17.2474, 13.00);
        controlPoints[3][2] = Point3D.of(14.9007, 18.5415, 18.00);

        controlPoints[0][3] = Point3D.of(-0.882286, 19.4889, 8.00);
        controlPoints[1][3] = Point3D.of(3.94734, 20.783, 13.00);
        controlPoints[2][3] = Point3D.of(8.77697, 22.0771, 3.00);
        controlPoints[3][3] = Point3D.of(13.6066, 23.3712, 8.00);

        PureBezierSurface3D basisSurface =
                new PureBezierSurface3D(controlPoints);

        Circle2D circle2D = new Circle2D(Point2D.of(0.5, 0.5), 0.3);
        SurfaceCurve3D surfaceCurve3D =
                new SurfaceCurve3D(null, basisSurface, circle2D,
                        PreferredSurfaceCurveRepresentation.CURVE_2D_1);

        TrimmedCurve3D trimmedCurve3D =
                new TrimmedCurve3D(surfaceCurve3D,
                        circle2D.parameterDomain().section());

        CompositeCurveSegment3D[] segments = new CompositeCurveSegment3D[1];

        segments[0] = new CompositeCurveSegment3D(TransitionCode.CONTINUOUS,
                true,
                trimmedCurve3D);

        CompositeCurve3D outerBoundary =
                new CompositeCurve3D(segments, true);

        Vector innerBoundaries = new Vector();

        CurveBoundedSurface3D surface =
                new CurveBoundedSurface3D(basisSurface,
                        outerBoundary,
                        innerBoundaries);

        // surface.basisSurface().output(System.out);

        // SetOfTriangles3D stri = surface.toSetOfTriangles(0.1, 0.01);
        SetOfTriangles3D stri = surface.toSetOfTriangles(0.01, 0.001);

        int i = 0;
        for (Enumeration e = stri.edgeElements(); e.hasMoreElements();) {
            SetOfTriangles3D.Edge edge =
                    (SetOfTriangles3D.Edge) e.nextElement();
            SetOfTriangles3D.Vertex[] vrtcs = edge.getVerticesOfStartEnd();
            Point3D pnt0 = vrtcs[0].getCoordinates();
            Point3D pnt1 = vrtcs[1].getCoordinates();

            System.out.println("Line3D	lin" + i);
            System.out.println("\tpnt\t" + pnt0.x() + " " + pnt0.y() + " " + pnt0.z());
            System.out.println("\tpnt\t" + pnt1.x() + " " + pnt1.y() + " " + pnt1.z());
            System.out.println("End");
            i++;
        }
    }
}

// end of file
