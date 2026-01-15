/*
 * �R���� : �t�B���b�g�Ȗ�?���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FilletObject3D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R���� : �t�B���b�g�Ȗ�?���\���N���X?B
 * <p/>
 * ���̃N���X��?A��􉽗v�f��?ڂ���~�ʂ̋O?� (�����t�B���b�g�Ƃ���) ��\��?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * <ul>
 * <li> �t�B���b�g�̒f�ʂ̗� sections ({@link FilletSection3D FilletSection3D}[])
 * </ul>
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 */

public class FilletObject3D extends NonParametricSurface3D {

    /**
     * �t�B���b�g�f�ʂ̔z��?B
     *
     * @serial
     */
    private FilletSection3D[] sections;

    /**
     * �t�B���b�g�f�ʂ̔z���^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * sections �̗v�f?��� 2 ���?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param sections �t�B���b�g�f�ʂ̔z��
     * @see InvalidArgumentValueException
     */
    FilletObject3D(FilletSection3D[] sections) {
        int nSections;

        if ((nSections = sections.length) < 2)
            throw new InvalidArgumentValueException();

        this.sections = new FilletSection3D[nSections];
        for (int i = 0; i < nSections; i++)
            this.sections[i] = sections[i];
    }

    /**
     * ���̃t�B���b�g�̒f�ʂ̗��Ԃ�?B
     *
     * @return �t�B���b�g�f�ʂ̗�
     */
    public FilletSection3D[] sections() {
        FilletSection3D[] copied = new FilletSection3D[nSections()];
        for (int i = 0; i < nSections(); i++)
            copied[i] = sectionAt(i);
        return copied;
    }

    /**
     * ���̃t�B���b�g�̒f�ʂ�?���Ԃ�?B
     *
     * @return ���̃t�B���b�g�̒f�ʂ�?�
     */
    public int nSections() {
        return sections.length;
    }

    /**
     * ���̃t�B���b�g�� n �Ԃ߂̒f�ʂ�Ԃ�?B
     *
     * @param n �C���f�b�N�X
     * @return n �Ԃ߂̒f��
     */
    public FilletSection3D sectionAt(int n) {
        return sections[n];
    }

    /**
     * ���̃t�B���b�g�̊e�f�ʂ� pointOnGeometry1 �� pointOnGeometry2 ��귂�����̂�Ԃ�?B
     *
     * @return �e�f�ʂ� pointOnGeometry1 �� pointOnGeometry2 ��귂����t�B���b�g
     * @see FilletSection3D#exchange()
     */
    FilletObject3D exchange() {
        FilletSection3D[] copied = sections();
        for (int i = 0; i < nSections(); i++)
            copied[i] = copied[i].exchange();
        return new FilletObject3D(copied);
    }

    /**
     * ���̃t�B���b�g�̒�?S�̋O?Ղ�\����?��Ԃ�?B
     *
     * @return ��?S�̋O?Ղ�\���|�����C��
     */
    public Polyline3D curveOfCenter() {
        Point3D[] pnts3D = new Point3D[nSections()];

        for (int i = 0; i < nSections(); i++)
            pnts3D[i] = sectionAt(i).center();

        return new Polyline3D(pnts3D);
    }

    /**
     * ���̃t�B���b�g�̊e�f�ʂ� pointOnGeometry1 ���̊􉽗v�f���Ȗʂł����̂Ƃ���?A
     * ���̋Ȗ�?�̓_�̋O?Ղ�\���Q������?��Ԃ�?B
     *
     * @return �Ȗ�?�̓_�̋O?Ղ�\���Q�����̃|�����C��
     */
    public Polyline2D curveOnSurface1() {
        Point2D[] pnts2D = new Point2D[nSections()];

        for (int i = 0; i < nSections(); i++) {
            PointOnSurface3D pnt3D = sectionAt(i).pointOnSurface1();
            pnts2D[i] = Point2D.of(pnt3D.uParameter(), pnt3D.vParameter());
        }

        return new Polyline2D(pnts2D);
    }

    /**
     * ���̃t�B���b�g�̊e�f�ʂ� pointOnGeometry2 ���̊􉽗v�f���Ȗʂł����̂Ƃ���?A
     * ���̋Ȗ�?�̓_�̋O?Ղ�\���Q������?��Ԃ�?B
     *
     * @return �Ȗ�?�̓_�̋O?Ղ�\���Q�����̃|�����C��
     */
    public Polyline2D curveOnSurface2() {
        Point2D[] pnts2D = new Point2D[nSections()];

        for (int i = 0; i < nSections(); i++) {
            PointOnSurface3D pnt3D = sectionAt(i).pointOnSurface2();
            pnts2D[i] = Point2D.of(pnt3D.uParameter(), pnt3D.vParameter());
        }

        return new Polyline2D(pnts2D);
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isFreeform() {
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
        writer.println(indent_tab + "\tsections");
        for (int i = 0; i < nSections(); i++) {
            sectionAt(i).output(writer, indent + 2);
        }
        writer.println(indent_tab + "End");
    }
}

