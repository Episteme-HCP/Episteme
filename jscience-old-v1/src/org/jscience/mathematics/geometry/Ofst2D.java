/*
 * �񎟌��̃I�t�Z�b�g��v�Z����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Ofst2D.java,v 1.3 2007-10-23 18:19:43 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * �񎟌��̃I�t�Z�b�g��v�Z����N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:43 $
 */
class Ofst2D {
    /** �I�t�Z�b�g�����?� */
    private ParametricCurve2D curve;

    /** �I�t�Z�b�g����̈� */
    private ParameterSection pint;

    /** �I�t�Z�b�g������ */
    private int side;

    /** �I�t�Z�b�g�̒��� */
    private double magni;

    /** ��?� */
    private ToleranceForDistance tolerance;

    /*
     * �T���v�����O��?�
     */

    //    private Point2D[] sampling_points;
    /** DOCUMENT ME! */
    private Polyline2D poly;

    /** DOCUMENT ME! */
    private double[] sampling_parameters;

/**
     * �R���X�g���N�^
     *
     * @param curve     �I�t�Z�b�g�����?�
     * @param pint      �I�t�Z�b�g������
     * @param magni     �I�t�Z�b�g��
     * @param side      �I�t�Z�b�g�̕��
     * @param tolerance ��?�
     */
    Ofst2D(ParametricCurve2D curve, ParameterSection pint, double magni,
        int side, ToleranceForDistance tolerance) {
        curve.checkValidity(pint);
        this.curve = curve;
        this.pint = pint;
        this.side = side;
        this.magni = magni;
        this.tolerance = tolerance;
    }

    /**
     * �I�t�Z�b�g��?��?�߂�
     *
     * @return �I�t�Z�b�g��?�
     */
    private BsplineCurve2D offset_bsc() {
        // �T���v�����O��?���v�Z����
        set_sample_variables();

        // �I�t�Z�b�g��?���v�Z����
        Point2D[] offset_points = set_offset_points();
        double[] offset_parameters = set_offset_parameters(offset_points);

        // ��ԋߎ�����
        BsplineCurve2D obsc = approx_bsc(offset_points, offset_parameters);

        return obsc;
    }

    /**
     * �T���v�����O��?���v�Z����
     */
    private void set_sample_variables() {
        double[] parameter;
        int i;
        int j;

        // �|�����C���ɕϊ�����
        poly = curve.toPolyline(pint, tolerance);

        // �T���v�����O�p���?�^��?�߂�
        sampling_parameters = new double[poly.nPoints()];

        for (i = 0; i < poly.nPoints(); i++)
            sampling_parameters[i] = ((PointOnCurve2D) poly.pointAt(i)).parameter();
    }

    /**
     * �I�t�Z�b�g�_���v�Z����
     *
     * @return �I�t�Z�b�g�_��
     */
    private Point2D[] set_offset_points() {
        Point2D[] offset_points = new Point2D[sampling_parameters.length];

        for (int i = 0; i < sampling_parameters.length; i++)
            offset_points[i] = make_offset_point(sampling_parameters[i]);

        return offset_points;
    }

    /**
     * �I�t�Z�b�g�p���?�^��v�Z����
     *
     * @param offset_points �I�t�Z�b�g�_��
     *
     * @return �I�t�Z�b�g�p���?�^
     */
    private double[] set_offset_parameters(Point2D[] offset_points) {
        double[] parameters = new double[offset_points.length];
        parameters[0] = 0.0;

        Point2D source_point = offset_points[0];

        for (int i = 1; i < offset_points.length; i++) {
            parameters[i] += (offset_points[i].distance(source_point) +
            parameters[i - 1]);
            source_point = offset_points[i];
        }

        double[] offset_parameters = new double[parameters.length];
        double lower = pint.lower();
        double interval = pint.increase();
        double maxlength = parameters[parameters.length - 1];

        for (int i = 0; i < parameters.length; i++) {
            offset_parameters[i] = lower +
                ((parameters[i] / maxlength) * interval);
        }

        return offset_parameters;
    }

    /**
     * �I�t�Z�b�g�_���B�X�v���C����?�ŋߎ�����
     *
     * @param offset_points �I�t�Z�b�g�_��
     * @param offset_parameters �I�t�Z�b�g�p���??[�^
     *
     * @return �I�t�Z�b�g��?�
     */
    private BsplineCurve2D approx_bsc(Point2D[] offset_points,
        double[] offset_parameters) {
        // �ߎ������?��ɓn����?���v�Z����
        ToleranceForDistance mid_tol = comp_mid_tol();

        // ���[�_��?�?�x�N�g��
        Vector2D[] EndVector = new Vector2D[2];
        EndVector[0] = curve.tangentVector(pint.start());
        EndVector[1] = curve.tangentVector(pint.end());

        // ���Ă���
        if (poly.isClosed()) {
            Point2D[] closed_offset_points = new Point2D[offset_points.length -
                1];

            for (int i = 0; i < (offset_points.length - 1); i++)
                closed_offset_points[i] = offset_points[i];

            offset_points = closed_offset_points;
        }

        // �ߎ�����B�X�v���C���I�u�W�F�N�g��?�?�
        BsplineCurve2D bsc = new BsplineCurve2D(offset_points,
                offset_parameters, EndVector, poly.isClosed(), tolerance,
                mid_tol);

        return bsc;
    }

