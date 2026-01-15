/*
 * 1D�|�����C����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Polyline1D.java,v 1.3 2007-10-23 18:19:44 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;


/**
 * 1D�|�����C����\���N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:44 $
 */
class Polyline1D {
    /** DOCUMENT ME! */
    private static final boolean CHECK_SAME_POINTS = false;

    /**
     * ?ߓ_�̔z��
     *
     * @see Point1D
     */
    private Point1D[] points;

    /** �����`�����ۂ���\���t���O */
    private boolean closed;

    /** �p���??[�^�h�?�C�� */
    ParameterDomain parameterDomain;

/**
     * ��`?���^����?�?�����
     *
     * @param points ?ߓ_�̔z��
     * @param closed ���Ă��邩�ۂ���\���t���O
     * @see Point1D
     */
    public Polyline1D(Point1D[] points, boolean closed) {
        super();
        setPoints(points, closed);
    }

/**
     * �_���^���ĊJ�����|�����C����?�?�����
     *
     * @param points ?ߓ_�̔z��
     * @see Point1D
     */
    public Polyline1D(Point1D[] points) {
        super();
        setPoints(points, false);
    }

    /**
     * �p���??[�^�h�?�C���𒲂ׂ�
     *
     * @return �p���??[�^�h�?�C��
     *
     * @throws FatalException DOCUMENT ME!
     *
     * @see ParameterDomain
     */
    ParameterDomain getParameterDomain() {
        double n = closed ? points.length : (points.length - 1);

        try {
            return new ParameterDomain(closed, 0, n);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ?ߓ_�̔z���?ݒ肷��
     *
     * @param points ?ߓ_�̔z��
     * @param closed ���Ă��邩�ۂ���\���t���O
     *
     * @throws InvalidArgumentValueException DOCUMENT ME!
     *
     * @see Point1D
     */
    private void setPoints(Point1D[] points, boolean closed) {
        if ((!closed && (points.length < 2)) ||
                (closed && (points.length < 3))) {
            throw new InvalidArgumentValueException();
        }

        this.closed = closed;
        this.points = new Point1D[points.length];

        this.points[0] = points[0];

        for (int i = 1; i < points.length; i++) {
            if (CHECK_SAME_POINTS) {
                if (points[i].identical(points[i - 1])) {
                    throw new InvalidArgumentValueException();
                }
            }

            this.points[i] = points[i];
        }

        if (CHECK_SAME_POINTS) {
            if (closed && points[0].identical(points[points.length - 1])) {
                throw new InvalidArgumentValueException();
            }
        }
    }

    /**
     * ?ߓ_�̔z���Ԃ�
     *
     * @return ?ߓ_�̔z��
     */
    public Point1D[] points() {
        Point1D[] pnts = new Point1D[points.length];

        for (int i = 0; i < points.length; i++)
            pnts[i] = points[i];

        return pnts;
    }

    /**
     * n �Ԃ߂�?ߓ_��Ԃ� �����`����?An
     * ��?ߓ_��?��ɓ�����?�?���?A0�Ԃ߂�?ߓ_��Ԃ�
     *
     * @param n DOCUMENT ME!
     *
     * @return n �Ԃ߂�?ߓ_
     */
    public Point1D pointAt(int n) {
        if (closed() && (n == nPoints())) {
            return points[0];
        }

        return points[n];
    }

    /**
     * �����`�����ۂ���Ԃ�
     *
     * @return �����`���ł���� <code>true</code>
     *         ��Ԃ�?A ����Ȃ��� <code>false</code>
     *         ��Ԃ�?B
     */
    public boolean closed() {
        return this.closed;
    }

    /**
     * ?ߓ_��?���Ԃ�
     *
     * @return ?ߓ_��?�
     */
    public int nPoints() {
        return points.length;
    }

    /**
     * �Z�O�?���g��?���Ԃ�
     *
     * @return �Z�O�?���g��?�
     */
    public int nSegments() {
        if (closed()) {
            return nPoints();
        }

        return nPoints() - 1;
    }

    /**
     * �p���??[�^�h�?�C���𒲂ׂ�
     *
     * @return �p���??[�^�h�?�C��
     *
     * @see ParameterDomain
     */
    ParameterDomain parameterDomain() {
        return parameterDomain;
    }

    /**
     * �p���??[�^���L��ۂ��𒲂ׂ�
     *
     * @param value ��?�����p���??[�^
     */
    public void checkValidity(double value) {
        parameterDomain().checkValidity(value);
    }

    /**
     * �p���??[�^�͈̔͂�?�����
     *
     * @param param DOCUMENT ME!
     *
     * @return ?ߓ_��?�
     *
     * @see ParameterOutOfRange
     */
    private PolyParam checkParameter(double param) {
        PolyParam p = new PolyParam();

        int n = closed ? points.length : (points.length - 1);

        if (closed) {
            param = parameterDomain().wrap(param);
        } else {
            checkValidity(param);
        }

        int idx = (int) Math.floor(param);

        if (idx < 0) {
            idx = 0;
        }

        if ((n - 1) < idx) {
            idx = n - 1;
        }

        p.sp = points[idx];

        if ((idx + 1) == points.length) {
            p.ep = points[0]; // only closed case
        } else {
            p.ep = points[idx + 1];
        }

        p.weight = param - idx;
        p.param = param;
        p.index = idx;

        return p;
    }

    /**
     * �^����ꂽ�p���??[�^�ł�?W�l��?�߂�
     *
     * @param param �p���??[�^
     *
     * @return ?W�l
     *
     * @see Point1D
     */
    public Point1D coordinates(double param) {
        PolyParam p = checkParameter(param);

        return p.ep.linearInterpolate(p.sp, p.weight);
    }

    // internal use
    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class PolyParam {
        /** DOCUMENT ME! */
        Point1D sp;

        /** DOCUMENT ME! */
        Point1D ep;

        /** DOCUMENT ME! */
        double weight;

        /** DOCUMENT ME! */
        double param;

        /** DOCUMENT ME! */
        int index;
    }
}
