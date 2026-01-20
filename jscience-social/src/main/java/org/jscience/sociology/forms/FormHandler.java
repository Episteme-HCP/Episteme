/*
 * @(#)FormHandler.java  2.0, 2001/06
 *
 * Copyright (C) 2001 Ilirjan Ostrovica. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.jscience.sociology.forms;

import org.jscience.sociology.forms.view.ContentGenerator;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * This class does the form handling. It's <code>process()</code> method
 * calls the <code>validate()</code> method of the Form instance and further
 * on calls the appropriate method of <code>ContentGenerator</code>.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 *
 * @see Form
 */
public class FormHandler {
    /**
     * DOCUMENT ME!
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * DOCUMENT ME!
     */
    private String web_inf_path;

    /**
     * DOCUMENT ME!
     */
    private String datDirPath;

    /**
     * DOCUMENT ME!
     */
    private Hashtable forms;

    /**
     * DOCUMENT ME!
     */
    private ContentGenerator contentGenerator;

    /**
     * Initializes the <code>FormHandler</code> object. This method is
     * called by
     * <code>org.jscience.sociology.forms.control.FormControllerServlet</code>.
     *
     * @param config the ServletConfig object of
     *        <code>FormControllerServlet</code>.
     */
    public void init(ServletConfig config)
        throws javax.servlet.ServletException {
        String contextPath = config.getServletContext()
                                   .getRealPath(File.separator);
        String formDesigner = config.getInitParameter("formDesigner");
        String xmlReaderName = config.getInitParameter("XMLReaderName");

        web_inf_path = contextPath + "WEB-INF" + File.separator;

        File datDir = new File(web_inf_path + "dat");

        if (!datDir.isDirectory()) {
            datDir.mkdir();
        }

        datDirPath = datDir.getPath();

        FormParser formParser = createFormParser(formDesigner, xmlReaderName);

        forms = formParser.getForms(); // parsing is done here
        contentGenerator = formParser.getContentGenerator();

        Enumeration en = forms.keys();
        String formName = null;

        while (en.hasMoreElements()) {
            formName = (String) en.nextElement();
            serializeForm(formName, (Form) forms.get(formName));
        }
    }

    /**
     * This is a key method for this API. It handles all the work for
     * processing the form and it also calls the appropriate methods of
     * <code>ContentGenerator</code>. This method is called by
     * <code>org.jscience.sociology.forms.control.FormControllerServlet</code>.
     *
     * @param formName the name of the form being processed.
     * @param req the HttpServletRequest object.
     * @param resp the HttpServletResponse object.
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void process(String formName, HttpServletRequest req,
        HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Form cachedForm = (Form) session.getAttribute(formName);

        if (cachedForm == null) { // first time
                                  // make sure form name exists

            if (!forms.containsKey(formName)) {
                throw new ServletException("The URL : " + req.getRequestURI() +
                    " is malformed, it must end with a valid form name (already defined in forms.xml)");
            }

            cachedForm = deserializeForm(formName);
            session.setAttribute(cachedForm.getName(), cachedForm);
            contentGenerator.sendFormContent(req, resp, cachedForm);

            return;
        } else {
            cachedForm.setFirstime(false);
        }

        if (cachedForm.validate(req)) { // validation went well
            contentGenerator.forwardToFormAction(req, resp, cachedForm);
        } else { //  validation went wrong
            contentGenerator.sendFormContent(req, resp, cachedForm);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param form DOCUMENT ME!
     */
    private void serializeForm(String name, Form form) {
        try {
            FileOutputStream fos = new FileOutputStream(datDirPath +
                    File.separator + name + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(form);
        } catch (IOException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("IOException happend while trying to serialize " +
                name + ".dat file");
            System.out.println("The message is: " + e.getMessage());
            System.out.println("----------------------" + NEW_LINE);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Form deserializeForm(String name) {
        Form toReturn = null;

        try {
            FileInputStream fis = new FileInputStream(datDirPath +
                    File.separator + name + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            toReturn = (Form) ois.readObject();
        } catch (IOException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println(
                "IOException happend while trying to deserialize " + name +
                ".dat file");
            System.out.println("The message is: " + e.getMessage());
            System.out.println("----------------------" + NEW_LINE);
        } catch (ClassNotFoundException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println(
                "ClassNotFoundException happend while trying to deserialize " +
                name + ".dat file");
            System.out.println("The message is: " + e.getMessage());
            System.out.println("----------------------" + NEW_LINE);
        }

        return toReturn;
    }

    /**
     * DOCUMENT ME!
     *
     * @param formDesigner DOCUMENT ME!
     * @param xmlReaderName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    private FormParser createFormParser(String formDesigner,
        String xmlReaderName) throws ServletException {
        if (formDesigner == null) {
            formDesigner = "forms.xml";
        }

        FileInputStream formsInputStream = null;
        XMLReader xmlReader = null;

        try {
            formsInputStream = new FileInputStream(web_inf_path + formDesigner);
            xmlReader = XMLReaderFactory.createXMLReader(xmlReaderName);
        } catch (SAXException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("Error creating XMLReader: " + e.getMessage());
            System.out.println("----------------------" + NEW_LINE);
        } catch (IOException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("IOException : " + e.getMessage());
            System.out.println("----------------------" + NEW_LINE);
        }

        return new FormParser(xmlReader, formsInputStream);
    }
}
