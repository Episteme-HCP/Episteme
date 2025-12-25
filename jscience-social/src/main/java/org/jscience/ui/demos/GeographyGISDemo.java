package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class GeographyGISDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getName() {
        return org.jscience.social.i18n.I18n.getInstance().get("geo.gis.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.social.i18n.I18n.getInstance().get("geo.gis.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        CheckBox terrainChk = new CheckBox(org.jscience.social.i18n.I18n.getInstance().get("geo.gis.check.terrain"));
        terrainChk.setSelected(true);
        CheckBox roadsChk = new CheckBox(org.jscience.social.i18n.I18n.getInstance().get("geo.gis.check.roads"));
        roadsChk.setSelected(true);
        CheckBox popChk = new CheckBox(org.jscience.social.i18n.I18n.getInstance().get("geo.gis.check.pop"));
        popChk.setSelected(false);

        Runnable draw = () -> renderMap(canvas.getGraphicsContext2D(), 800, 600,
                terrainChk.isSelected(), roadsChk.isSelected(), popChk.isSelected());

        terrainChk.setOnAction(e -> draw.run());
        roadsChk.setOnAction(e -> draw.run());
        popChk.setOnAction(e -> draw.run());

        VBox controls = new VBox(10, new Label(org.jscience.social.i18n.I18n.getInstance().get("geo.gis.label.layers")),
                terrainChk, roadsChk, popChk);
        controls.setStyle("-fx-padding: 10; -fx-background-color: #eee;");
        root.setRight(controls);

        draw.run();

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void renderMap(GraphicsContext gc, double w, double h, boolean terrain, boolean roads, boolean pop) {
        gc.clearRect(0, 0, w, h);

        // 1. Terrain Layer (Procedural Noise approximation)
        if (terrain) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(0, 0, w, h);

            gc.setFill(Color.LIGHTGREEN);
            // Draw a "continent"
            gc.fillOval(100, 100, 600, 400);
            gc.fillOval(500, 50, 200, 200);

            gc.setFill(Color.FORESTGREEN);
            // Some forests
            gc.fillOval(200, 200, 100, 100);
        }

        // 2. Roads Layer
        if (roads) {
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(3);
            gc.strokeLine(150, 150, 650, 450); // Hwy 1
            gc.strokeLine(650, 150, 150, 450); // Hwy 2

            gc.setLineWidth(1);
            gc.strokeLine(300, 150, 300, 450);
            gc.strokeLine(150, 300, 650, 300);
        }

        // 3. Population Layer (Heatmap blobs)
        if (pop) {
            gc.setGlobalAlpha(0.5);
            gc.setFill(Color.RED);
            // Major Cities
            gc.fillOval(380, 280, 40, 40); // Center City
            gc.fillOval(130, 130, 30, 30);
            gc.fillOval(630, 430, 30, 30);
            gc.setGlobalAlpha(1.0);
        }
    }
}
