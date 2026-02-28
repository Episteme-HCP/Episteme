/*
 * �p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractParametricSurface.java,v 1.2 2006/03/01 21:15:51 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X��?A��̎�?��l�ŕ\�����p���??[�^�̑g (u, v) �̒l�ɂ�B�?A
 * �ʒu�����肳���Ȗ� P(u, v) �S�ʂ��?��?�����\������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:51 $
 * @see ParameterDomain
 * @see AbstractParametricCurve
 */

public abstract class AbstractParametricSurface extends GeometryElement {
    /**
     * U ���̃p���??[�^��`��
     * <p/>
     * �K�v�ɉ����ăL���b�V�������
     * </p>
     *
     * @serial
     */
    private ParameterDomain uDomain;

    /**
     * V ���̃p���??[�^��`��
     * <p/>
     * �K�v�ɉ����ăL���b�V�������
     * </p>
     *
     * @serial
     */
    private ParameterDomain vDomain;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected AbstractParametricSurface() {
        super();
        initFields();
    }

    /**
     * private �ȃt�B?[���h��?�����?B
     */
    private void initFields() {
        uDomain = null;
        vDomain = null;
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     *
     * @return U ���̃p���??[�^��`��
     */
    public ParameterDomain uParameterDomain() {
        if (uDomain == null) {
            uDomain = getUParameterDomain();
        }
        return uDomain;
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     *
     * @return V ���̃p���??[�^��`��
     */
    public ParameterDomain vParameterDomain() {
        if (vDomain == null) {
            vDomain = getVParameterDomain();
        }
        return vDomain;
    }

    /**
     * �Ȗʂ��ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �ȖʂȂ̂�?A?�� <code>true</code>
     */
    public boolean isSurface() {
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
     * ���̋Ȗʂ� U ���ɗL���ۂ���Ԃ�?B
     *
     * @return U ���ɗL�ł���� true?A�����łȂ���� false
     */
    public boolean isUFinite() {
        return uParameterDomain().isFinite();
    }

    /**
     * ���̋Ȗʂ� V ���ɗL���ۂ���Ԃ�?B
     *
     * @return V ���ɗL�ł���� true?A�����łȂ���� false
     */
    public boolean isVFinite() {
        return vParameterDomain().isFinite();
    }

    /**
     * ���̋Ȗʂ� U ���ɖ����ۂ���Ԃ�?B
     *
     * @return U ���ɖ��ł���� true?A�����łȂ���� false
     */
    public boolean isUInfinite() {
        return uParameterDomain().isInfinite();
    }

    /**
     * ���̋Ȗʂ� V ���ɖ����ۂ���Ԃ�?B
     *
     * @return V ���ɖ��ł���� true?A�����łȂ���� false
     */
    public boolean isVInfinite() {
        return vParameterDomain().isInfinite();
    }

    /**
     * ���̋Ȗʂ� U ���Ɏ��I���ۂ���Ԃ�
     *
     * @return U ���Ɏ��I�ł���� true?A�����łȂ���� false
     */
    public boolean isUPeriodic() {
        return uParameterDomain().isPeriodic();
    }

    /**
     * ���̋Ȗʂ� V ���Ɏ��I���ۂ���Ԃ�
     *
     * @return V ���Ɏ��I�ł���� true?A�����łȂ���� false
     */
    public boolean isVPeriodic() {
        return vParameterDomain().isPeriodic();
    }

    /**
     * ���̋Ȗʂ� U ���ɔ���I���ۂ���Ԃ�
     *
     * @return U ���ɔ���I�ł���� true?A�����łȂ���� false
     */
    public boolean isUNonPeriodic() {
        return uParameterDomain().isNonPeriodic();
    }

    /**
     * ���̋Ȗʂ� V ���ɔ���I���ۂ���Ԃ�
     *
     * @return V ���ɔ���I�ł���� true?A�����łȂ���� false
     */
    public boolean isVNonPeriodic() {
        return vParameterDomain().isNonPeriodic();
    }

    /**
     * �^����ꂽ�p���??[�^��?A���̋Ȗʂ� U ���̒�`��̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * value ��?A�L��ȃp���??[�^��Ԃ̒[�_�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     * <p/>
     * ���e��?��Ƃ���?A��?�?ݒ肳��Ă��鉉�Z?�?��?u�p���??[�^�l�̋��e��?�?v��Q?Ƃ���?B
     * </p>
     *
     * @param value ��?�����p���??[�^�l
     * @return value �� U ���̒�`��̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @see ConditionOfOperation
     * @see ParameterDomain#isValid(double)
     */
    public boolean isValidUParameter(double value) {
        return uParameterDomain().isValid(value);
    }

    /**
     * �^����ꂽ�p���??[�^��?A���̋Ȗʂ� V ���̒�`��̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * value ��?A�L��ȃp���??[�^��Ԃ̒[�_�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     * <p/>
     * ���e��?��Ƃ���?A��?�?ݒ肳��Ă��鉉�Z?�?��?u�p���??[�^�l�̋��e��?�?v��Q?Ƃ���?B
     * </p>
     *
     * @param value ��?�����p���??[�^�l
     * @return value �� V ���̒�`��̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @see ConditionOfOperation
     * @see ParameterDomain#isValid(double)
     */
    public boolean isValidVParameter(double value) {
        return vParameterDomain().isValid(value);
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� U ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     *
     * @param value ��?�����U���̃p���??[�^�l
     * @throws ParameterOutOfRange �p���??[�^����`���O��Ă���
     * @see ParameterDomain#checkValidity(double)
     */
    public void checkUValidity(double value) {
        uParameterDomain().checkValidity(value);
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� V ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     *
     * @param value ��?�����V���̃p���??[�^�l
     * @throws ParameterOutOfRange �p���??[�^����`���O��Ă���
     * @see ParameterDomain#checkValidity(double)
     */
    public void checkVValidity(double value) {
        vParameterDomain().checkValidity(value);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃ�?A���̋Ȗʂ� U ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * section �̑?���l�͕��ł�?\��Ȃ�?B
     * </p>
     *
     * @param section ��?�����U���̃p���??[�^���
     * @throws ParameterOutOfRange           �p���??[�^��Ԃ���`���O��Ă���
     * @throws InvalidArgumentValueException �p���??[�^��Ԃ̒�����?A����蒷��
     *                                       (���I��?�?��̂�)
     * @see ParameterDomain#checkValidity(ParameterSection)
     */
    public void checkUValidity(ParameterSection section) {
        uParameterDomain().checkValidity(section);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃ�?A���̋Ȗʂ� V ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * section �̑?���l�͕��ł�?\��Ȃ�?B
     * </p>
     *
     * @param section ��?�����V���̃p���??[�^���
     * @throws ParameterOutOfRange           �p���??[�^��Ԃ���`���O��Ă���
     * @throws InvalidArgumentValueException �p���??[�^��Ԃ̒�����?A����蒷��
     *                                       (���I��?�?��̂�)
     * @see ParameterDomain#checkValidity(ParameterSection)
     */
    public void checkVValidity(ParameterSection section) {
        vParameterDomain().checkValidity(section);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ���?ۃ?�\�b�h?B
     *
     * @return ���̋Ȗʂ� U ���̃p���??[�^��`��
     */
    abstract ParameterDomain getUParameterDomain();

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ���?ۃ?�\�b�h?B
     *
     * @return ���̋Ȗʂ� V ���̃p���??[�^��`��
     */
    abstract ParameterDomain getVParameterDomain();
}
