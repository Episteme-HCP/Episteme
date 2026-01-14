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

import java.util.Hashtable;
import java.util.Stack;

/**
 * ConditionOfOperation �́A���Z�쏂�\���N���X�ł��B
 * <p/>
 * ���Z�쏂Ƃ́AJGCL �̃I�u�W�F�N�g�̃��\�b�h��
 * �e��̊􉽉��Z��s�Ȃ���ŎQ�Ƃ��鋖�e�덷��܂Ƃ߂���̂ł��B
 * <br>
 * ��̉��Z�쏂́A�ȉ��̋��e�덷�BĂ��܂��B
 * <ul>
 * <li>	{@link ToleranceForDistance  �����̋��e�덷}
 * <li>	{@link ToleranceForAngle     �p�x�̋��e�덷}
 * <li>	{@link ToleranceForParameter �p�����[�^�l�̋��e�덷}
 * <li>	{@link Tolerance             ���@���Ȃ����l�̋��e�덷}
 * </ul>
 * </p>
 * <p/>
 * JGCL �ł́AJava �̃X���b�h���ɈقȂ鉉�Z�쏂�ݒ肷�邱�Ƃ��ł��܂��B
 * <br>
 * �X�̃X���b�h�ɂ́A���Z�쏂�܂ރX�^�b�N������Ή����܂��B
 * ���̃X�^�b�N�͏����Ԃł͋� (empty) �ł��B
 * ���[�U�́A���̃X�^�b�N�ɔC�ӂ̉��Z�쏂�� push/pop ���邱�Ƃ��ł��܂��B
 * <br>
 * JGCL �̃I�u�W�F�N�g�̃��\�b�h�́A
 * ���炩�̋��e�덷���f���K�v�ȏꍇ�ɁA
 * ���ꂪ�
 * s�����X���b�h�ɑΉ�����
 * ���Z�쏃X�^�b�N�̈�ԏ�ɂ��鉉�Z�쏂�Q�Ƃ��܂��B
 * �X�^�b�N����̏ꍇ�ɂ́A�f�t�H���g�̉��Z�쏂�Q�Ƃ��܂��B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:55 $
 */

public class ConditionOfOperation extends java.lang.Object {

    /**
     * �����̋��e�덷�B
     */
    private final ToleranceForDistance dTol;

    /**
     * �p�x�̋��e�덷�B
     */
    private final ToleranceForAngle aTol;

    /**
     * �p�����[�^�l�̋��e�덷�B
     */
    private final ToleranceForParameter pTol;

    /**
     * ���@���Ȃ����l�̋��e�덷�B
     */
    private final Tolerance rTol;

    /**
     * Thread ��L�[�Ƃ����A���Z�쏃X�^�b�N�̃n�b�V���e�[�u���B
     */
    private static final Hashtable threadTable;

    /**
     * �f�t�H���g�̉��Z�쏁B
     * <p/>
     * ����l�Ƃ��Đݒ肳��鉉�Z�쏂̊e���e�덷�̒l�͈ȉ��̒ʂ�B
     * <pre>
     * �����̋��e�덷 : 1.0e-4
     * �p�x�̋��e�덷 : Math.PI * 0.1 / 180.0
     * �p�����[�^�l�̋��e�덷 : 1.0e-8
     * ���@���Ȃ����l�̋��e�덷 : 1.0e-8
     * </pre>
     * </p>
     */
    private static ConditionOfOperation defaultCondition;

    /**
     * static �ȃt�B�[���h�ɒl��ݒ肷��B
     */
    static {
        defaultCondition = new ConditionOfOperation();
        threadTable = new Hashtable();
    }

    /**
     * �ȉ��̋��e�덷�l�ŃI�u�W�F�N�g��\�z����B
     * <p/>
     * <pre>
     * �����̋��e�덷 : 1.0e-4
     * �p�x�̋��e�덷 : Math.PI * 0.1 / 180.0
     * �p�����[�^�l�̋��e�덷 : 1.0e-8
     * ���@���Ȃ����l�̋��e�덷 : 1.0e-8
     * </pre>
     * </p>
     */
    public ConditionOfOperation() {
        // these values from GHL3 src/util/gh_utilInit.c

        dTol = new ToleranceForDistance(1.0e-4);
        aTol = new ToleranceForAngle(Math.PI * 0.1 / 180.0);
        pTol = new ToleranceForParameter(1.0e-8);
        rTol = new Tolerance(1.0e-8);
    }

