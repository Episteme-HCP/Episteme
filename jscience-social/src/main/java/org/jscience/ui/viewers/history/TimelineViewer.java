
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
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Generic Viewer for Timeline data.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimelineViewer extends BorderPane implements org.jscience.ui.Viewer {

    private Timeline timeline;
    private final Canvas canvas;
    private boolean logScale = false;
    private double currentYearHighlight = 2025; // Default "Now"
    
    // Bounds
    private double minYear = -10000;
    private double maxYear = 2050;

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
        // Adjust bounds for log?
        calculateBounds();
        draw();
    }
    
    public void setCurrentYearHighlight(double year) {
        this.currentYearHighlight = year;
        draw();
    }
    
    private void calculateBounds() {
        if (timeline == null) return;
        
        Optional<HistoricalEvent> first = timeline.getEarliestEvent();
        Optional<HistoricalEvent> last = timeline.getLatestEvent();
        
        if (first.isPresent() && last.isPresent()) {
            minYear = getYearValue(first.get().getStartDate()) - 100;
            maxYear = getYearValue(last.get().getEndDate()) + 100;
            
            // Fix min for log scale (cannot be <= 0 relative to "now" in years ago?)
            // Kurzweil used Years Ago. Standard log timeline usually means Log(Time).
            // But History is roughly linear.
            // Kurzweil demo specifically did "Log Years Ago".
            // If this is a generic Viewer, Log Scale typically implies Log Time? 
            // Or maybe "Time since Big Bang"?
            // If I stick to Kurzweil's logic "Log Years Ago", it's specific to "Backward looking".
            // But generic Log Scale for history usually means compressing ancient history.
            // I'll implement Log Scale as "SymLog" or simply "Log Years Ago" if logScale is true, 
            // assuming the intent is to show ancient event density.
            // For now, I'll stick to a standard Linear mapping, and if logScale is true, use a Log mapping of (Present - Year).
        }
    }
    
    private double getYearValue(FuzzyDate date) {
        if (date == null || date.getYear() == null) return 0;
        return date.isBce() ? -date.getYear() : date.getYear();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        
        // Background
        boolean isDark = true; // Hardcode dark for now or check ThemeManager
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
        
        // Pre-calc log parameters if needed
        double logMin = 0;
        double logMax = 0;
        double present = 2025; // Reference frame
        
        if (logScale) {
             // Logic from KurzweilDemo: Log(Years Ago)
             // Find max years ago
             double oldest = minYear;
             double maxAgo = Math.max(1, present - oldest);
             logMax = Math.log(maxAgo);
             logMin = Math.log(1); // 1 year ago
        }

        for (HistoricalEvent e : events) {
            double year = getYearValue(e.getStartDate());
            double x;
            
            if (logScale) {
                double yearsAgo = Math.max(1, present - year);
                double logY = Math.log(yearsAgo);
                // Map logY [logMin, logMax] to x [w-30, 30] (Recently is on right)
                // logMax (oldest) -> Left (30)
                // logMin (recent) -> Right (w-30)
                double ratio = (logY - logMin) / (logMax - logMin); 
                x = (w - 30) - (ratio * (w - 60));
            } else {
                // Linear: minYear -> 30, maxYear -> w-30
                double ratio = (year - minYear) / (maxYear - minYear);
                x = 30 + ratio * (w - 60);
            }
            
            // Draw Event Marker
            // Color logic: Current vs Past
            boolean isPast = year <= currentYearHighlight;
            
            if (logScale) {
                 // Kurzweil colors
                 double yearsAgo = present - year;
                 gc.setFill(yearsAgo < 50 ? Color.YELLOW : (yearsAgo < 150 ? Color.ORANGE : Color.DARKORANGE));
            } else {
                 gc.setFill(year <= 2025 ? Color.CYAN : Color.ORANGE);
            }
            
            double size = logScale ? (6 + (1 - (Math.log(Math.max(1, present-year)) / logMax)) * 6) : 8;
            gc.fillOval(x - size/2, cy - size/2, size, size);
            
            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, cy - 12);
            gc.rotate(-45);
            // Translate I18n label if possible, here we assume event name is key or label
            // HistoricalEvent name might be key
            String label = e.getName();
            // Try I18n?
            if (label.startsWith("kurzweil.")) {
                 // It's a key
                 // Try looking it up via SocialI18n (passed via context? or generic I18n)
                 // Generic I18n might not have Social keys if not loaded?
                 // But I18n loads "messages_social" by default in my I18n class! (checked earlier)
                 // So I18n.getInstance().get(label) should work.
                 label = I18n.getInstance().get(label);
            }
            
            gc.fillText(label, 0, 0);
            gc.restore();
        }
        
        // Axis Labels (Simplified)
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("System", 10));
        // ... (Implement axis ticks if time permits, skipping for brevity to focus on data)
    }

    @Override
    public String getCategory() {
        return "History";
    }

    @Override
    public String getName() {
        return "Timeline Viewer";
    }
    
    @Override
    public String getDescription() {
        return "Visualizes events on a timeline.";
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
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return java.util.Collections.emptyList();
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
