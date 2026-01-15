/*
 * $Id: OMBinding.java,v 1.2 2007-10-21 17:46:56 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl/
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which 
 *  case the provisions of the LGPL license are applicable instead of those 
 *  above. A copy of the LGPL license is available at http://www.gnu.org/
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti, Wouter Wiersma 
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Models an OpenMath binding object. <p>
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @author Wouter Wiersma (W.T.Wiersma@stud.tue.nl)
 * @version $Revision: 1.2 $
 * @see "The OpenMath standard 2.0, 2.1.3"
 */
public class OMBinding extends OMObject {
    /**
     * Stores the binder. <p>
     */
    protected OMObject binder;

    /**
     * Stores the variables. <p>
     */
    protected Vector variables = new Vector();

    /**
     * Stores the body. <p>
     */
    protected OMObject body;

    /**
     * Constructor. <p>
     */
    public OMBinding() {
        super();
    }

    /**
     * Constructor. <p>
     *
     * @param newBinder    the binder.
     * @param newVariables a Vector with the bound variables.
     * @param newBody      the body.
     */
    public OMBinding(OMObject newBinder, Vector newVariables, OMObject newBody) {
        super();

        binder = newBinder;
        variables = newVariables;
        body = newBody;
    }

    /**
     * Gets the type. <p>
     *
     * @return the type of the OMObject.
     */
    public String getType() {
        return "OMBIND";
    }

    /**
     * Get the binder. <p>
     *
     * @return the binder, or <b>null</b> if not set.
     */
    public OMObject getBinder() {
        return binder;
    }

    /**
     * Sets the binder. <p>
     *
     * @param newBinder sets the binder.
     */
    public void setBinder(OMObject newBinder) {
        binder = newBinder;
    }

    /**
     * Get the variables. <p>
     *
     * @return the variables, or <b>null</b> if not set.
     */
    public Vector getVariables() {
        return variables;
    }

    /**
     * Set the variables. <p>
     *
     * @param newVariables the variables.
     */
    public void setVariables(Vector newVariables) {
        variables = newVariables;
    }

    /**
     * Get the body. <p>
     *
     * @return the body, or <b>null</b> if not set.
     */
    public OMObject getBody() {
        return body;
    }

    /**
     * Set the body. <p>
     *
     * @param newBody the body
     */
    public void setBody(OMObject newBody) {
        body = newBody;
    }

    /**
     * Get variable at i-place. <p>
     *
     * @param index the index of the variable.
     * @return the variable.
     */
    public OMObject getVariableAt(int index) {
        return (OMObject) variables.elementAt(index);
    }

    /**
     * Set variable at i-place. <p>
     *
     * @param object the object to set.
     * @param index  the index to set at.
     */
    public void setVariableAt(OMObject object, int index) {
        variables.setElementAt(object, index);
    }

    /**
     * Insert variable at i-place. <p>
     *
     * @param object the object to insert.
     * @param index  the index to insert at.
     */
    public void insertVariableAt(OMObject object, int index) {
        variables.insertElementAt(object, index);
    }

    /**
     * Remove variable at i-place. <p>
     *
     * @param index the index to remove from.
     */
    public void removeVariableAt(int index) {
        variables.removeElementAt(index);
    }

    /**
     * Add variable. <p>
     *
     * @param object adds a variable (at the end).
     */
    public void addVariable(OMObject object) {
        variables.addElement(object);
    }

    /**
     * Remove variable. <p>
     * <p/>
     * <p/>
     * <i>Note: This removes the first occurence of the given
     * variable. If you want to remove all the references to
     * the given object, continue remove until this returns
     * false.</i>
     * </p>
     *
     * @param object the variable to remove.
     * @return <b>true</b> if there are more occurrences, <b>false</b> if not.
     */
    public boolean removeVariable(OMObject object) {
        return variables.removeElement(object);
    }

    /**
     * Remove all variables. <p>
     */
    public void removeAllVariables() {
        variables.removeAllElements();
    }

    /**
     * Get the first variable. <p>
     *
     * @return the first variable.
     */
    public OMObject firstVariable() {
        return (OMObject) variables.firstElement();
    }

    /**
     * Get the last variable. <p>
     *
     * @return the last variable.
     */
    public OMObject lastVariable() {
        return (OMObject) variables.lastElement();
    }

    /**
     * Returns a string representation of the object. <p>
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        Enumeration enumeration = variables.elements();

        result.append("<OMBIND>");
        result.append(binder);
        result.append("<OMBVAR>");

        for (; enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            result.append(object.toString());
        }

        result.append("</OMBVAR>");
        result.append(body);
        result.append("</OMBIND>");

        return result.toString();
    }

    /**
     * Clones the object (shallow copy).
     */
    public Object clone() {
        OMBinding binding = new OMBinding();
        binding.binder = this.binder;
        binding.variables = this.variables;
        binding.body = this.body;

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();
            binding.setAttribute(key, value);
        }

