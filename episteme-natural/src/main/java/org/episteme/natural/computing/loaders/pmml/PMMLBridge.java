/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;

import org.episteme.natural.computing.ml.Model;
import org.episteme.natural.computing.ml.RegressionModel;
import org.episteme.natural.computing.ml.TreeModel;
import org.episteme.natural.computing.ml.NeuralNetworkModel;
import org.episteme.core.mathematics.ml.neural.layers.Sequential;
import org.episteme.core.mathematics.ml.neural.layers.Linear;
import org.episteme.core.mathematics.ml.neural.layers.ActivationLayer;
import org.episteme.core.mathematics.ml.neural.ActivationFunction;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.computing.ml.ClusteringModel;
import org.episteme.natural.computing.ml.DataField;
import org.episteme.natural.computing.ml.MiningField;

/**
 * Bridge for converting PMML DTOs to core Episteme machine learning objects.
 * <p>
 * PMML (Predictive Model Markup Language) is the XML standard for representing
 * machine learning models. This bridge converts parsed PMML to Episteme ML structures.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * PMML â†’ PMMLReader â†’ PMML DTOs â†’ PMMLBridge â†’ Core Objects
 *                                              â”œâ”€â”€ RegressionModel
 *                                              â”œâ”€â”€ TreeModel
 *                                              â”œâ”€â”€ NeuralNetworkModel
 *                                              â””â”€â”€ ClusteringModel
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMMLBridge {

    /**
     * Converts PMML document to Episteme ML Model.
     *
     * @param pmmlDoc the parsed PMML document
     * @return the appropriate Model subtype
     */
    public Model toModel(PMMLDocument pmmlDoc) {
        if (pmmlDoc == null || pmmlDoc.getModel() == null) {
            return null;
        }
        
        PMMLModel pmmlModel = pmmlDoc.getModel();
        
        return switch (pmmlModel.getModelType()) {
            case "RegressionModel" -> toRegressionModel((PMMLRegressionModel) pmmlModel, pmmlDoc);
            case "TreeModel" -> toTreeModel((PMMLTreeModel) pmmlModel, pmmlDoc);
            case "NeuralNetwork" -> toNeuralNetworkModel((PMMLNeuralNetwork) pmmlModel, pmmlDoc);
            case "ClusteringModel" -> toClusteringModel((PMMLClusteringModel) pmmlModel, pmmlDoc);
            default -> toGenericModel(pmmlModel, pmmlDoc);
        };
    }

    /**
     * Converts PMML regression model.
     */
    public RegressionModel toRegressionModel(PMMLRegressionModel pmmlReg, PMMLDocument doc) {
        if (pmmlReg == null) {
            return null;
        }
        
        RegressionModel model = new RegressionModel(pmmlReg.getModelName());
        model.setTrait("pmml.function", pmmlReg.getFunctionName());
        model.setTrait("pmml.algorithm", pmmlReg.getAlgorithmName());
        
        // Add data dictionary fields
        addDataFields(model, doc.getDataDictionary());
        
        // Add mining schema
        addMiningSchema(model, pmmlReg.getMiningSchema());
        
        // Set coefficients
        if (pmmlReg.getRegressionTable() != null) {
            PMMLRegressionTable table = pmmlReg.getRegressionTable();
            model.setIntercept(table.getIntercept());
            
            for (PMMLNumericPredictor pred : table.getNumericPredictors()) {
                model.addCoefficient(pred.getName(), pred.getCoefficient());
                model.setTrait("exponent." + pred.getName(), pred.getExponent());
            }
        }
        
        return model;
    }

    /**
     * Converts PMML tree model.
     */
    public TreeModel toTreeModel(PMMLTreeModel pmmlTree, PMMLDocument doc) {
        if (pmmlTree == null) {
            return null;
        }
        
        TreeModel model = new TreeModel(pmmlTree.getModelName());
        model.setTrait("pmml.function", pmmlTree.getFunctionName());
        model.setTrait("pmml.split.characteristic", pmmlTree.getSplitCharacteristic());
        model.setTrait("pmml.missing.value.strategy", pmmlTree.getMissingValueStrategy());
        
        addDataFields(model, doc.getDataDictionary());
        addMiningSchema(model, pmmlTree.getMiningSchema());
        
        // Convert tree structure
        if (pmmlTree.getNode() != null) {
            TreeModel.Node root = convertTreeNode(pmmlTree.getNode());
            model.setRootNode(root);
        }
        
        return model;
    }

    /**
     * Converts a PMML tree node recursively.
     */
    private TreeModel.Node convertTreeNode(PMMLNode pmmlNode) {
        if (pmmlNode == null) {
            return null;
        }
        
        TreeModel.Node node = new TreeModel.Node(pmmlNode.getId());
        node.setScore(pmmlNode.getScore());
        node.setRecordCount(pmmlNode.getRecordCount());
        
        // Set predicate
        if (pmmlNode.getPredicate() != null) {
            node.setTrait("predicate.type", pmmlNode.getPredicate().getType());
            node.setTrait("predicate.field", pmmlNode.getPredicate().getField());
            node.setTrait("predicate.operator", pmmlNode.getPredicate().getOperator());
            node.setTrait("predicate.value", pmmlNode.getPredicate().getValue());
        }
        
        // Convert child nodes
        if (pmmlNode.getNodes() != null) {
            for (PMMLNode child : pmmlNode.getNodes()) {
                TreeModel.Node childNode = convertTreeNode(child);
                if (childNode != null) {
                    node.addChild(childNode);
                }
            }
        }
        
        return node;
    }

    /**
     * Converts PMML neural network model.
     */
    public NeuralNetworkModel toNeuralNetworkModel(PMMLNeuralNetwork pmmlNN, PMMLDocument doc) {
        if (pmmlNN == null) {
            return null;
        }
        
        NeuralNetworkModel model = new NeuralNetworkModel(pmmlNN.getModelName());
        model.setTrait("pmml.function", pmmlNN.getFunctionName());
        model.setTrait("pmml.activation", pmmlNN.getActivationFunction());
        
        addDataFields(model, doc.getDataDictionary());
        addMiningSchema(model, pmmlNN.getMiningSchema());
        
        // Convert layers to a modern Sequential graph
        if (pmmlNN.getNeuralLayers() != null) {
            Sequential<Real> sequential = new Sequential<>();
            int inputSize = 0; // PMML usually defines input neurons in a separate layer or schema
            
            // Note: PMML NeuralNetwork logic mapping to Autograd is complex.
            // Simplified: Treat each PMML NeuralLayer as a Linear layer followed by activation.
            for (PMMLNeuralLayer layer : pmmlNN.getNeuralLayers()) {
                int outputSize = layer.getNumberOfNeurons();
                
                if (inputSize > 0) {
                    Linear<Real> linear = new Linear<>(inputSize, outputSize);
                    
                    // Manually set weights from PMML if possible
                    // In a production scenario, we'd map pmmlNeuron.getConnections() to weights tensor.
                    // For now, we use the build-in layer structure for the model wrapper.
                    sequential.add(linear);
                }
                
                String actName = layer.getActivationFunction() != null ? layer.getActivationFunction() : pmmlNN.getActivationFunction();
                sequential.add(new ActivationLayer<>(ActivationFunction.of(actName)));
                
                inputSize = outputSize;
            }
            
            model.addLayer(sequential);
        }
        
        return model;
    }

    /**
     * Converts PMML clustering model.
     */
    public ClusteringModel toClusteringModel(PMMLClusteringModel pmmlCluster, PMMLDocument doc) {
        if (pmmlCluster == null) {
            return null;
        }
        
        ClusteringModel model = new ClusteringModel(pmmlCluster.getModelName());
        model.setTrait("pmml.function", pmmlCluster.getFunctionName());
        model.setTrait("pmml.model.class", pmmlCluster.getModelClass());
        model.setTrait("pmml.number.of.clusters", pmmlCluster.getNumberOfClusters());
        
        addDataFields(model, doc.getDataDictionary());
        addMiningSchema(model, pmmlCluster.getMiningSchema());
        
        // Add cluster centers
        if (pmmlCluster.getClusters() != null) {
            for (PMMLCluster cluster : pmmlCluster.getClusters()) {
                ClusteringModel.Cluster c = new ClusteringModel.Cluster(cluster.getId());
                c.setName(cluster.getName());
                c.setCentroid(cluster.getArray());
                model.addCluster(c);
            }
        }
        
        return model;
    }

    /**
     * Converts to generic model for unsupported types.
     */
    private Model toGenericModel(PMMLModel pmmlModel, PMMLDocument doc) {
        Model model = new Model(pmmlModel.getModelName());
        model.setTrait("pmml.model.type", pmmlModel.getModelType());
        addDataFields(model, doc.getDataDictionary());
        return model;
    }

    private void addDataFields(Model model, PMMLDataDictionary dict) {
        if (dict != null && dict.getDataFields() != null) {
            for (PMMLDataField df : dict.getDataFields()) {
                DataField field = new DataField(df.getName(), df.getDataType(), df.getOpType());
                model.addDataField(field);
            }
        }
    }

    private void addMiningSchema(Model model, PMMLMiningSchema schema) {
        if (schema != null && schema.getMiningFields() != null) {
            for (PMMLMiningField mf : schema.getMiningFields()) {
                MiningField field = new MiningField(mf.getName(), mf.getUsageType());
                model.addMiningField(field);
            }
        }
    }
}


