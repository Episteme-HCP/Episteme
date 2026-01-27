/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.animl;

import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.spectroscopy.Spectrum;
import org.jscience.chemistry.spectroscopy.SpectralPeak;
import org.jscience.chemistry.experiment.Experiment;
import org.jscience.chemistry.experiment.Sample;
import org.jscience.chemistry.experiment.Measurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting AnIML DTOs to core JScience analytical chemistry objects.
 * <p>
 * AnIML (Analytical Information Markup Language) is the ASTM standard for
 * analytical chemistry data. This bridge converts parsed AnIML to JScience structures.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * AnIML → AnIMLReader → AnIML DTOs → AnIMLBridge → Core Objects
 *                                                   ├── Experiment
 *                                                   ├── Sample
 *                                                   ├── Measurement
 *                                                   └── Spectrum
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLBridge {

    /**
     * Converts AnIML document to JScience Experiment.
     *
     * @param animlDoc the parsed AnIML document
     * @return an Experiment object with samples and results
     */
    public Experiment toExperiment(AnIMLDocument animlDoc) {
        if (animlDoc == null) {
            return null;
        }
        
        Experiment experiment = new Experiment(animlDoc.getName());
        experiment.setTrait("animl.version", animlDoc.getVersion());
        
        // Convert sample set
        if (animlDoc.getSampleSet() != null) {
            for (AnIMLSample animlSample : animlDoc.getSampleSet().getSamples()) {
                Sample sample = convertSample(animlSample);
                if (sample != null) {
                    experiment.addSample(sample);
                }
            }
        }
        
        // Convert experiment steps
        if (animlDoc.getExperimentStepSet() != null) {
            for (AnIMLExperimentStep step : animlDoc.getExperimentStepSet().getSteps()) {
                Measurement measurement = convertExperimentStep(step);
                if (measurement != null) {
                    experiment.addMeasurement(measurement);
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
    public Sample convertSample(AnIMLSample animlSample) {
        if (animlSample == null) {
            return null;
        }
        
        Sample sample = new Sample(animlSample.getName());
        sample.setTrait("animl.sample.id", animlSample.getSampleId());
        sample.setTrait("barcode", animlSample.getBarcode());
        sample.setTrait("container.type", animlSample.getContainerType());
        sample.setTrait("source.type", animlSample.getSourceType());
        
        // Add location if available
        if (animlSample.getLocation() != null) {
            sample.setTrait("location", animlSample.getLocation());
        }
        
        return sample;
    }

    /**
     * Converts AnIML experiment step to JScience Measurement.
     */
    public Measurement convertExperimentStep(AnIMLExperimentStep step) {
        if (step == null) {
            return null;
        }
        
        Measurement measurement = new Measurement(step.getName());
        measurement.setTrait("animl.step.id", step.getExperimentStepId());
        measurement.setTrait("technique.name", step.getTechniqueName());
        
        // Convert result data
        if (step.getResult() != null) {
            AnIMLResult result = step.getResult();
            
            // Check if this is spectral data
            if (result.getSeriesSet() != null && !result.getSeriesSet().isEmpty()) {
                Spectrum spectrum = convertToSpectrum(result);
                measurement.setSpectrum(spectrum);
            }
            
            // Add scalar results
            if (result.getParameters() != null) {
                for (AnIMLParameter param : result.getParameters()) {
                    measurement.setTrait(param.getName(), param.getValue());
                }
            }
        }
        
        // Add method information
        if (step.getMethod() != null) {
            measurement.setTrait("method.name", step.getMethod().getName());
            measurement.setTrait("method.author", step.getMethod().getAuthor());
        }
        
        return measurement;
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