        return binding;
    }

    /**
     * Copies the object (full copy).
     */
    public Object copy() {
        OMBinding binding = new OMBinding();
        binding.binder = (OMObject) this.binder.copy();
        binding.variables = new Vector();
        binding.body = (OMObject) this.body.copy();

        Enumeration variables = this.variables.elements();

        for (; variables.hasMoreElements();) {
            binding.variables.addElement(((OMObject) variables.nextElement()).copy());
        }

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            binding.setAttribute(key, value);
        }

        return binding;
    }

    /**
     * Are we a composite object.
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Are we an atom object.
     */
    public boolean isAtom() {
        return false;
    }

    /**
     * Determines if this is the same object. <p>
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMBinding) {
            OMBinding binding = (OMBinding) object;

            if (!body.isSame(binding.getBody()))
                return false;

            if (!binder.isSame(binding.getBinder()))
                return false;

            if (variables.size() == binding.variables.size()) {
                Enumeration enum1 = variables.elements();
                Enumeration enum2 = binding.variables.elements();

                for (; enum1.hasMoreElements();) {
                    OMObject object1 = (OMObject) enum1.nextElement();
                    OMObject object2 = (OMObject) enum2.nextElement();

                    if (!object1.isSame(object2))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this is a valid object. <p>
     */
    public boolean isValid() {
        for (Enumeration enumeration = variables.elements(); enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            if (!object.isValid())
                return false;
        }

        if (!binder.isValid())
            return false;

        if (!body.isValid())
            return false;

        return true;
    }

    /**
     * Replace any occurrence of source to destination. <p>
     * <p/>
     * Note: this method will not replace bound variables inside a binding. This
     * will be the responsibility of the alphaConvert method. So if the
     * given source is a bound variable the replacing will NOT occur, use
     * the alphaConvert method instead!
     *
     * @param source the source object.
     * @param dest   the destination object.
     * @return the application with the replacing.
     */
    public OMObject replace(OMObject source, OMObject dest) {
        /*
        * Make sure the source is not a bound variable, otherwise we just
        * return the current OMBinding.
        */
        for (Enumeration enumeration = variables.elements();
             enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            if (source.isSame(object))
                return this;
        }

        if (!body.isSame(source)) {
            /*
             * OK, we are sure we are not replacing a bound variable and the
             * body is not entirely going to be replaced. So now we can call
             * replace on the body of the binding.
            */
            if (body instanceof OMApplication) {
                OMApplication application = (OMApplication) body;
                body = application.replace(source, dest);
            }
            if (body instanceof OMAttribution) {
                OMAttribution attribution = (OMAttribution) body;
                body = attribution.replace(source, dest);
            }
            if (body instanceof OMBinding) {
                OMBinding binding = (OMBinding) body;
                body = binding.replace(source, dest);
            }
            if (body instanceof OMError) {
                OMError error = (OMError) body;
                body = error.replace(source, dest);
            }
        } else {
            /*
            * OK, we are replacing the entire body.
            */
            body = dest;
        }
        return this;
    }

    /**
     * Performs light-weight Beta reduction. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> While reducing name-capturing might occur. This method
     * does NOT do alpha conversion to avoid this problem. So consider this
     * to be light-weight Beta reduction.
     * </p>
     *
     * @param var       the variable to reduce.
     * @param reduction the object to reduce it to.
     * @return the beta reduced object.
     */
    public OMObject betaReduce(OMObject var, OMObject reduction) {
        /*
        * Make sure it is actually a bound variable. If it is, remove it from
        * the variable vector.
        */
        if (var instanceof OMVariable) {
            Enumeration enumeration = variables.elements();
            int i = 0;
            for (; enumeration.hasMoreElements();) {
                OMObject object = (OMObject) enumeration.nextElement();
                if (var.isSame(object)) {
                    removeVariableAt(i);
                }
                i++;
            }
        }

        if (variables.size() == 0) {
            if (body instanceof OMApplication) {
                OMApplication application = (OMApplication) body;
                return application.replace(var, reduction);
            } else if (body instanceof OMAttribution) {
                OMAttribution attribution = (OMAttribution) body;
                return attribution.replace(var, reduction);
            } else if (body instanceof OMBinding) {
                OMBinding binding = (OMBinding) body;
                return binding.replace(var, reduction);
            } else if (body instanceof OMError) {
                OMError error = (OMError) body;
                return error.replace(var, reduction);
            } else if (body instanceof OMVariable) {
                OMVariable variable = (OMVariable) body;
                if (variable.isSame(var))
                    return reduction;
            } else {
                return body;
            }
        }

        /*
        * And now replace the source with destination. Note that this will work
        * because we have removed the bound variable previously.
        */
        return replace(var, reduction);
    }

    /**
     * Perform Alpha conversion on the binding and its children. <p>
     * <p/>
     * Note: the renaming is done by means of the replace method, this will
     * ensure that any bound variable lower in the tree with the same
     * name as the variable in this binding will not be replaced .
     *
     * @param source the source object
     * @param dest   the destination object
     * @return the converted object
     */
    public OMObject alphaConvert(OMObject source, OMObject dest) {
        /*
        * Make sure it is actually a bound variable. If it is, replace
        * it with the destination. And also replace it in the body of the
        * binding.
        */
        if (source instanceof OMVariable) {
            Enumeration enumeration = variables.elements();
            int i = 0;
            for (; enumeration.hasMoreElements();) {
                OMObject object = (OMObject) enumeration.nextElement();
                if (source.isSame(object)) {
                    setVariableAt(dest, i);
                    return replace(source, dest);
                }
                i++;
            }
        }
        return this;
    }
}
