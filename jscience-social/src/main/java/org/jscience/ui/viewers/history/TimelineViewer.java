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

package org.jscience.ui.viewers.history;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jscience.history.HistoricalEvent;
import org.jscience.history.Timeline;
import org.jscience.history.FuzzyDate;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.RealParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.Parameter;

import java.util.List;
import java.util.Optional;

/**
 * Generic Viewer for Timeline data.
 * Refactored to use Real precision for dates (Years).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimelineViewer extends BorderPane implements org.jscience.ui.Viewer {

    private Timeline timeline;
    private final Canvas canvas;
    private boolean logScale = false;
    private Real currentYearHighlight = Real.of(2025); // Default "Now"
    
    // Bounds
    private Real minYear = Real.of(-10000);
    private Real maxYear = Real.of(2050);

    public TimelineViewer() {
        this.canvas = new Canvas(800, 200);
        Pane container = new Pane(canvas);
        setCenter(container); // Pane allows resizing
        
        // Resize listener
        canvas.widthProperty().bind(container.widthProperty());
        canvas.heightProperty().bind(container.heightProperty());
        
        canvas.widthProperty().addListener(o -> draw());
        canvas.heightProperty().addListener(o -> draw());
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
        calculateBounds();
        draw();
    }
    
    public void setLogScale(boolean logScale) {
        this.logScale = logScale;
        calculateBounds();
        draw();
    }
    
    public void setCurrentYearHighlight(Real year) {
        this.currentYearHighlight = year;
        draw();
    }
    
    private void calculateBounds() {
        if (timeline == null) return;
        
        Optional<HistoricalEvent> first = timeline.getEarliestEvent();
        Optional<HistoricalEvent> last = timeline.getLatestEvent();
        
        if (first.isPresent() && last.isPresent()) {
            minYear = getYearValue(first.get().getStartDate()).subtract(Real.of(100));
            maxYear = getYearValue(last.get().getEndDate()).add(Real.of(100));
        }
    }
    
    private Real getYearValue(FuzzyDate date) {
        if (date == null || date.getYear() == null) return Real.ZERO;
        double val = date.getYear().doubleValue();
        if (date.isBce()) val = -val;
        return Real.of(val);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        
        // Background
        boolean isDark = true;
        Color bgColor = isDark ? Color.rgb(20, 20, 30) : Color.WHITE;
        gc.setFill(bgColor);
        gc.fillRect(0, 0, w, h);
        
        if (timeline == null) {
            gc.setFill(Color.GRAY);
            gc.fillText("No Timeline Data", w/2, h/2);
            return;
        }
        
        double cy = h / 2;
        
        // Axis
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(30, cy, w - 30, cy);
        
        // Draw Events
        List<HistoricalEvent> events = timeline.getEvents();
        
        // Pre-calc log parameters
        Real logMin = Real.ZERO;
        Real logMax = Real.ZERO;
        Real present = Real.of(2025);
        
        if (logScale) {
             Real oldest = minYear;
             // maxAgo = max(1, present - oldest)
             Real diff = present.subtract(oldest);
             Real maxAgo = (diff.compareTo(Real.ONE) > 0) ? diff : Real.ONE;
             
             logMax = maxAgo.log();
             logMin = Real.ONE.log(); // ln(1) = 0
        }

        for (HistoricalEvent e : events) {
            Real year = getYearValue(e.getStartDate());
            double x;
            
            if (logScale) {
                Real diff = present.subtract(year);
                Real yearsAgo = (diff.compareTo(Real.ONE) > 0) ? diff : Real.ONE;
                Real logY = yearsAgo.log();
                
                // ratio = (logY - logMin) / (logMax - logMin)
                Real div = logMax.subtract(logMin);
                Real ratio = (div.equals(Real.ZERO)) ? Real.ZERO : logY.subtract(logMin).divide(div);
                
                // x = (w - 30) - (ratio * (w - 60))
                double r = ratio.doubleValue();
                x = (w - 30) - (r * (w - 60));
            } else {
                // Linear: ratio = (year - min) / (max - min)
                Real range = maxYear.subtract(minYear);
                Real ratio = (range.equals(Real.ZERO)) ? Real.ZERO : year.subtract(minYear).divide(range);
                double r = ratio.doubleValue();
                x = 30 + r * (w - 60);
            }
            
            // Draw Event Marker
            boolean isPast = year.compareTo(currentYearHighlight) <= 0;
            
            gc.setFill(isPast ? Color.CYAN : Color.ORANGE);
            
            double size = 8;
            if (logScale) {
                 Real diff = present.subtract(year);
                 Real yearsAgo = (diff.compareTo(Real.ONE) > 0) ? diff : Real.ONE;
                 // size logic: 6 + (1 - log/logMax)*6
                 double logVal = yearsAgo.log().doubleValue();
                 double logMaxVal = logMax.doubleValue();
                 size = 6 + (1 - (logVal / (logMaxVal == 0 ? 1 : logMaxVal))) * 6;
            }
            
            gc.fillOval(x - size/2, cy - size/2, size, size);
            
            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, cy - 12);
            gc.rotate(-45);
            String label = e.getName();
            label = I18n.getInstance().get(label, label);
            gc.fillText(label, 0, 0);
            gc.restore();
        }
    }

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.history", "History");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.timeline.name", "Timeline Viewer");
    }
    
    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.timeline.desc", "Visualizes events on a timeline.");
    }

    @Override
    public void show(javafx.stage.Stage stage) {
         javafx.scene.Scene scene = new javafx.scene.Scene(this);
         org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
         stage.setTitle(getName());
         stage.setScene(scene);
         stage.show();
    }
    
    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> parameters = new java.util.ArrayList<>();
        
        Real defMin = Real.of(org.jscience.io.Configuration.getDouble("viewer.timelineviewer.default.start", -10000));
        Real defMax = Real.of(org.jscience.io.Configuration.getDouble("viewer.timelineviewer.default.end", 2050));
        
        if (minYear.equals(Real.of(-10000))) minYear = defMin;
        if (maxYear.equals(Real.of(2050))) maxYear = defMax;

        parameters.add(new RealParameter("viewer.timelineviewer.param.start",
                I18n.getInstance().get("viewer.timelineviewer.param.start.desc", "Start Year"),
                minYear.subtract(Real.of(10000)), 
                maxYear, 
                Real.of(100), 
                minYear,
                v -> {
                    this.minYear = v;
                    draw();
                }));

        parameters.add(new RealParameter("viewer.timelineviewer.param.end",
                I18n.getInstance().get("viewer.timelineviewer.param.end.desc", "End Year"),
                minYear, 
                maxYear.add(Real.of(10000)), 
                Real.of(100), 
                maxYear,
                v -> {
                    this.maxYear = v;
                    draw();
                }));
                
        parameters.add(new BooleanParameter("viewer.timelineviewer.param.log",
                I18n.getInstance().get("viewer.timelineviewer.param.log.desc", "Logarithmic Scale"),
                logScale,
                v -> {
                    this.logScale = v;
                    calculateBounds();
                    draw();
                }));
                
        return parameters;
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("viewer.timeline.longdesc", "Interactive historical chronological visualization tool. features support for BCE/CE dates, event clustering, fuzzy date handling, and dynamic scaling to explore deep history and modern events.");
    }
}

