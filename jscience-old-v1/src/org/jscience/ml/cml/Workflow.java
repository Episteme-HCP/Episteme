package org.jscience.ml.cml;

/**
 * represents workflow
 */
public interface Workflow {
    /**
     * run the workflow
     *
     * @throws CMLException DOCUMENT ME!
     */
    void run() throws CMLException;

    /**
     * get the CMLDocument for object creation
     *
     * @return DOCUMENT ME!
     */
    AbstractCMLDocument getCMLDocument();

/**
     * store object by name.
     */

    //    public void putObject(String name, Object object) throws CMLException;
/**
     * get object by name.
     */

    //    public Object getObject(String name);
/**
     * substitute variables in string. e.g. if bar==abc and plugh==qqq
     * foo${bar}zz${plugh}yy ==> fooabczzqqqyy not recursive
     */

    //    public String substituteVariables(String value) throws CMLException;
/**
     * process command
     */

    //    public void process(Node node) throws CMLException;
}
