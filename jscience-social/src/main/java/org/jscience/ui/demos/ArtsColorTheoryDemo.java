package org.jscience.ui.demos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class ArtsColorTheoryDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Arts"; // Arts section
    }

    @Override
    public String getName() {
        return org.jscience.social.i18n.I18n.getInstance().get("ArtsColorTheory.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.social.i18n.I18n.getInstance().get("ArtsColorTheory.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Main Color
        Slider hueSlider = new Slider(0, 360, 0);
        Rectangle colorBox = new Rectangle(100, 100);

        // Harmony Boxes
        Rectangle compBox = new Rectangle(50, 50); // Complementary
        Rectangle ana1Box = new Rectangle(50, 50); // Analogous +30
        Rectangle ana2Box = new Rectangle(50, 50); // Analogous -30
        Rectangle tri1Box = new Rectangle(50, 50); // Triad +120
        Rectangle tri2Box = new Rectangle(50, 50); // Triad -120

        Runnable update = () -> {
            double hue = hueSlider.getValue();
            Color c = Color.hsb(hue, 1.0, 1.0);
            colorBox.setFill(c);

            compBox.setFill(Color.hsb((hue + 180) % 360, 1.0, 1.0));

            ana1Box.setFill(Color.hsb((hue + 30) % 360, 1.0, 1.0));
            ana2Box.setFill(Color.hsb((hue - 30 + 360) % 360, 1.0, 1.0));

            tri1Box.setFill(Color.hsb((hue + 120) % 360, 1.0, 1.0));
            tri2Box.setFill(Color.hsb((hue - 120 + 360) % 360, 1.0, 1.0));
        };
        hueSlider.valueProperty().addListener(e -> update.run());
        update.run();

        VBox harmonies = new VBox(20);
        harmonies.setPadding(new Insets(20));
        harmonies.setAlignment(Pos.CENTER);

        harmonies.getChildren()
                .add(new Label(org.jscience.social.i18n.I18n.getInstance().get("arts.color.label.comp")));
        harmonies.getChildren().add(compBox);

        HBox ana = new HBox(10, ana1Box, ana2Box);
        ana.setAlignment(Pos.CENTER);
        harmonies.getChildren()
                .add(new Label(org.jscience.social.i18n.I18n.getInstance().get("arts.color.label.analog")));
        harmonies.getChildren().add(ana);

        HBox tri = new HBox(10, tri1Box, tri2Box);
        tri.setAlignment(Pos.CENTER);
        harmonies.getChildren()
                .add(new Label(org.jscience.social.i18n.I18n.getInstance().get("arts.color.label.triad")));
        harmonies.getChildren().add(tri);

        VBox main = new VBox(10, new Label(org.jscience.social.i18n.I18n.getInstance().get("arts.color.label.hue")),
                hueSlider, colorBox);
        main.setAlignment(Pos.CENTER);

        root.setCenter(main);
        root.setRight(harmonies);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
