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

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jscience.ui.i18n.I18nManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Standardized Help Dialog for Featured Apps.
 * Supports displaying text and images for Documentation and Tutorials.
 */
public class HelpDialog extends Dialog<Void> {

    private final BorderPane content;
    private final TreeView<String> navigation;
    private final ScrollPane detailPane;
    private final Map<String, HelpTopic> topics = new HashMap<>();

    public HelpDialog(String title) {
        setTitle(title);
        setHeaderText(null);

        ButtonType closeButtonType = new ButtonType(I18nManager.getInstance().get("dialog.close"),
                ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(closeButtonType);

        content = new BorderPane();
        content.setPrefSize(800, 600);

        // Navigation Tree
        TreeItem<String> rootItem = new TreeItem<>("Topics");
        rootItem.setExpanded(true);
        navigation = new TreeView<>(rootItem);
        navigation.setShowRoot(false);
        navigation.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showTopic(newVal.getValue());
            }
        });

        content.setLeft(navigation);

        // Detail View
        detailPane = new ScrollPane();
        detailPane.setFitToWidth(true);
        detailPane.setPadding(new Insets(20));
        content.setCenter(detailPane);

        getDialogPane().setContent(content);
    }

    public void addTopic(String category, String title, String contentText, String imagePath) {
        HelpTopic topic = new HelpTopic(title, contentText, imagePath);
        topics.put(title, topic);

        TreeItem<String> categoryItem = findOrCreateCategory(category);
        categoryItem.getChildren().add(new TreeItem<>(title));
    }

    private TreeItem<String> findOrCreateCategory(String category) {
        for (TreeItem<String> item : navigation.getRoot().getChildren()) {
            if (item.getValue().equals(category)) {
                return item;
            }
        }
        TreeItem<String> newItem = new TreeItem<>(category);
        newItem.setExpanded(true);
        navigation.getRoot().getChildren().add(newItem);
        return newItem;
    }

    private void showTopic(String title) {
        HelpTopic topic = topics.get(title);
        if (topic == null) {
            detailPane.setContent(new Label(org.jscience.ui.i18n.I18n.getInstance().get("auto.helpdialog.select_a_topic_to_view_details", "Select a topic to view details.")));
            return;
        }

        VBox box = new VBox(15);

        Label header = new Label(topic.title);
        header.getStyleClass().add("font-bold"); // Replaced inline style: -fx-font-size: 24px; -fx-font-weight: bold;
        box.getChildren().add(header);

        if (topic.imagePath != null && !topic.imagePath.isEmpty()) {
            try {
                // Try to load from classpath
                InputStream is = getClass().getResourceAsStream(topic.imagePath);
                if (is != null) {
                    Image img = new Image(is);
                    ImageView iv = new ImageView(img);
                    iv.setPreserveRatio(true);
                    iv.setFitWidth(500);
                    box.getChildren().add(iv);
                } else {
                    // Placeholder if missing
                    Label imgPlace = new Label("[Image: " + topic.imagePath + "]");
                    imgPlace.setStyle("-fx-border-color: gray; -fx-padding: 20; -fx-alignment: center;");
                    imgPlace.setPrefSize(500, 200);
                    box.getChildren().add(imgPlace);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Text text = new Text(topic.content);
        text.wrappingWidthProperty().bind(detailPane.widthProperty().subtract(40));
        TextFlow flow = new TextFlow(text);
        box.getChildren().add(flow);

        detailPane.setContent(box);
    }

    private static record HelpTopic(String title, String content, String imagePath) {
    }
}
