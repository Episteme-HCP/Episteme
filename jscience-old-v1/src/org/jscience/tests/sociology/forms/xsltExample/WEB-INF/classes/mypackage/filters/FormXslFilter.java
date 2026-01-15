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
