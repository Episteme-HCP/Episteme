/*
 * �p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractParametricCurve.java,v 1.2 2006/03/01 21:15:51 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �p���?�g���b�N�ȋ�?� P(t) �̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X��?A��̎�?��l�ŕ\�����p���??[�^ t �̒l�ɂ�B�?A
 * �ʒu�����肳����?� P(t) �S�ʂ��?��?�����\������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:51 $
 * @see ParameterDomain
 * @see AbstractParametricSurface
 */

public abstract class AbstractParametricCurve extends GeometryElement {

    /**
     * ��?�̃p���??[�^��`��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * ?�?����̒l�� null?B
     * </p>
     *
     * @serial
     */
    private ParameterDomain domain;

    /**
     * �􉽓I�ɕ��Ă��邩�ۂ����t���O?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * ?�?����̒l�� TrueFalseUndefined.UNDEFINED?B
     * </p>
     *
     * @serial
     * @see TrueFalseUndefined
     */
    private int closedCurve;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected AbstractParametricCurve() {
        super();
        initFields();
    }

    /**
     * private �ȃt�B?[���h��?�����?B
     *
     * @see TrueFalseUndefined
     */
    private void initFields() {
        domain = null;
        closedCurve = TrueFalseUndefined.UNDEFINED;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ��?�̃p���??[�^��`�悪�L�Ŕ���I��?�?�?A
     * pint �������p���??[�^��Ԃ�?A
     * ��?�̃p���??[�^��`��̓Ք�Ɏ�܂BĂ��Ȃ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * �Ȃ�?Apint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     * @see #parameterDomain()
     */
    public abstract double length(ParameterSection pint);

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return ��?�̃p���??[�^��`��
     */
    public ParameterDomain parameterDomain() {
        if (domain == null) {
            domain = getParameterDomain();
        }
        return domain;
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     *
     * @return �􉽓I�ɕ��Ă���� true?A�����łȂ���� false
     */
    public boolean isClosed() {
        if (closedCurve == TrueFalseUndefined.UNDEFINED) {
            if (getClosedFlag()) {
                closedCurve = TrueFalseUndefined.TRUE;
            } else {
                closedCurve = TrueFalseUndefined.FALSE;
            }
        }
        if (closedCurve == TrueFalseUndefined.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���̋�?�􉽓I�ɊJ���Ă��邩�ۂ���Ԃ�?B
     *
     * @return �􉽓I�ɊJ���Ă���� true?A�����łȂ���� false
     */
    public boolean isOpen() {
        return !isClosed();
    }

    /**
     * ��?�ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return ��?�Ȃ̂�?A?�� <code>true</code>
     */
    public boolean isCurve() {
        return true;
    }

    /**
     * �p���?�g���b�N���ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �p���?�g���b�N�Ȃ̂�?A?�� <code>true</code>
     */
    public boolean isParametric() {
        return true;
    }

    /**
     * ���̋�?�L���ۂ���Ԃ�?B
     *
     * @return �L�ł���� true?A�����łȂ���� false
     */
    public boolean isFinite() {
        return parameterDomain().isFinite();
    }

    /**
     * ���̋�?���ۂ���Ԃ�?B
     *
     * @return ���ł���� true?A�����łȂ���� false
     */
    public boolean isInfinite() {
        return parameterDomain().isInfinite();
    }

    /**
     * ���̋�?�̃p���??[�^��`�悪���I���ۂ���Ԃ�?B
     *
     * @return ���I�ł���� true?A�����łȂ���� false
     */
    public boolean isPeriodic() {
        return parameterDomain().isPeriodic();
    }

    /**
     * ���̋�?�̃p���??[�^��`�悪����I���ۂ���Ԃ�?B
     *
     * @return ����I�ł���� true?A�����łȂ���� false
     */
    public boolean isNonPeriodic() {
        return parameterDomain().isNonPeriodic();
    }

    /**
     * �^����ꂽ�p���??[�^��?A���̋�?�̒�`��̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * value ��?A�L��ȃp���??[�^��Ԃ̒[�_�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     * <p/>
     * ���e��?��Ƃ���?A��?�?ݒ肳��Ă��鉉�Z?�?��?u�p���??[�^�l�̋��e��?�?v��Q?Ƃ���?B
     * </p>
     *
     * @param value ��?�����p���??[�^
     * @return value ����`��̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @see ConditionOfOperation
     * @see ParameterDomain#isValid(double)
     */
    public boolean isValid(double value) {
        return parameterDomain().isValid(value);
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋�?�̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * value �����̋�?�̃p���??[�^��`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param value ��?�����p���??[�^�l
     * @see ParameterOutOfRange
     * @see ParameterDomain#checkValidity(double)
     */
    public void checkValidity(double value) {
        parameterDomain().checkValidity(value);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃ�?A���̋�?�̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * section �̑?���l�͕��ł�?\��Ȃ�?B
     * </p>
     *
     * @param section ��?�����p���??[�^���
     * @throws ParameterOutOfRange           �p���??[�^��Ԃ���`���O��Ă���
     * @throws InvalidArgumentValueException �p���??[�^��Ԃ̒�����?A����蒷��
     *                                       (���I��?�?��̂�)
     * @see ParameterDomain#checkValidity(ParameterSection)
     */
    public void checkValidity(ParameterSection section) {
        parameterDomain().checkValidity(section);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ���?ۃ?�\�b�h?B
     *
     * @return ���̋�?�̃p���??[�^��`��
     */
    abstract ParameterDomain getParameterDomain();

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ���?ۃ?�\�b�h?B
     *
     * @return �􉽓I�ɕ��Ă���� true?A�����łȂ���� false
     */
    abstract boolean getClosedFlag();
}
