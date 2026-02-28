/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology.loaders.neuroml;

import org.episteme.natural.biology.cell.Organelle;
import org.episteme.natural.biology.neuroscience.Neuron;
import org.episteme.natural.biology.neuroscience.NeuronMorphology;
import org.episteme.natural.biology.neuroscience.Synapse;
import org.episteme.natural.biology.neuroscience.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting NeuroML DTOs to core Episteme domain objects.
 * <p>
 * This bridge provides systematic conversion from NeuroML-parsed data transfer objects
 * to the core Episteme architecture, enabling deep integration with the library's
 * domain models for cells, neurons, synapses, and neural networks.
 * </p>
 * 
 * <h2>Architecture</h2>
 * <pre>
 * NeuroML XML â†’ NeuroMLReader â†’ Cell/Morphology DTOs â†’ NeuroMLBridge â†’ Core Episteme Objects
 *                                                                      â”œâ”€â”€ Neuron
 *                                                                      â”œâ”€â”€ NeuronMorphology  
 *                                                                      â”œâ”€â”€ Synapse
 *                                                                      â””â”€â”€ NeuralNetwork
 * </pre>
 * 
 * <h2>Usage Example</h2>
 * <pre>{@code
 * NeuroMLReader reader = new NeuroMLReader();
 * NeuroMLModel model = reader.read(new File("neuron.nml"));
 * 
 * // Convert to core Episteme structures
 * NeuroMLBridge bridge = new NeuroMLBridge();
 * List<Neuron> neurons = bridge.toNeurons(model);
 * NeuralNetwork network = bridge.toNeuralNetwork(model);
 * }</pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see NeuroMLReader
 * @see Neuron
 * @see NeuralNetwork
 */
public class NeuroMLBridge {

    /**
     * Converts NeuroML Cell DTOs to Episteme Neuron objects.
     * <p>
     * NeuroML cells contain morphological and biophysical data that maps
     * to the neuroscience domain model.
     * </p>
     *
     * @param model the parsed NeuroML model
     * @return list of Neuron objects
     */
    public List<Neuron> toNeurons(NeuroMLDocument model) {
        List<Neuron> neurons = new ArrayList<>();
        if (model == null || model.getCells() == null) {
            return neurons;
        }
        
        for (Cell neuromlCell : model.getCells()) {
            Neuron neuron = convertToNeuron(neuromlCell);
            if (neuron != null) {
                neurons.add(neuron);
            }
        }
        return neurons;
    }

    /**
     * Converts a single NeuroML Cell DTO to a Episteme Neuron.
     *
     * @param neuromlCell the NeuroML cell DTO
     * @return a Neuron, or null if conversion fails
     */
    public Neuron convertToNeuron(Cell neuromlCell) {
        if (neuromlCell == null) {
            return null;
        }
        
        String id = neuromlCell.getId();
        Neuron neuron = new Neuron(id != null ? id : "unknown");
        
        // Transfer NeuroML-specific attributes
        neuron.setTrait("neuroml.id", id);
        
        // Convert morphology if present
        Morphology morphology = neuromlCell.getMorphology();
        if (morphology != null) {
            NeuronMorphology nm = convertMorphology(morphology);
            neuron.setMorphology(nm);
        }
        
        // Convert biophysical properties
        BiophysicalProperties bioProps = neuromlCell.getBiophysicalProperties();
        if (bioProps != null) {
            transferBiophysicalProperties(neuron, bioProps);
        }
        
        return neuron;
    }

    /**
     * Converts NeuroML Morphology to Episteme NeuronMorphology.
     *
     * @param morphology the NeuroML morphology DTO
     * @return a NeuronMorphology object
     */
    public NeuronMorphology convertMorphology(Morphology morphology) {
        NeuronMorphology nm = new NeuronMorphology(morphology.getId());
        
        // Convert segments
        if (morphology.getSegments() != null) {
            for (Segment segment : morphology.getSegments()) {
                nm.addSegment(
                    segment.getId(),
                    segment.getName(),
                    convertPoint3D(segment.getProximal()),
                    convertPoint3D(segment.getDistal()),
                    segment.getParentId()
                );
            }
        }
        
        if (morphology.getSegmentGroups() != null) {
            for (SegmentGroup sg : morphology.getSegmentGroups()) {
                nm.addSegmentGroup(sg.getId(), sg.getMemberSegmentIds());
            }
        }
        
        return nm;
    }

