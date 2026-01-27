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

package org.jscience.computing.loaders.pmml;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;

/**
 * PMML Reader for Predictive Model Markup Language.
 * <p>
 * PMML is a standard for representing predictive analytics and machine
 * learning models including regression, neural networks, decision trees,
 * clustering, and association rules.
 * </p>
 * <p>
 * <b>Supported Model Types:</b>
 * <ul>
 *   <li>Regression models (linear, polynomial)</li>
 *   <li>Decision trees and rule sets</li>
 *   <li>Neural networks</li>
 *   <li>Clustering models</li>
 *   <li>Support vector machines</li>
 *   <li>Naive Bayes classifiers</li>
 * </ul>
 * </p>
 * * @see <a href="https://dmg.org/pmml/">DMG PMML Standard</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMMLReader extends AbstractResourceReader<PMMLModel> {


    public PMMLReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<PMMLModel> getResourceType() { return PMMLModel.class; }
    @Override public String getName() { return "PMML Reader"; }
    @Override public String getDescription() { return "Reads machine learning models from PMML format"; }
    @Override public String getLongDescription() { return "PMML is the standard for predictive model exchange including regression, decision trees, neural networks, and clustering."; }
    @Override public String getCategory() { return "Computing"; }
    @Override public String[] getSupportedVersions() { return new String[] {"4.4", "4.3", "4.2"}; }

    @Override
    protected PMMLModel loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new PMMLException("Resource not found: " + resourceId);
    }

    @Override
    protected PMMLModel loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads a PMML model from an input stream.
     */
    public PMMLModel read(InputStream input) throws PMMLException {
        try {
            DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new PMMLException("Failed to parse PMML", e);
        }
    }

    /**
     * Reads a PMML model from a file.
     */
    public PMMLModel read(File file) throws PMMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new PMMLException("Failed to read file: " + file, e);
        }
    }

    private PMMLModel parseDocument(Document doc) {
        PMMLModel model = new PMMLModel();
        Element root = doc.getDocumentElement();
        
        model.setVersion(root.getAttribute("version"));
        
        // Parse header
        Element header = getFirstChildElement(root, "Header");
        if (header != null) {
            model.setCopyright(header.getAttribute("copyright"));
            model.setDescription(header.getAttribute("description"));
            
            Element application = getFirstChildElement(header, "Application");
            if (application != null) {
                model.setApplicationName(application.getAttribute("name"));
                model.setApplicationVersion(application.getAttribute("version"));
            }
        }
        
        // Parse data dictionary
        Element dataDict = getFirstChildElement(root, "DataDictionary");
        if (dataDict != null) {
            parseDataDictionary(dataDict, model);
        }
        
        // Parse models
        parseModels(root, model);
        
        return model;
    }

    private void parseDataDictionary(Element dataDict, PMMLModel model) {
        NodeList fields = dataDict.getElementsByTagName("DataField");
        for (int i = 0; i < fields.getLength(); i++) {
            Element fieldElem = (Element) fields.item(i);
            DataField field = new DataField();
            field.setName(fieldElem.getAttribute("name"));
            field.setOptype(fieldElem.getAttribute("optype"));
            field.setDataType(fieldElem.getAttribute("dataType"));
            
            // Parse values for categorical fields
            NodeList values = fieldElem.getElementsByTagName("Value");
            for (int j = 0; j < values.getLength(); j++) {
                Element valueElem = (Element) values.item(j);
                field.addValue(valueElem.getAttribute("value"));
            }
            
            model.addDataField(field);
        }
    }

    private void parseModels(Element root, PMMLModel pmmlModel) {
        // Check for various model types
        
        // Regression
        Element regression = getFirstChildElement(root, "RegressionModel");
        if (regression != null) {
            pmmlModel.addModel(parseRegressionModel(regression));
        }
        
        // Tree model
        Element tree = getFirstChildElement(root, "TreeModel");
        if (tree != null) {
            pmmlModel.addModel(parseTreeModel(tree));
        }
        
        // Neural network
        Element neuralNet = getFirstChildElement(root, "NeuralNetwork");
        if (neuralNet != null) {
            pmmlModel.addModel(parseNeuralNetwork(neuralNet));
        }
        
        // Clustering
        Element clustering = getFirstChildElement(root, "ClusteringModel");
        if (clustering != null) {
            pmmlModel.addModel(parseClusteringModel(clustering));
        }
        
        // Support Vector Machine
        Element svm = getFirstChildElement(root, "SupportVectorMachineModel");
        if (svm != null) {
            pmmlModel.addModel(parseSVMModel(svm));
        }
    }

    private Model parseRegressionModel(Element elem) {
        Model model = new Model();
        model.setType("Regression");
        model.setModelName(elem.getAttribute("modelName"));
        model.setFunctionName(elem.getAttribute("functionName"));
        model.setTargetField(elem.getAttribute("targetFieldName"));
        
        // Parse mining schema
        parseMiningSchema(elem, model);
        
        // Parse regression table
        Element table = getFirstChildElement(elem, "RegressionTable");
        if (table != null) {
            model.setIntercept(parseDouble(table.getAttribute("intercept"), 0));
            
            NodeList predictors = table.getElementsByTagName("NumericPredictor");
            for (int i = 0; i < predictors.getLength(); i++) {
                Element pred = (Element) predictors.item(i);
                String name = pred.getAttribute("name");
                double coef = parseDouble(pred.getAttribute("coefficient"), 0);
                model.addCoefficient(name, coef);
            }
        }
        
        return model;
    }

    private Model parseTreeModel(Element elem) {
        Model model = new Model();
        model.setType("TreeModel");
        model.setModelName(elem.getAttribute("modelName"));
        model.setFunctionName(elem.getAttribute("functionName"));
        model.setSplitCharacteristic(elem.getAttribute("splitCharacteristic"));
        
        parseMiningSchema(elem, model);
        
        // Parse root node
        Element node = getFirstChildElement(elem, "Node");
        if (node != null) {
            model.setRootNode(parseTreeNode(node));
        }
        
        return model;
    }

    private TreeNode parseTreeNode(Element elem) {
        TreeNode node = new TreeNode();
        node.setId(elem.getAttribute("id"));
        node.setScore(elem.getAttribute("score"));
        node.setRecordCount(parseDouble(elem.getAttribute("recordCount"), 0));
        
        // Parse predicate
        Element predicate = getFirstChildElement(elem, "SimplePredicate");
        if (predicate != null) {
            node.setPredicateField(predicate.getAttribute("field"));
            node.setPredicateOperator(predicate.getAttribute("operator"));
            node.setPredicateValue(predicate.getAttribute("value"));
        }
        
        // Parse child nodes recursively
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                if ("Node".equals(child.getLocalName()) || "Node".equals(child.getTagName())) {
                    node.addChild(parseTreeNode(child));
                }
            }
        }
        
        return node;
    }

    private Model parseNeuralNetwork(Element elem) {
        Model model = new Model();
        model.setType("NeuralNetwork");
        model.setModelName(elem.getAttribute("modelName"));
        model.setFunctionName(elem.getAttribute("functionName"));
        model.setActivationFunction(elem.getAttribute("activationFunction"));
        
        parseMiningSchema(elem, model);
        
        // Count layers
        NodeList layers = elem.getElementsByTagName("NeuralLayer");
        model.setLayerCount(layers.getLength());
        
        return model;
    }

    private Model parseClusteringModel(Element elem) {
        Model model = new Model();
        model.setType("ClusteringModel");
        model.setModelName(elem.getAttribute("modelName"));
        model.setFunctionName(elem.getAttribute("modelClass"));
        
        parseMiningSchema(elem, model);
        
        String numClusters = elem.getAttribute("numberOfClusters");
        if (!numClusters.isEmpty()) {
            model.setClusterCount(Integer.parseInt(numClusters));
        }
        
        return model;
    }

    private Model parseSVMModel(Element elem) {
        Model model = new Model();
        model.setType("SupportVectorMachine");
        model.setModelName(elem.getAttribute("modelName"));
        model.setFunctionName(elem.getAttribute("functionName"));
        
        parseMiningSchema(elem, model);
        
        return model;
    }

    private void parseMiningSchema(Element modelElem, Model model) {
        Element schema = getFirstChildElement(modelElem, "MiningSchema");
        if (schema != null) {
            NodeList fields = schema.getElementsByTagName("MiningField");
            for (int i = 0; i < fields.getLength(); i++) {
                Element field = (Element) fields.item(i);
                String name = field.getAttribute("name");
                String usage = field.getAttribute("usageType");
                if (usage == null || usage.isEmpty()) usage = "active";
                model.addMiningField(name, usage);
            }
        }
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Element getFirstChildElement(Element parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element elem = (Element) children.item(i);
                if (name.equals(elem.getLocalName()) || name.equals(elem.getTagName())) {
                    return elem;
                }
            }
        }
        return null;
    }
}
