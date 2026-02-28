/*
 * �Q���� : �t�B���b�g��?�?���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FilletObject2D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q���� : �t�B���b�g��?�?���\���N���X?B
 * <p/>
 * ���̃N���X��?A���?��?ڂ���~�� (�����t�B���b�g�Ƃ���) ��\��?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * <ul>
 * <li> �t�B���b�g�̔��a radius
 * <li> �t�B���b�g�̒�?S center
 * <li> �t�B���b�g�����̋�?� (��?� 1) ��?ڂ���_ pointOnCurve1
 * <li> �t�B���b�g������̋�?� (��?� 2) ��?ڂ���_ pointOnCurve2
 * </ul>
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 */

public class FilletObject2D extends NonParametricCurve2D {
    /**
     * �t�B���b�g�̔��a?B
     *
     * @serial
     */
    private double radius;

    /**
     * �t�B���b�g�̒�?S?B
     *
     * @serial
     */
    private Point2D center;

    /**
     * ��?� 1 ?�̓_?B
     *
     * @serial
     */
    private PointOnCurve2D pointOnCurve1;

    /**
     * ��?� 2 ?�̓_?B
     *
     * @serial
     */
    private PointOnCurve2D pointOnCurve2;

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?Apubic �ȃR���X�g���N�^�ł͂Ȃ�����?A��?��̃`�F�b�N��?s�BĂ��Ȃ�?B
     * public�ɂ���K�v��?o��?�?���?AdoCheck ��?���?��̂�?��K�v������?B
     * </p>
     *
     * @param radius        �t�B���b�g�̔��a
     * @param center        �t�B���b�g�̒�?S
     * @param pointOnCurve1 ��?� 1 ?�̓_
     * @param pointOnCurve2 ��?� 2 ?�̓_
     */
    FilletObject2D(double radius, Point2D center,
                   PointOnCurve2D pointOnCurve1, PointOnCurve2D pointOnCurve2) {
        super();
        this.radius = radius;
        this.center = center;
        this.pointOnCurve1 = pointOnCurve1;
        this.pointOnCurve2 = pointOnCurve2;
    }

    /**
     * ���̃t�B���b�g�̔��a��Ԃ�?B
     *
     * @return �t�B���b�g�̔��a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * ���̃t�B���b�g�̒�?S��Ԃ�?B
     *
     * @return �t�B���b�g�̒�?S
     */
    public Point2D center() {
        return this.center;
    }

    /**
     * ���̃t�B���b�g�̋�?� 1 ?�̓_��Ԃ�?B
     *
     * @return ��?� 1 ?�̓_
     */
    public PointOnCurve2D pointOnCurve1() {
        return this.pointOnCurve1;
    }

    /**
     * ���̃t�B���b�g�̋�?� 2 ?�̓_��Ԃ�?B
     *
     * @return ��?� 2 ?�̓_
     */
    public PointOnCurve2D pointOnCurve2() {
        return this.pointOnCurve2;
    }

    /**
     * ���̃t�B���b�g���?� (�~��) �ɕϊ�����?B
     * <p/>
     * smallFan �� true ��?�?���?A��?S�p���΂��?������Ȃ��̉~�ʂ�Ԃ�?B
     * smallFan �� false ��?�?���?A��?S�p���΂��傫���Ȃ��̉~�ʂ�Ԃ�?B
     * �Ȃ�?A�~�ʂ�?i?s���͕K�� pointOnCurve1 ���� pointOnCurve2 �֌�?B
     * </p>
     *
     * @param smallFan �~�ʂ̒�?S�p���΂��?������Ȃ�悤�ɂ��邩�ǂ���
     * @return �t�B���b�g��\���~��
     */
    public TrimmedCurve2D toCurve(boolean smallFan) {
        Vector2D vecS = pointOnCurve1().subtract(center()).unitized();
        Vector2D vecE = pointOnCurve2().subtract(center()).unitized();
        if (vecS.parallelDirection(vecE))
            throw new InvalidArgumentValueException();

        double angle = vecS.angleWith(vecE);

        if ((angle < Math.PI && !smallFan) ||
                (angle > Math.PI && smallFan))
            angle -= GeometryUtils.PI2;

        Axis2Placement2D a2p = new Axis2Placement2D(center(), vecS);
        Circle2D cir = new Circle2D(a2p, radius);
        ParameterSection section = new ParameterSection(0.0, angle);
        return new TrimmedCurve2D(cir, section);
    }

    /**
     * ���̃t�B���b�g�� pointOnCurve1 �� pointOnCurve2 ��귂�����̂�Ԃ�?B
     *
     * @return pointOnCurve1 �� pointOnCurve2 ��귂����t�B���b�g
     */
    FilletObject2D exchange() {
        return new FilletObject2D(radius(), center(), pointOnCurve2(), pointOnCurve1());
    }

    /**
     * ���̃t�B���b�g��?A�^����ꂽ�t�B���b�g������̃t�B���b�g��\�����ۂ��𒲂ׂ�?B
     *
     * @return ����̃t�B���b�g��\���Ȃ�� true?A�����łȂ���� false
     */
    boolean parametricallyIdentical(FilletObject2D mate) {
        if (!center.identical(mate.center()))
            return false;

        if (!pointOnCurve1.parametricallyIdentical(mate.pointOnCurve1()))
            return false;

        if (!pointOnCurve2.parametricallyIdentical(mate.pointOnCurve2()))
            return false;

        return true;
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tradius " + radius);
        writer.println(indent_tab + "\tcenter");
        center().output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnCurve1");
        pointOnCurve1().output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnCurve2");
        pointOnCurve2().output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}

