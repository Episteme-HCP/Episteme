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

package mypackage.filters;

import org.jscience.sociology.forms.Form;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * This is the xsltExample application's filter, that applies XSLT
 * transformations to the form's XML data.
 */
public class FormXslFilter implements Filter {
    /**
     * DOCUMENT ME!
     */
    private FilterConfig config = null;

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     */
    public void init(FilterConfig config) {
        this.config = config;
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        config = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param chain DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        // do nothing before
        chain.doFilter(request, response);

        String formName = ((HttpServletRequest) request).getPathInfo()
                           .substring(1);
        String contextPath = config.getServletContext()
                                   .getRealPath(File.separator);

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Form form = (Form) session.getAttribute(formName);
        StringReader xmlStringReader = form.generateXML(); // contains XML file
                                                           //    System.out.println(form.generateXmlAsString());

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = ((HttpServletResponse) response).getWriter();

        try {
            FileInputStream xslInputStream = new FileInputStream(contextPath +
                    form.getFormPagePath());
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Source xmlSource = new StreamSource(xmlStringReader);
            Source xslSource = new StreamSource(xslInputStream);

            // Generate the transformer.
            Transformer transformer = tFactory.newTransformer(xslSource);
            // Perform the transformation, sending the output to the response.
            transformer.transform(xmlSource, new StreamResult(out));
        } catch (TransformerException e) {
            out.println("A TransformerException has happend");
            out.println("The message is : " + e.getMessage());
            e.printStackTrace();
        }

        out.close();
    }
}