    /**
     * �~�ʂ܂ł̋���?H��?�߂�
     *
     * @param points �T���v�����O�_��
     * @param index �~��?l������_�̃C���f�b�N�X
     *
     * @return �~�ʂ܂ł̋���
     */
    private double get_circular_arc_height(Point2D[] points, int index) {
        double height;
        double leng01;
        double leng02;
        Circle2D circle;

        try {
            circle = new Circle2D(points[index], points[index + 1],
                    points[index + 2]);
        } catch (InvalidArgumentValueException e) {
            return 0;
        }

        leng01 = points[index].distance(points[index + 1]) / 2;
        leng02 = points[index + 1].distance(points[index + 2]) / 2;

        if (leng01 > leng02) {
            height = circle.radius() -
                Math.sqrt((circle.radius() * circle.radius()) -
                    (leng01 * leng01));
        } else {
            height = circle.radius() -
                Math.sqrt((circle.radius() * circle.radius()) -
                    (leng02 * leng02));
        }

        return height;
    }

    /**
     * �ߎ������?��ɓn����?���?�߂�
     *
     * @return �ߎ������?��ɓn����?�
     */
    private ToleranceForDistance comp_mid_tol() {
        double mid_tol;
        double height;
        int i;
        int j;

        if (poly.nPoints() < 3) {
            return tolerance;
        }

        mid_tol = 0.0;

        for (i = 2, j = 0; i < poly.nPoints(); i++, j++) {
            height = get_circular_arc_height(poly.points(), j);

            if ((i == 2) || (mid_tol < height)) {
                mid_tol = height;
            }
        }

        // �~�܂��͑ȉ~�Ńp���??[�^�͈͂��Q�΂̂Ƃ�

        /*
           ���Ă���?�?��͓_�̔z��͈ȉ��̂悤�ɂȂBĂ���
        
                          0    1    2    ?c
        ?c  n-3    n-2    n-1
        
        �܂�_���?�?���?Ō�͓���_���Ă���?B
        ���̂��ߕ��Ă���Ƃ���
        n-2    0    1
        ��?�?��̌�?��Ҳ�ׂ�K�v������?B
        */
        if (curve.isClosed() == true) {
            Point2D[] cross_boundary_points = new Point2D[3];

            cross_boundary_points[0] = poly.pointAt(poly.nPoints() - 2);

            //sampling_points[sampling_points.length-2];
            cross_boundary_points[1] = poly.pointAt(0);
            cross_boundary_points[2] = poly.pointAt(1);

            height = get_circular_arc_height(cross_boundary_points, 0);

            if (mid_tol < height) {
                mid_tol = height;
            }
        }

        mid_tol += tolerance.value();

        return new ToleranceForDistance(mid_tol);
    }

    /**
     * ��̃p���??[�^�l�ɑ΂���I�t�Z�b�g�_��v�Z����
     *
     * @param param �p���??[�^
     *
     * @return �I�t�Z�b�g�_
     *
     * @throws InvalidArgumentValueException DOCUMENT ME!
     */
    private Point2D make_offset_point(double param) {
        Vector2D tng = curve.tangentVector(param);
        tng = tng.unitized();

        LiteralVector2D offset_dir;

        if (side == WhichSide.LEFT) {
            offset_dir = new LiteralVector2D(-1 * tng.y(), tng.x());
        } else if (side == WhichSide.RIGHT) {
            offset_dir = new LiteralVector2D(tng.y(), -1 * tng.x());
        } else {
            throw new InvalidArgumentValueException();
        }

        offset_dir = (LiteralVector2D) offset_dir.unitized();
        offset_dir = (LiteralVector2D) offset_dir.multiply(magni);

        Point2D cnt_pnt = curve.coordinates(param);
        Point2D offset_point = cnt_pnt.add(offset_dir);

        return offset_point;
    }

    /**
     * �I�t�Z�b�g��?��Ԃ�
     *
     * @return �I�t�Z�b�g��?�
     */
    BsplineCurve2D offset() {
        if (magni == 0.0) {
            return curve.toBsplineCurve(pint);
        } else {
            return offset_bsc();
        }
    }
}
