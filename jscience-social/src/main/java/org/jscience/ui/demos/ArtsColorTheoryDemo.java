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
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.arts");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("ArtsColorTheory.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("ArtsColorTheory.desc");
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

        // Labels for codes
        Label mainCode = new Label();
        Label compCode = new Label();
        Label ana1Code = new Label();
        Label ana2Code = new Label();
        Label tri1Code = new Label();
        Label tri2Code = new Label();

        Runnable update = () -> {
            double hue = hueSlider.getValue();

            Color c = Color.hsb(hue, 1.0, 1.0);
            colorBox.setFill(c);
            mainCode.setText(toHex(c));

            Color comp = Color.hsb((hue + 180) % 360, 1.0, 1.0);
            compBox.setFill(comp);
            compCode.setText(toHex(comp));

            Color ana1 = Color.hsb((hue + 30) % 360, 1.0, 1.0);
            ana1Box.setFill(ana1);
            ana1Code.setText(toHex(ana1));

            Color ana2 = Color.hsb((hue - 30 + 360) % 360, 1.0, 1.0);
            ana2Box.setFill(ana2);
            ana2Code.setText(toHex(ana2));

            Color tri1 = Color.hsb((hue + 120) % 360, 1.0, 1.0);
            tri1Box.setFill(tri1);
            tri1Code.setText(toHex(tri1));

            Color tri2 = Color.hsb((hue - 120 + 360) % 360, 1.0, 1.0);
            tri2Box.setFill(tri2);
            tri2Code.setText(toHex(tri2));
        };
        hueSlider.valueProperty().addListener(e -> update.run());
        update.run();

        VBox harmonies = new VBox(15);
        harmonies.setPadding(new Insets(20));
        harmonies.setAlignment(Pos.CENTER);

        harmonies.getChildren()
                .add(new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("arts.color.label.comp")));
        harmonies.getChildren().addAll(compBox, compCode);

        HBox ana = new HBox(10, new VBox(5, ana1Box, ana1Code), new VBox(5, ana2Box, ana2Code));
        ana.setAlignment(Pos.CENTER);
        harmonies.getChildren()
                .add(new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("arts.color.label.analog")));
        harmonies.getChildren().add(ana);

        HBox tri = new HBox(10, new VBox(5, tri1Box, tri1Code), new VBox(5, tri2Box, tri2Code));
        tri.setAlignment(Pos.CENTER);
        harmonies.getChildren()
                .add(new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("arts.color.label.triad")));
        harmonies.getChildren().add(tri);

        VBox main = new VBox(10, new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("arts.color.label.hue")),
                hueSlider, colorBox, mainCode);
        main.setAlignment(Pos.CENTER);

        root.setCenter(main);
        root.setRight(harmonies);

        Scene scene = new Scene(root, 600, 400);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }
}
