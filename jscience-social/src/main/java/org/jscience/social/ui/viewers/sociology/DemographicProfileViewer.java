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

package org.jscience.social.ui.viewers.sociology;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.social.sociology.DemographicData;

import java.util.List;

/**
 * Visualizes population structures using demographic pyramids.
 * Supports comparison and historical evolution.
 */
public final class DemographicProfileViewer extends AbstractViewer {

    private final BarChart<Number, String> chart;
    private final XYChart.Series<Number, String> maleSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, String> femaleSeries = new XYChart.Series<>();

    public DemographicProfileViewer() {
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        
        xAxis.setLabel(I18N.getInstance().get("viewer.demography.axis.population", "Population Count"));
        yAxis.setLabel(I18N.getInstance().get("viewer.demography.axis.age", "Age Group"));

        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(I18N.getInstance().get("viewer.demography.title", "Population Pyramid"));
        
        maleSeries.setName(I18N.getInstance().get("viewer.demography.male", "Male"));
        femaleSeries.setName(I18N.getInstance().get("viewer.demography.female", "Female"));
        
        chart.getData().add(maleSeries);
        chart.getData().add(femaleSeries);
        
        setCenter(chart);
        getStyleClass().add("demographic-profile-viewer");
    }

    public void setDemographicData(DemographicData data) {
        maleSeries.getData().clear();
        femaleSeries.getData().clear();
        
        List<DemographicData.AgeGroup> groups = data.getConsolidatedGroups(5);
        for (var group : groups) {
            String label = group.minAge() + "-" + group.maxAge();
            // Male values are negative to show on the left in a pyramid
            maleSeries.getData().add(new XYChart.Data<>(-group.maleCount(), label));
            femaleSeries.getData().add(new XYChart.Data<>(group.femaleCount(), label));
        }
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.social", "Social Sciences"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.demography.name", "Demographic Profile Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.demography.desc", "Visualizes population pyramids and demographic structures."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.demography.longdesc", "Advanced demographic analysis tool. Comparative visualization of age and gender distribution across populations."); 
    }
}

