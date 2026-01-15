package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.CharacterData;


/**
 * 
 */
public class PMRCharacterDataImpl extends PMRNodeImpl implements CharacterData {
/**
     * Creates a new PMRCharacterDataImpl object.
     */
    public PMRCharacterDataImpl() {
        super();
    }

/**
     * Creates a new PMRCharacterDataImpl object.
     *
     * @param cd  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMRCharacterDataImpl(CharacterData cd, PMRDocument doc) {
        super(cd, doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    public void deleteData(int i, int j) {
        ((CharacterData) delegateNode).deleteData(i, j);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getData() {
        return ((CharacterData) delegateNode).getData();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String substringData(int i, int j) {
        return ((CharacterData) delegateNode).substringData(i, j);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void appendData(String s) {
        ((CharacterData) delegateNode).appendData(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setData(String s) {
        ((CharacterData) delegateNode).setData(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return ((CharacterData) delegateNode).getLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    public void replaceData(int i, int j, String s) {
        ((CharacterData) delegateNode).replaceData(i, j, s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    public void insertData(int i, String s) {
        ((CharacterData) delegateNode).insertData(i, s);
    }
}
