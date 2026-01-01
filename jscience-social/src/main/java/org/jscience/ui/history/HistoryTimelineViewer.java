/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.history;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jscience.ui.i18n.SocialI18n;
import org.jscience.ui.ThemeManager;

import java.time.chrono.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced History Timeline Viewer with drag navigation and zoom.
 * Events are displayed as clickable text with encyclopedia-style details.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoryTimelineViewer extends BorderPane {

    private static final double BASE_TIMELINE_WIDTH = 5000;
    private static final double TIMELINE_HEIGHT = 350;
    private static final double START_YEAR = -3500;
    private static final double END_YEAR = 2050;

    private double zoomLevel = 1.0;
    private double timelineWidth = BASE_TIMELINE_WIDTH;
    private double scale = timelineWidth / (END_YEAR - START_YEAR);

    private ScrollPane scrollPane;
    private Pane timelineContainer;
    private ChoiceBox<CalendarType> calendarSelector;
    private VBox detailsPanel;
    private Label detailsTitleLabel;
    private TextArea detailsTextArea;

    // Mouse drag state
    private double dragStartX;
    private double scrollStartH;

    private enum CalendarType {
        GREGORIAN("hist.calendar.gregorian", IsoChronology.INSTANCE),
        ISLAMIC("hist.calendar.islamic", HijrahChronology.INSTANCE),
        THAI("hist.calendar.thai", ThaiBuddhistChronology.INSTANCE),
        JAPANESE("hist.calendar.japanese", JapaneseChronology.INSTANCE),
        MINGUO("hist.calendar.minguo", MinguoChronology.INSTANCE);

        final String name;
        final Chronology chrono;

        CalendarType(String name, Chronology chrono) {
            this.name = name;
            this.chrono = chrono;
        }

        @Override
        public String toString() {
            return SocialI18n.getInstance().get(name);
        }
    }

    private static class HistoricEvent {
        final double year;
        final String nameKey;
        final String descKey;
        final Color color;

        HistoricEvent(double year, String nameKey, String descKey, Color color) {
            this.year = year;
            this.nameKey = nameKey;
            this.descKey = descKey;
            this.color = color;
        }
    }

    private final List<HistoricEvent> events = new ArrayList<>();

    public HistoryTimelineViewer() {
        setStyle("-fx-background-color: #f5f5f5;");
        initData();
        initUI();
    }

    private void initData() {
        // Ancient
        events.add(
                new HistoricEvent(-3100, "hist.event.egypt_unified", "hist.event.egypt_unified.desc", Color.GOLDENROD));
        events.add(
                new HistoricEvent(-2560, "hist.event.pyramid_giza", "hist.event.pyramid_giza.desc", Color.DARKORANGE));
        events.add(new HistoricEvent(-1750, "hist.event.hammurabi", "hist.event.hammurabi.desc", Color.SADDLEBROWN));

        // Classical
        events.add(new HistoricEvent(-753, "hist.event.rome_founded", "hist.event.rome_founded.desc", Color.DARKRED));
        events.add(new HistoricEvent(-508, "hist.event.athens_democracy", "hist.event.athens_democracy.desc",
                Color.DARKBLUE));
        events.add(new HistoricEvent(-44, "hist.event.caesar_fall", "hist.event.caesar_fall.desc", Color.CRIMSON));
        events.add(new HistoricEvent(0, "hist.event.common_era", "hist.event.common_era.desc", Color.PURPLE));
        events.add(new HistoricEvent(476, "hist.event.rome_fall", "hist.event.rome_fall.desc", Color.DIMGRAY));

        // Medieval
        events.add(new HistoricEvent(622, "hist.event.hijra", "hist.event.hijra.desc", Color.DARKGREEN));
        events.add(new HistoricEvent(800, "hist.event.charlemagne", "hist.event.charlemagne.desc", Color.NAVY));
        events.add(new HistoricEvent(1066, "hist.event.hastings", "hist.event.hastings.desc", Color.FORESTGREEN));
        events.add(new HistoricEvent(1215, "hist.event.magna_carta", "hist.event.magna_carta.desc", Color.SLATEGRAY));
        events.add(new HistoricEvent(1453, "hist.event.constantinople_fall", "hist.event.constantinople_fall.desc",
                Color.MEDIUMPURPLE));

        // Renaissance / Modern
        events.add(new HistoricEvent(1492, "hist.event.columbus", "hist.event.columbus.desc", Color.STEELBLUE));
        events.add(new HistoricEvent(1687, "hist.event.newton_principia", "hist.event.newton_principia.desc",
                Color.DARKCYAN));
        events.add(
                new HistoricEvent(1776, "hist.event.american_indep", "hist.event.american_indep.desc", Color.CRIMSON));
        events.add(new HistoricEvent(1789, "hist.event.french_rev", "hist.event.french_rev.desc", Color.ROYALBLUE));
        events.add(new HistoricEvent(1859, "hist.event.darwin_origin", "hist.event.darwin_origin.desc", Color.SIENNA));
        events.add(new HistoricEvent(1914, "hist.event.wwi_start", "hist.event.wwi_start.desc", Color.DARKSLATEGRAY));
        events.add(new HistoricEvent(1945, "hist.event.wwii_end", "hist.event.wwii_end.desc", Color.GRAY));
        events.add(
                new HistoricEvent(1969, "hist.event.moon_landing", "hist.event.moon_landing.desc", Color.MIDNIGHTBLUE));
        events.add(new HistoricEvent(1989, "hist.event.berlin_wall", "hist.event.berlin_wall.desc", Color.DARKGRAY));
        events.add(new HistoricEvent(2025, "hist.event.present", "hist.event.present.desc", Color.SEAGREEN));
    }

    private void initUI() {
        // Toolbar
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #e8e8e8;");

        // Zoom controls
        Button zoomIn = new Button("+");
        Button zoomOut = new Button("-");
        Label zoomLabel = new Label("100%");

        zoomIn.setOnAction(e -> {
            zoomLevel = Math.min(4.0, zoomLevel * 1.25);
            updateZoom();
            zoomLabel.setText((int) (zoomLevel * 100) + "%");
        });
        zoomOut.setOnAction(e -> {
            zoomLevel = Math.max(0.25, zoomLevel / 1.25);
            updateZoom();
            zoomLabel.setText((int) (zoomLevel * 100) + "%");
        });

        calendarSelector = new ChoiceBox<>();
        calendarSelector.getItems().addAll(CalendarType.values());
        calendarSelector.setValue(CalendarType.GREGORIAN);

        Label hint = new Label(SocialI18n.getInstance().get("hist.hint.drag"));
        hint.setStyle("-fx-text-fill: #666;");

        toolbar.getChildren().addAll(
                new Label(SocialI18n.getInstance().get("hist.label.zoom")), zoomOut, zoomLabel, zoomIn,
                new Separator(),
                new Label(SocialI18n.getInstance().get("hist.label.calendar")), calendarSelector,
                new Separator(),
                hint);
        setTop(toolbar);

        // Timeline
        timelineContainer = new Pane();
        timelineContainer.setPrefSize(timelineWidth, TIMELINE_HEIGHT);
        timelineContainer.setStyle("-fx-background-color: #fafafa;");

        scrollPane = new ScrollPane(timelineContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Mouse drag navigation
        scrollPane.setCursor(Cursor.OPEN_HAND);
        scrollPane.setOnMousePressed(e -> {
            dragStartX = e.getScreenX();
            scrollStartH = scrollPane.getHvalue();
            scrollPane.setCursor(Cursor.CLOSED_HAND);
        });
        scrollPane.setOnMouseDragged(e -> {
            double deltaX = e.getScreenX() - dragStartX;
            double scrollWidth = timelineWidth - scrollPane.getViewportBounds().getWidth();
            if (scrollWidth > 0) {
                double deltaH = -deltaX / scrollWidth;
                scrollPane.setHvalue(Math.max(0, Math.min(1, scrollStartH + deltaH)));
            }
        });
        scrollPane.setOnMouseReleased(e -> scrollPane.setCursor(Cursor.OPEN_HAND));

        // Mouse wheel zoom
        scrollPane.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                zoomLevel = Math.min(4.0, zoomLevel * 1.1);
            } else {
                zoomLevel = Math.max(0.25, zoomLevel / 1.1);
            }
            updateZoom();
            zoomLabel.setText((int) (zoomLevel * 100) + "%");
            e.consume();
        });

        setCenter(scrollPane);

        // Details panel (encyclopedia-style)
        detailsPanel = new VBox(10);
        detailsPanel.setPadding(new Insets(15));
        detailsPanel.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-width: 1 0 0 0;");
        detailsPanel.setPrefHeight(150);

        detailsTitleLabel = new Label(SocialI18n.getInstance().get("hist.details.select"));
        detailsTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        detailsTextArea = new TextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setWrapText(true);
        detailsTextArea.setPrefHeight(80);
        detailsTextArea.setStyle("-fx-control-inner-background: #f9f9f9;");

        detailsPanel.getChildren().addAll(detailsTitleLabel, detailsTextArea);
        setBottom(detailsPanel);

        drawTimeline();

        // Listeners
        calendarSelector.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> drawTimeline());
    }

    private void updateZoom() {
        timelineWidth = BASE_TIMELINE_WIDTH * zoomLevel;
        scale = timelineWidth / (END_YEAR - START_YEAR);
        timelineContainer.setPrefWidth(timelineWidth);
        drawTimeline();
    }

    private void drawTimeline() {
        timelineContainer.getChildren().clear();
        CalendarType currentCalendar = calendarSelector.getValue();

        // Main axis
        Line axis = new Line(0, 220, timelineWidth, 220);
        axis.setStroke(Color.DARKGRAY);
        axis.setStrokeWidth(2);
        timelineContainer.getChildren().add(axis);

        // Year Markers
        double markerInterval = zoomLevel > 2 ? 50 : (zoomLevel > 1 ? 100 : 200);
        for (double y = START_YEAR; y <= END_YEAR; y += markerInterval) {
            double x = (y - START_YEAR) * scale;
            Line tick = new Line(x, 210, x, 230);
            tick.setStroke(Color.GRAY);

            String labelText = getFormattedYear(y, currentCalendar);
            Label lbl = new Label(labelText);
            lbl.setLayoutX(x - 20);
            lbl.setLayoutY(235);
            lbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #555;");

            timelineContainer.getChildren().addAll(tick, lbl);
        }

        // Add Eras
        addEra("hist.era.ancient", -3500, -500, Color.web("#d4c4a8", 0.4));
        addEra("hist.era.classical", -500, 500, Color.web("#d4a8a8", 0.4));
        addEra("hist.era.medieval", 500, 1450, Color.web("#a8b8d4", 0.4));
        addEra("hist.era.renaissance", 1450, 1750, Color.web("#d4a8d4", 0.4));
        addEra("hist.era.industrial", 1750, 1900, Color.web("#a8d4d4", 0.4));
        addEra("hist.era.modern", 1900, 2050, Color.web("#a8d4a8", 0.4));

        // Add Events
        boolean toggle = true;
        for (HistoricEvent ev : events) {
            addEvent(ev, toggle);
            toggle = !toggle;
        }
    }

    private void addEra(String nameKey, double startYear, double endYear, Color color) {
        double x1 = (startYear - START_YEAR) * scale;
        double x2 = (endYear - START_YEAR) * scale;

        Rectangle r = new Rectangle(x1, 40, x2 - x1, 140);
        r.setFill(color);

        Label l = new Label(SocialI18n.getInstance().get(nameKey));
        l.setLayoutX(x1 + 10);
        l.setLayoutY(50);
        l.setStyle("-fx-font-weight: bold; -fx-text-fill: #444;");

        timelineContainer.getChildren().addAll(r, l);
    }

    private void addEvent(HistoricEvent ev, boolean up) {
        double x = (ev.year - START_YEAR) * scale;
        double y = 220;
        double stemHeight = up ? -70 : 70;

        Line stem = new Line(x, y, x, y + stemHeight);
        stem.setStroke(ev.color);
        stem.setStrokeWidth(1.5);

        Circle dot = new Circle(x, y, 5, ev.color);

        // Clickable text label (not button)
        Label eventLabel = new Label(SocialI18n.getInstance().get(ev.nameKey));
        eventLabel.setLayoutX(x - 40);
        eventLabel.setLayoutY(y + stemHeight + (up ? -20 : 5));
        eventLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + toHex(ev.color) + "; -fx-cursor: hand;");
        eventLabel.setUnderline(true);

        eventLabel.setOnMouseEntered(e -> eventLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: "
                + toHex(ev.color.brighter()) + "; -fx-cursor: hand; -fx-font-weight: bold;"));
        eventLabel.setOnMouseExited(e -> eventLabel
                .setStyle("-fx-font-size: 11px; -fx-text-fill: " + toHex(ev.color) + "; -fx-cursor: hand;"));

        eventLabel.setOnMouseClicked(e -> showEventDetails(ev));

        timelineContainer.getChildren().addAll(stem, dot, eventLabel);
    }

    private void showEventDetails(HistoricEvent ev) {
        String yearStr = getFormattedYear(ev.year, calendarSelector.getValue());
        String name = SocialI18n.getInstance().get(ev.nameKey);
        String desc = SocialI18n.getInstance().get(ev.descKey);

        detailsTitleLabel.setText(yearStr + " - " + name);
        detailsTextArea.setText(desc);
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }

    private String getFormattedYear(double year, CalendarType type) {
        if (type == CalendarType.GREGORIAN) {
            if (year < 0)
                return Math.abs((int) year) + " " + SocialI18n.getInstance().get("hist.era.bce");
            return (int) year + "";
        }

        try {
            int y = (int) year;
            if (y <= 0)
                return y + ""; // Chrono conversion might fail for negative Gregorian years if not supported
            LocalDate date = LocalDate.of(y, 1, 1);
            ChronoLocalDate cDate = type.chrono.date(date);
            // Short format: "1445 AH"
            int cYear = cDate.get(java.time.temporal.ChronoField.YEAR);
            String era = "";
            if (type == CalendarType.ISLAMIC)
                era = " " + SocialI18n.getInstance().get("hist.era.ah");
            else if (type == CalendarType.THAI)
                era = " " + SocialI18n.getInstance().get("hist.era.be");
            else if (type == CalendarType.JAPANESE)
                era = " " + SocialI18n.getInstance().get("hist.era.jp");
            else if (type == CalendarType.MINGUO)
                era = " " + SocialI18n.getInstance().get("hist.era.roc");
            return cYear + era;
        } catch (Exception e) {
            return (int) year + "";
        }
    }

    public static void show(Stage stage) {
        Scene scene = new Scene(new HistoryTimelineViewer(), 1100, 600);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(SocialI18n.getInstance().get("hist.timeline.title"));
        stage.setScene(scene);
        stage.show();
    }
}


