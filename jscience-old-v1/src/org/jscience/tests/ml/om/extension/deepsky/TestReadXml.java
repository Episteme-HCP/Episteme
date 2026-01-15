/* ====================================================================
 * extension/deepSky/test/TestReadXml
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om.extension.deepsky.test;

import org.jscience.ml.om.FGCAException;
import org.jscience.ml.om.RootElement;
import org.jscience.ml.om.util.SchemaLoader;

import java.io.File;


/**
 * Simple Test class that reads a XML and writes it again
 */
public class TestReadXml {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Need to pass xmlFilePath and newXmlFilePath as arguments. E.g. /home/john/myTestObservation.xml /home/john/myNewTestObservation.xml");

            return;
        }

        SchemaLoader loader = null;

        try {
            // Read
            loader = new SchemaLoader();

            RootElement obs = loader.load(new File(args[0]),
                    new File("file:/home/dirk/programming/java/observation/xml/basic/comast14.xsd"));

            obs.serializeAsXml(new File(args[1]));
        } catch (FGCAException fgca) {
            System.err.println("Error while loading document: " +
                fgca.getMessage());
        }
    }
}
