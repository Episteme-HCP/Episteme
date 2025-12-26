package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class HistoryTimelineDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.social.i18n.I18n.getInstance().get("category.history");
    }

    @Override
    public String getName() {
        return org.jscience.social.i18n.I18n.getInstance().get("hist.timeline.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.social.i18n.I18n.getInstance().get("hist.timeline.desc");
    }

    @Override
    public void show(Stage stage) {
        ScrollPane scroll = new ScrollPane();
        Pane content = new Pane();
        content.setPrefHeight(400);
        content.setPrefWidth(2000); // Long timeline

        // Draw Axis
        Line axis = new Line(50, 200, 1950, 200);
        axis.setStroke(Color.BLACK);
        axis.setStrokeWidth(2);
        content.getChildren().add(axis);

        // Add Eras (Rectangles behind)
        addEra(content, "Ancient Era", 50, 400, Color.LIGHTYELLOW);
        addEra(content, "Classical Era", 400, 700, Color.LIGHTGOLDENRODYELLOW);
        addEra(content, "Medieval Era", 700, 1000, Color.LIGHTGRAY);
        addEra(content, "Renaissance", 1000, 1300, Color.LAVENDER);
        addEra(content, "Industrial", 1300, 1600, Color.LIGHTSTEELBLUE);
        addEra(content, "Modern Era", 1600, 1950, Color.LIGHTCYAN);

        // Add Events
        addEvent(content, "Event A", 100, 200, true);
        addEvent(content, "Event B", 350, 200, false);
        addEvent(content, "Empire Rise", 500, 200, true);
        addEvent(content, "Empire Fall", 650, 200, false);
        addEvent(content, "Discovery X", 1100, 200, true);
        addEvent(content, "Revolution Y", 1450, 200, false);
        addEvent(content, "War Z", 1700, 200, true);
        addEvent(content, "Internet", 1900, 200, false);

        scroll.setContent(content);

        Scene scene = new Scene(scroll, 800, 450);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void addEra(Pane root, String name, double startX, double endX, Color c) {
        Rectangle rect = new Rectangle(startX, 50, endX - startX, 300);
        rect.setFill(c);
        rect.setOpacity(0.5);
        Label lbl = new Label(name);
        lbl.setLayoutX(startX + 10);
        lbl.setLayoutY(60);
        lbl.setStyle("-fx-font-weight: bold;");
        root.getChildren().addAll(rect, lbl);
    }

    private void addEvent(Pane root, String name, double x, double y, boolean up) {
        Circle point = new Circle(x, y, 5, Color.RED);
        double labelY = up ? y - 40 : y + 40;
        Line stem = new Line(x, y, x, labelY);
        Label lbl = new Label(name);
        lbl.setLayoutX(x - 20);
        lbl.setLayoutY(up ? labelY - 20 : labelY);
        lbl.setStyle("-fx-background-color: white; -fx-padding: 3; -fx-border-color: black; -fx-border-width: 1;");

        root.getChildren().addAll(stem, point, lbl);
    }
}
