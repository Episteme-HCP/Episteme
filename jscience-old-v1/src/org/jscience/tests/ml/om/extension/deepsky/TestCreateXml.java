/* ====================================================================
 * extension/deepSky/test/TestCreateXml
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.tests.ml.om.extension.deepsky;

import org.jscience.ml.om.IObservation;
import org.jscience.ml.om.RootElement;
import org.jscience.ml.om.util.SchemaException;

import java.io.File;


/**
 * Simple Test class that writes an XML
 */
public class TestCreateXml {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                "Need to pass xmlFilePath as argument. E.g. /home/john/myTestObservation.xml");

            return;
        }

        String xmlFilePath = args[0];

        DeepSkyTestUtil dst = new DeepSkyTestUtil();

        IObservation obs1 = dst.createDeepSkyObservation();
        IObservation obs2 = dst.createDeepSkyObservation2();
        IObservation obs3 = dst.createDeepSkyObservation3();

        RootElement observations = new RootElement();

        try {
            observations.addObservation(obs1);
            observations.addObservation(obs2);
            observations.addObservation(obs3);
        } catch (SchemaException schemaException) {
            System.err.println(
                "Cannot add DeepSkyObservation. Nested Exception is:" +
                schemaException);
        }

        try {
            observations.serializeAsXml(new File(xmlFilePath));
        } catch (SchemaException schemaException) {
            System.err.println("Cannot serialized XML. nested Exception is: " +
                schemaException);
        }
    }
}
