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

package org.jscience.apps.framework;

import org.jscience.ui.i18n.I18nManager;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jscience.ui.ThemeManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared Help Manager for JScience Killer Apps.
 * Provides consistent in-app help with images and markdown-like formatting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HelpManager {

    private static HelpManager instance;

    private HelpManager() {
    }

    public static HelpManager getInstance() {
        if (instance == null) {
            instance = new HelpManager();
        }
        return instance;
    }

    /**
     * Shows a help dialog for the given topic.
     */
    public void showHelp(String appName, String title, List<HelpSection> sections) {
        Stage helpStage = new Stage();
        helpStage.initModality(Modality.APPLICATION_MODAL);
        helpStage.setTitle(I18nManager.getInstance().get("help.title") + " - " + appName);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label headerLabel = new Label(title);
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        content.getChildren().add(headerLabel);
        content.getChildren().add(new Separator());

        for (HelpSection section : sections) {
            VBox sectionBox = createSectionBox(section);
            content.getChildren().add(sectionBox);
        }

        // Close button
        Button closeBtn = new Button(I18nManager.getInstance().get("action.close"));
        closeBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 20;");
        closeBtn.setOnAction(e -> helpStage.close());

        HBox buttonBox = new HBox(closeBtn);
        buttonBox.setStyle("-fx-alignment: center-right;");
        content.getChildren().add(buttonBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(scrollPane, 600, 500);
        ThemeManager.getInstance().applyTheme(scene);
        helpStage.setScene(scene);
        helpStage.show();
    }

    private VBox createSectionBox(HelpSection section) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(10, 0, 10, 0));

        Label sectionTitle = new Label("Ã°Å¸â€œÅ’ " + section.title);
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        box.getChildren().add(sectionTitle);

        Label description = new Label(section.description);
        description.setWrapText(true);
        description.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");
        box.getChildren().add(description);

        // Add image if provided
        if (section.imagePath != null) {
            try {
                InputStream is = getClass().getResourceAsStream(section.imagePath);
                if (is != null) {
                    Image img = new Image(is);
                    ImageView iv = new ImageView(img);
                    iv.setFitWidth(400);
                    iv.setPreserveRatio(true);
                    iv.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
                    box.getChildren().add(iv);
                }
            } catch (Exception e) {
                // Image not found, skip
            }
        }

        // Add tip if provided
        if (section.tip != null) {
            Label tipLabel = new Label(I18nManager.getInstance().get("help.tip") + " " + section.tip);
            tipLabel.setWrapText(true);
            tipLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #27ae60; -fx-font-style: italic;");
            box.getChildren().add(tipLabel);
        }

        return box;
    }

    /**
     * Quick help dialog with just text.
     */
    public void showQuickHelp(String title, String content) {
        List<HelpSection> sections = new ArrayList<>();
        sections.add(new HelpSection(I18nManager.getInstance().get("help.overview"), content));
        showHelp("JScience", title, sections);
    }

    /**
     * Help section data class.
     */
    public static class HelpSection {
        public String title;
        public String description;
        public String imagePath;
        public String tip;

        public HelpSection(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public HelpSection withImage(String path) {
            this.imagePath = path;
            return this;
        }

        public HelpSection withTip(String tip) {
            this.tip = tip;
            return this;
        }
    }
}
