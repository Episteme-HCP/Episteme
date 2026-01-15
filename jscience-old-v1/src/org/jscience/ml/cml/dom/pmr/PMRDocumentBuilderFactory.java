package org.jscience.ml.cml.dom.pmr;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;

/**
 * at present hardcoded to using default
 */

public class PMRDocumentBuilderFactory extends DocumentBuilderFactory {
    HashMap<String, Object> attributeMap;
    HashMap<String, Boolean> featureMap;

    public PMRDocumentBuilderFactory() {
        attributeMap = new HashMap<String, Object>();
        featureMap = new HashMap<String, Boolean>();
    }

    /**
     * Obtain a new instance of a DocumentBuilderFactory
     * This static method creates a new factory instance. This method uses the following ordered lookup procedure to determine the DocumentBuilderFactory implementation class to load:
     * Use the javax.xml.parsers.DocumentBuilderFactory system property.
     * Use the properties file "lib/jaxp.properties" in the JRE directory. This configuration file is in standard java.util.Properties format and contains the fully qualified name of the implementation class with the key being the system property defined above.
     * Use the Services API (as detailed in the JAR specification), if available, to determine the classname. The Services API will look for a classname in the file META-INF/services/javax.xml.parsers.DocumentBuilderFactory in jars available to the runtime.
     * Platform default DocumentBuilderFactory instance.
     * Once an application has obtained a reference to a DocumentBuilderFactory it can use the factory to configure and obtain parser instances.
     * Throws:
     * FactoryConfigurationError - if the implementation is not available or cannot be instantiated.
     */
    public static DocumentBuilderFactory newInstance() throws FactoryConfigurationError {
        return new PMRDocumentBuilderFactory();
    }

    /**
     * Creates a new instance of a DocumentBuilder using the currently configured parameters.
     * Returns:
     * A new instance of a DocumentBuilder.
     * Throws:
     * ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     */
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        return new PMRDocumentBuilder();
    }

    /**
     * Allows the user to set specific attributes on the underlying
     * implementation.
     *
     * @param name  The name of the attribute.
     * @param value The value of the attribute.
     * @throws IllegalArgumentException thrown if the underlying
     *                                  implementation doesn't recognize the attribute.
     */
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        attributeMap.put(name, value);
    }

    /**
     * Allows the user to retrieve specific attributes on the underlying
     * implementation.
     *
     * @param name The name of the attribute.
     * @return value The value of the attribute.
     * @throws IllegalArgumentException thrown if the underlying
     *                                  implementation doesn't recognize the attribute.
     */
    public Object getAttribute(String name) throws IllegalArgumentException {
        return attributeMap.get(name);
    }

    /**
     * <p>Set a feature for this <code>DocumentBuilderFactory</code> and <code>DocumentBuilder</code>s created by this factory.</p>
     * <p/>
     * <p/>
     * Feature names are fully qualified {@link java.net.URI}s.
     * Implementations may define their own features.
     * An {@link ParserConfigurationException} is thrown if this <code>DocumentBuilderFactory</code> or the
     * <code>DocumentBuilder</code>s it creates cannot support the feature.
     * It is possible for an <code>DocumentBuilderFactory</code> to expose a feature value but be unable to change its state.
     * </p>
     * <p/>
     * <p/>
     * All implementations are required to support the {@link javax.xml.XMLConstants#FEATURE_SECURE_PROCESSING} feature.
     * When the feature is:</p>
     * <ul>
     * <li>
     * <code>true</code>: the implementation will limit XML processing to conform to implementation limits.
     * Examples include enity expansion limits and XML Schema constructs that would consume large amounts of resources.
     * If XML processing is limited for security reasons, it will be reported via a call to the registered
     * {@link org.xml.sax.ErrorHandler#fatalError(SAXParseExceptionexception)}.
     * See {@link  DocumentBuilder#setErrorHandler(org.xml.sax.ErrorHandlererrorHandler)}.
     * </li>
     * <li>
     * <code>false</code>: the implementation will processing XML according to the XML specifications without
     * regard to possible implementation limits.
     * </li>
     * </ul>
     *
     * @param name  Feature name.
     * @param value Is feature state <code>true</code> or <code>false</code>.
     * @throws ParserConfigurationException if this <code>DocumentBuilderFactory</code> or the <code>DocumentBuilder</code>s
     *                                      it creates cannot support this feature.
     * @throws NullPointerException         If the <code>name</code> parameter is null.
     */
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        featureMap.put(name, value);
    }

    /**
     * <p>Get the state of the named feature.</p>
     * <p/>
     * <p/>
     * Feature names are fully qualified {@link java.net.URI}s.
     * Implementations may define their own features.
     * An {@link ParserConfigurationException} is thrown if this <code>DocumentBuilderFactory</code> or the
     * <code>DocumentBuilder</code>s it creates cannot support the feature.
     * It is possible for an <code>DocumentBuilderFactory</code> to expose a feature value but be unable to change its state.
     * </p>
     *
     * @param name Feature name.
     * @return State of the named feature.
     * @throws ParserConfigurationException if this <code>DocumentBuilderFactory</code>
     *                                      or the <code>DocumentBuilder</code>s it creates cannot support this feature.
     */
    public boolean getFeature(String name) throws ParserConfigurationException {
        return featureMap.get(name);
    }

}