    /**
     * ���e�덷��w�肵�ăI�u�W�F�N�g��\�z����B
     *
     * @param dTol �����̋��e�덷
     * @param aTol �p�x�̋��e�덷
     * @param pTol �p�����[�^�l�̋��e�덷
     * @param rTol ���@���Ȃ����l�̋��e�덷
     */
    public ConditionOfOperation(ToleranceForDistance dTol,
                                ToleranceForAngle aTol,
                                ToleranceForParameter pTol,
                                Tolerance rTol) {
        this.dTol = dTol;
        this.aTol = aTol;
        this.pTol = pTol;
        this.rTol = rTol;
    }

    /**
     * ���̉��Z�쏂���A�����̋��e�덷������w��̒l�ɕύX����������Ԃ��B
     *
     * @param dTol �ݒ肷�鋗���̋��e�덷
     */
    public ConditionOfOperation makeCopy(ToleranceForDistance dTol) {
        return new ConditionOfOperation(dTol, this.aTol, this.pTol, this.rTol);
    }

    /**
     * ���̉��Z�쏂���A�����̋��e�덷������w��̒l�ɕύX����������Ԃ��B
     *
     * @param value �ݒ肷�鋗���̋��e�덷
     * @see ToleranceForDistance
     */
    public ConditionOfOperation makeCopyWithToleranceForDistance(double value) {
        ToleranceForDistance dTol = new ToleranceForDistance(value);
        return makeCopy(dTol);
    }

    /**
     * ���̉��Z�쏂���A�p�x�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param aTol �ݒ肷��p�x�̋��e�덷
     */
    public ConditionOfOperation makeCopy(ToleranceForAngle aTol) {
        return new ConditionOfOperation(this.dTol, aTol, this.pTol, this.rTol);
    }

    /**
     * ���̉��Z�쏂���A�p�x�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param value �ݒ肷��p�x�̋��e�덷
     * @see ToleranceForAngle
     */
    public ConditionOfOperation makeCopyWithToleranceForAngle(double value) {
        ToleranceForAngle aTol = new ToleranceForAngle(value);
        return makeCopy(aTol);
    }

    /**
     * ���̉��Z�쏂���A�p�����[�^�l�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param pTol �ݒ肷��p�����[�^�l�̋��e�덷
     */
    public ConditionOfOperation makeCopy(ToleranceForParameter pTol) {
        return new ConditionOfOperation(this.dTol, this.aTol, pTol, this.rTol);
    }

    /**
     * ���̉��Z�쏂���A�p�����[�^�l�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param value �ݒ肷��p�����[�^�l�̋��e�덷
     * @see ToleranceForParameter
     */
    public ConditionOfOperation makeCopyWithToleranceForParameter(double value) {
        ToleranceForParameter pTol = new ToleranceForParameter(value);
        return makeCopy(pTol);
    }

    /**
     * ���̉��Z�쏂���A���@���Ȃ����l�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param rTol �ݒ肷�鐡�@���Ȃ����l�̋��e�덷
     */
    public ConditionOfOperation makeCopy(Tolerance rTol) {
        return new ConditionOfOperation(this.dTol, this.aTol, this.pTol, rTol);
    }

    /**
     * ���̉��Z�쏂���A���@���Ȃ����l�̋��e�덷������w��̒l�ɕύX����������Ԃ�
     *
     * @param value �ݒ肷�鐡�@���Ȃ����l�̋��e�덷
     * @see Tolerance
     */
    public ConditionOfOperation makeCopyWithToleranceForRealNumber(double value) {
        Tolerance rTol = new Tolerance(value);
        return makeCopy(rTol);
    }

    /**
     * ���̉��Z�쏂̋����̋��e�덷��Ԃ��B
     *
     * @return �����̋��e�덷
     */
    public double getToleranceForDistance() {
        return dTol.value();
    }

    /**
     * ���̉��Z�쏂̋����̋��e�덷��Ԃ��B
     *
     * @return �����̋��e�덷
     */
    public ToleranceForDistance getToleranceForDistanceAsObject() {
        return dTol;
    }

    /**
     * ���̉��Z�쏂̋����̋��e�덷�̎����Ԃ��B
     *
     * @return �����̋��e�덷�̎���
     */
    public double getToleranceForDistance2() {
        return dTol.value2();
    }

    /**
     * ���̉��Z�쏂̊p�x�̋��e�덷��Ԃ��B
     *
     * @return �p�x�̋��e�덷
     */
    public double getToleranceForAngle() {
        return aTol.value();
    }

    /**
     * ���̉��Z�쏂̊p�x�̋��e�덷��Ԃ��B
     *
     * @return �p�x�̋��e�덷
     */
    public ToleranceForAngle getToleranceForAngleAsObject() {
        return aTol;
    }

