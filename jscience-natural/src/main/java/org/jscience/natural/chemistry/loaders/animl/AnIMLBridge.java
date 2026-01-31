/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.loaders.animl;

import org.jscience.natural.chemistry.spectroscopy.Spectrum;
import org.jscience.natural.chemistry.spectroscopy.SpectralPeak;
import org.jscience.core.methodology.ScientificExperiment;
import org.jscience.core.methodology.Sample;
import org.jscience.core.methodology.Observation;

import java.util.Map;

/**
 * Bridge for converting AnIML DTOs to core JScience analytical chemistry objects.
 * <p>
 * AnIML (Analytical Information Markup Language) is the ASTM standard for
 * analytical chemistry data. This bridge converts parsed AnIML to JScience structures.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * AnIML â†’ AnIMLReader â†’ AnIML DTOs â†’ AnIMLBridge â†’ Core Objects
 *                                                   â”œâ”€â”€ ScientificExperiment
 *                                                   â”œâ”€â”€ Sample
 *                                                   â””â”€â”€ Observation (capturing Spectrum)
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLBridge {

    /**
     * Converts AnIML document to JScience ScientificExperiment.
     *
     * @param animlDoc the parsed AnIML document
     * @return an Experiment object with samples and results
     */
    public ScientificExperiment<Map<String, Object>, Object> toExperiment(AnIMLDocument animlDoc) {
        if (animlDoc == null) {
            return null;
        }
        
        ScientificExperiment<Map<String, Object>, Object> experiment = new ScientificExperiment<>(animlDoc.getName());
        experiment.setTrait("animl.version", animlDoc.getVersion());
        
        // Convert sample set
        if (animlDoc.getSampleSet() != null) {
            for (AnIMLSample animlSample : animlDoc.getSampleSet().getSamples()) {
                Sample<?> sample = convertSample(animlSample);
                if (sample != null) {
                    experiment.setTrait("sample." + sample.getName(), sample);
                }
            }
        }
        
        // Convert experiment steps
        if (animlDoc.getExperimentStepSet() != null) {
            for (AnIMLExperimentStep step : animlDoc.getExperimentStepSet().getSteps()) {
                Observation<Object> obs = convertExperimentStep(step);
                if (obs != null) {
                    experiment.record(obs);
                }
            }
        }
        
        // Add audit trail
        if (animlDoc.getAuditTrail() != null) {
            experiment.setTrait("audit.entries", animlDoc.getAuditTrail().getEntries().size());
        }
        
        return experiment;
    }

    /**
     * Converts AnIML sample to JScience Sample.
     */
    public Sample<?> convertSample(AnIMLSample animlSample) {
        if (animlSample == null) {
            return null;
        }
        
        Sample<String> sample = new Sample<>(animlSample.getName());
        sample.setTrait("animl.sample.id", animlSample.getSampleId());
        sample.setTrait("barcode", animlSample.getBarcode());
        sample.setTrait("container.type", animlSample.getContainerType());
        sample.setTrait("source.type", animlSample.getSourceType());
        
        // Add location if available
        if (animlSample.getLocation() != null) {
            sample.setPosition(animlSample.getLocation());
        }
        
        return sample;
    }

    /**
     * Converts AnIML experiment step to JScience Observation.
     */
    public Observation<Object> convertExperimentStep(AnIMLExperimentStep step) {
        if (step == null) {
            return null;
        }
        
        Object capturedValue = null;
        
        // Convert result data
        if (step.getResult() != null) {
            AnIMLResult result = step.getResult();
            
            // Check if this is spectral data
            if (result.getSeriesSet() != null && !result.getSeriesSet().isEmpty()) {
                capturedValue = convertToSpectrum(result);
            } else {
                capturedValue = result.getName(); // Fallback
            }
        } else {
            capturedValue = step.getTechniqueName();
        }
        
        Observation<Object> observation = new Observation<>(capturedValue);
        observation.setTag(step.getName());
        observation.setTrait("animl.step.id", step.getExperimentStepId());
        observation.setTrait("technique.name", step.getTechniqueName());
        
        if (step.getResult() != null && step.getResult().getParameters() != null) {
            for (AnIMLParameter param : step.getResult().getParameters()) {
                observation.setTrait(param.getName(), param.getValue());
            }
        }
        
        // Add method information
        if (step.getMethod() != null) {
            observation.setTrait("method.name", step.getMethod().getName());
            observation.setTrait("method.author", step.getMethod().getAuthor());
        }
        
        return observation;
    }

    /**
     * Converts AnIML result series to JScience Spectrum.
     */
    public Spectrum convertToSpectrum(AnIMLResult result) {
        if (result == null || result.getSeriesSet() == null) {
            return null;
        }
        
        Spectrum spectrum = new Spectrum(result.getName());
        
        for (AnIMLSeries series : result.getSeriesSet()) {
            String seriesType = series.getSeriesType();
            double[] data = series.getData();
            
            if ("independent".equalsIgnoreCase(seriesType)) {
                spectrum.setXData(data);
                spectrum.setXUnit(series.getUnit());
                spectrum.setXLabel(series.getName());
            } else if ("dependent".equalsIgnoreCase(seriesType)) {
                spectrum.setYData(data);
                spectrum.setYUnit(series.getUnit());
                spectrum.setYLabel(series.getName());
            }
        }
        
        // Extract peaks if annotated
        if (result.getPeakTable() != null) {
            for (AnIMLPeak peak : result.getPeakTable().getPeaks()) {
                SpectralPeak sp = new SpectralPeak(peak.getPosition(), peak.getIntensity());
                sp.setTrait("width", peak.getWidth());
                sp.setTrait("assignment", peak.getAssignment());
                spectrum.addPeak(sp);
            }
        }
        
        return spectrum;
    }
}


