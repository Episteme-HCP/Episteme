/*
 * �R���� : �t�B���b�g�̒f�ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FilletSection3D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R���� : �t�B���b�g�̒f�ʂ�\���N���X?B
 * <p/>
 * ���̃N���X��?A��􉽗v�f��?ڂ���~�ʂ̋O?� (�����t�B���b�g�Ƃ���) ��
 * �����f�ʂ�\��?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * <ul>
 * <li> �t�B���b�g�f�ʂ̔��a radius
 * <li> �t�B���b�g�f�ʂ̒�?S center
 * <li> �t�B���b�g�f�ʂ����̊􉽗v�f (�􉽗v�f 1) ��?ڂ���_ pointOnGeometry1
 * <li> �t�B���b�g�f�ʂ�����̊􉽗v�f (�􉽗v�f 2) ��?ڂ���_ pointOnGeometry2
 * </ul>
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 * @see FilletObject3D
 */

public class FilletSection3D extends NonParametricCurve3D {

    /**
     * �t�B���b�g�f�ʂ̔��a?B
     *
     * @serial
     */
    private double radius;

    /**
     * �t�B���b�g�f�ʂ̒�?S?B
     *
     * @serial
     */
    private Point3D center;

    /**
     * �􉽗v�f 1 ?�̓_?B
     *
     * @serial
     */
    private PointOnGeometry3D pointOnGeometry1;

    /**
     * �􉽗v�f 2 ?�̓_?B
     *
     * @serial
     */
    private PointOnGeometry3D pointOnGeometry2;

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?Apubic �ȃR���X�g���N�^�ł͂Ȃ�����?A��?��̃`�F�b�N��?s�BĂ��Ȃ�?B
     * public�ɂ���K�v��?o��?�?���?AdoCheck ��?���?��̂�?��K�v������?B
     * </p>
     *
     * @param radius           �t�B���b�g�f�ʂ̔��a
     * @param center           �t�B���b�g�f�ʂ̒�?S
     * @param pointOnGeometry1 �􉽗v�f 1 ?�̓_
     * @param pointOnGeometry2 �􉽗v�f 2 ?�̓_
     */
    FilletSection3D(double radius, Point3D center,
                    PointOnGeometry3D pointOnGeometry1,
                    PointOnGeometry3D pointOnGeometry2) {
        super();
        this.radius = radius;
        this.center = center;
        this.pointOnGeometry1 = pointOnGeometry1;
        this.pointOnGeometry2 = pointOnGeometry2;
    }

    /**
     * ���̃t�B���b�g�f�ʂ̔��a��Ԃ�?B
     *
     * @return �t�B���b�g�̔��a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * ���̃t�B���b�g�f�ʂ̒�?S��Ԃ�?B
     *
     * @return �t�B���b�g�̒�?S
     */
    public Point3D center() {
        return this.center;
    }

    /**
     * ���̃t�B���b�g�f�ʂ̊􉽗v�f 1 ?�̓_��Ԃ�?B
     *
     * @return �􉽗v�f 1 ?�̓_
     */
    public PointOnGeometry3D pointOnGeometry1() {
        return this.pointOnGeometry1;
    }

    /**
     * ���̃t�B���b�g�f�ʂ̊􉽗v�f 2 ?�̓_��Ԃ�?B
     *
     * @return �􉽗v�f 2 ?�̓_
     */
    public PointOnGeometry3D pointOnGeometry2() {
        return this.pointOnGeometry2;
    }

    /**
     * ���̃t�B���b�g�f�ʂ̊􉽗v�f 1 ��ȖʂƂ���?A���̋Ȗ�?�̓_��Ԃ�?B
     *
     * @return �Ȗʂł���􉽗v�f 1 ?�̓_
     */
    public PointOnSurface3D pointOnSurface1() {
        if (!pointOnGeometry1().geometry().isSurface())
            return null;
        return (PointOnSurface3D) pointOnGeometry1();
    }

    /**
     * ���̃t�B���b�g�f�ʂ̊􉽗v�f 2 ��ȖʂƂ���?A���̋Ȗ�?�̓_��Ԃ�?B
     *
     * @return �Ȗʂł���􉽗v�f 2 ?�̓_
     */
    public PointOnSurface3D pointOnSurface2() {
        if (!pointOnGeometry2().geometry().isSurface())
            return null;
        return (PointOnSurface3D) pointOnGeometry2();
    }

    /**
     * ���̃t�B���b�g�f�ʂ��?� (�~��) �ɕϊ�����?B
     * <p/>
     * smallFan �� true ��?�?���?A��?S�p���΂��?������Ȃ��̉~�ʂ�Ԃ�?B
     * smallFan �� false ��?�?���?A��?S�p���΂��傫���Ȃ��̉~�ʂ�Ԃ�?B
     * �Ȃ�?A�~�ʂ�?i?s���͕K�� pointOnGeometry1 ���� pointOnGeometry2 �֌�?B
     * </p>
     *
     * @param smallFan �~�ʂ̒�?S�p���΂��?������Ȃ�悤�ɂ��邩�ǂ���?B
     * @return �t�B���b�g�f�ʂ�\���~��
     */
    public TrimmedCurve3D toCurve(boolean smallFan) {
        Vector3D vecS = pointOnSurface1().subtract(center()).unitized();
        Vector3D vecE = pointOnSurface2().subtract(center()).unitized();
        if (vecS.parallelDirection(vecE))
            throw new InvalidArgumentValueException();

        Vector3D nrm = vecS.crossProduct(vecE);
        double angle = vecS.angleWith(vecE, nrm);

        if ((angle < Math.PI && !smallFan) ||
                (angle > Math.PI && smallFan))
            angle -= GeometryUtils.PI2;

        Axis2Placement3D a2p = new Axis2Placement3D(center(), nrm, vecS);
        Circle3D cir = new Circle3D(a2p, radius);
        ParameterSection section = new ParameterSection(0.0, angle);
        return new TrimmedCurve3D(cir, section);
    }

    /**
     * ���̃t�B���b�g�f�ʂ� pointOnGeometry1 �� pointOnGeometry2 ��귂�����̂�Ԃ�?B
     *
     * @return pointOnGeometry1 �� pointOnGeometry2 ��귂����t�B���b�g�f��
     */
    FilletSection3D exchange() {
        return new FilletSection3D(radius(), center(), pointOnGeometry2(), pointOnGeometry1());
    }

    /**
     * ���̃t�B���b�g�f�ʂ�?A�^����ꂽ�t�B���b�g�f�ʂ�����̃t�B���b�g�f�ʂ�\�����ۂ��𒲂ׂ�?B
     * <p/>
     * ��?�ł̓p���??[�^�l�͎Q?Ƃ���?A�P�Ɋ􉽓I�ȓ���?��݂̂𒲂ׂĂ���?B
     * </p>
     *
     * @return ����̃t�B���b�g�f�ʂ�\���Ȃ�� true?A�����łȂ���� false
     */
    boolean parametricallyIdentical(FilletSection3D mate) {
        if (!center.identical(mate.center()))
            return false;

        if (!pointOnGeometry1.identical(mate.pointOnGeometry1()))
            return false;

        if (!pointOnGeometry2.identical(mate.pointOnGeometry2()))
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
        writer.println(indent_tab + "\tpointOnGeometry1");
        pointOnGeometry1().output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnGeometry2");
        pointOnGeometry2().output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