    /**
     * ���̉��Z�쏂̃p�����[�^�l�̋��e�덷��Ԃ��B
     *
     * @return �p�����[�^�l�̋��e�덷
     */
    public double getToleranceForParameter() {
        return pTol.value();
    }

    /**
     * ���̉��Z�쏂̃p�����[�^�l�̋��e�덷��Ԃ��B
     *
     * @return �p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter getToleranceForParameterAsObject() {
        return pTol;
    }

    /**
     * ���̉��Z�쏂̐��@���Ȃ����l�̋��e�덷��Ԃ��B
     *
     * @return ���@���Ȃ����l�̋��e�덷
     */
    public double getToleranceForRealNumber() {
        return rTol.value();
    }

    /**
     * ���̉��Z�쏂̐��@���Ȃ����l�̋��e�덷��Ԃ��B
     *
     * @return ���@���Ȃ����l�̋��e�덷
     */
    public Tolerance getToleranceForRealNumberAsObject() {
        return rTol;
    }

    /**
     * �f�t�H���g�Ƃ��Đݒ肳��Ă��鉉�Z�쏂�Ԃ��B
     */
    public static ConditionOfOperation getDefaultCondition() {
        return defaultCondition;
    }

    /**
     * �^����ꂽ���Z�쏂�f�t�H���g�Ƃ��Đݒ肷��B
     *
     * @param cond �f�t�H���g�Ƃ��Đݒ肷�鉉�Z��
     */
    public synchronized static void setDefaultCondition(ConditionOfOperation cond) {
        defaultCondition = cond;
    }

    /**
     * �^����ꂽ�X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N����o���B
     *
     * @param theThread �X���b�h
     */
    private static Stack getStack(Thread theThread) {
        Object obj = threadTable.get(theThread);
        Stack stack;

        if (obj == null) {
            stack = new Stack();
            threadTable.put(theThread, stack);
        } else {
            stack = (Stack) obj;
        }

        return stack;
    }

    /**
     * ���ݎ
     * s���̃X���b�h�ɂ����ĎQ�Ƃ����ׂ����Z�쏂���o���B
     * <p/>
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N����ł���΁A
     * �f�t�H���g�̉��Z�쏂�Ԃ��B
     * </p>
     *
     * @return ���ݎ s���̃X���b�h�ɂ����ĎQ�Ƃ����ׂ����Z��
     */
    public static ConditionOfOperation getCondition() {
        ConditionOfOperation cond = peek();
        if (cond == null)
            cond = defaultCondition;
        return cond;
    }

    /**
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N���󂩔ۂ���Ԃ��B
     *
     * @return ���ݎ s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N���󂩔ۂ�
     */
    public static boolean empty() {
        Stack stack = getStack(Thread.currentThread());
        return stack.empty();
    }

    /**
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N�̈�ԏ�ɂ��鉉�Z�쏂�Ԃ��B
     * <p/>
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N����ł���� null ��Ԃ��B
     * </p>
     * <p/>
     * ���̃��\�b�h�́A���Z�쏃X�^�b�N�̏�Ԃ�ύX���Ȃ��B
     * </p>
     *
     * @return ���ݎ s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N�̈�ԏ�ɂ��鉉�Z��
     */
    public static ConditionOfOperation peek() {
        Stack stack = getStack(Thread.currentThread());
        if (stack.empty())
            return null;

        return (ConditionOfOperation) stack.peek();
    }

    /**
     * �^����ꂽ���Z�쏂�A���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N�� push ����B
     *
     * @param condition push ���鉉�Z��
     */
    public static void push(ConditionOfOperation condition) {
        Stack stack = getStack(Thread.currentThread());

        stack.push(condition);
    }

    /**
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N���牉�Z�쏂��� pop ����B
     * <p/>
     * ���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N����̏ꍇ�ɂ� null ��Ԃ��B
     * </p>
     *
     * @return pop ���ꂽ���Z��
     */
    public static ConditionOfOperation pop() {
        Stack stack = getStack(Thread.currentThread());
        if (stack.empty())
            return null;

        ConditionOfOperation cond = (ConditionOfOperation) stack.peek();
        stack.pop();
        return cond;
    }

    /**
     * ���̉��Z�쏂�A�f�t�H���g�̉��Z�쏂Ƃ��Đݒ肷��B
     */
    public synchronized void setDefault() {
        setDefaultCondition(this);
    }

    /**
     * ���̉��Z�쏂�A���ݎ
     * s���̃X���b�h�ɑΉ����鉉�Z�쏃X�^�b�N�� push ����B
     */
    public void push() {
        push(this);
    }
}