    /**
     * Converts NeuroML Point3D to Episteme coordinates.
     */
    private double[] convertPoint3D(Point3D point) {
        if (point == null) {
            return new double[]{0, 0, 0, 0};
        }
        return new double[]{point.getX(), point.getY(), point.getZ(), point.getDiameter()};
    }

    /**
     * Transfers biophysical properties to the neuron.
     */
    private void transferBiophysicalProperties(Neuron neuron, BiophysicalProperties bioProps) {
        neuron.setTrait("membrane.capacitance", bioProps.getSpecificCapacitance());
        
        // Transfer ion channel densities
        List<ChannelDensity> channelDensities = bioProps.getChannelDensities();
        if (channelDensities != null) {
            for (ChannelDensity cd : channelDensities) {
                neuron.setTrait("channel." + cd.getIonChannel(), cd.getCondDensity());
            }
        }
    }

    /**
     * Converts NeuroML network to Episteme NeuralNetwork.
     *
     * @param model the parsed NeuroML model
     * @return a NeuralNetwork containing populations and projections
     */
    public NeuralNetwork toNeuralNetwork(NeuroMLDocument model) {
        if (model == null || model.getNetworks().isEmpty()) {
            return null;
        }
        
        // Use first network for simplicity in bridge
        Network network = model.getNetworks().get(0);
        NeuralNetwork nn = new NeuralNetwork(network.getId());
        
        // Build neuron lookup map
        Map<String, Neuron> neuronMap = new HashMap<>();
        for (Neuron n : toNeurons(model)) {
            neuronMap.put((String) n.getTrait("neuroml.id"), n);
        }
        
        // Add populations
        if (network.getPopulations() != null) {
            for (Population pop : network.getPopulations()) {
                String cellType = pop.getComponent();
                Neuron template = neuronMap.get(cellType);
                
                for (int i = 0; i < pop.getSize(); i++) {
                    Neuron instance = template != null ? template.copy() : new Neuron(cellType + "_" + i);
                    instance.setTrait("population", pop.getId());
                    instance.setTrait("instance.index", i);
                    nn.addNeuron(instance);
                }
            }
        }
        
        // Add projections (synaptic connections)
        if (network.getProjections() != null) {
            for (Projection proj : network.getProjections()) {
                Synapse synapse = new Synapse(proj.getSynapse());
                synapse.setTrait("source", proj.getPresynapticPopulation());
                synapse.setTrait("target", proj.getPostsynapticPopulation());
                nn.addSynapse(synapse);
            }
        }
        
        return nn;
    }

    /**
     * Converts a NeuroML Cell to a generic Episteme biology Cell.
     * <p>
     * This provides a more basic conversion when full Neuron features
     * are not needed.
     * </p>
     *
     * @param neuromlCell the NeuroML cell DTO
     * @return a biology Cell object
     */
    public org.episteme.natural.biology.cell.Cell toBiologyCell(Cell neuromlCell) {
        if (neuromlCell == null) {
            return null;
        }
        
        org.episteme.natural.biology.cell.Cell cell = new org.episteme.natural.biology.cell.Cell("Neuron");
        cell.setTrait("neuroml.id", neuromlCell.getId());
        
        // Neurons have specific organelles
        cell.addOrganelle(Organelle.NUCLEUS);
        cell.addOrganelle(Organelle.MITOCHONDRIA);
        cell.addOrganelle(Organelle.ENDOPLASMIC_RETICULUM);
        cell.addOrganelle(Organelle.GOLGI_APPARATUS);
        
        return cell;
    }
}


