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

package org.jscience.ui.viewers.biology.ecology;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.biology.services.GbifService;
import org.jscience.biology.services.GbifService.GbifSpecies;
import javafx.scene.image.Image;
import org.jscience.ui.Parameter;
import org.jscience.ui.StringParameter;
import org.jscience.ui.BooleanParameter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

/**
 * Species Browser Viewer using GBIF API.
 * Refactored to be parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpeciesBrowserViewer extends AbstractViewer {

    private String searchQuery = "Panthera leo";
    private ListView<GbifSpecies> resultList;
    private TextArea detailArea;
    private ImageView imageView;
    private ProgressIndicator progress;
    
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public SpeciesBrowserViewer() {
        setupParameters();
        initUI();
    }

    private void setupParameters() {
        parameters.add(new StringParameter("species.query", I18n.getInstance().get("species.label.species", "Search Scientific Name"), searchQuery, v -> searchQuery = v));
        parameters.add(new BooleanParameter("species.search", I18n.getInstance().get("species.btn.search", "Perform Search"), false, v -> {
            if (v) performSearch();
        }));
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        setPadding(new Insets(10));

        progress = new ProgressIndicator();
        progress.setMaxSize(20, 20);
        progress.setVisible(false);

        // Main Content
        SplitPane split = new SplitPane();
        resultList = new ListView<>();
        
        VBox details = new VBox(10);
        details.setPadding(new Insets(0, 0, 0, 10));
        detailArea = new TextArea(I18n.getInstance().get("species.prompt.search", "Search for a species to view details from GBIF..."));
        detailArea.setEditable(false);
        detailArea.setWrapText(true);
        detailArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        details.getChildren().addAll(progress, new Label(I18n.getInstance().get("species.label.taxonomy", "Taxonomy & Details:")), detailArea, imageView);

        split.getItems().addAll(resultList, details);
        split.setDividerPositions(0.35);
        setCenter(split);

        resultList.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) updateDetails(val);
        });
    }

    private void performSearch() {
        if (searchQuery.trim().isEmpty()) return;
        progress.setVisible(true);
        GbifService.getInstance().searchSpecies(searchQuery)
                .thenAccept(results -> Platform.runLater(() -> {
                    resultList.getItems().setAll(results);
                    progress.setVisible(false);
                    if (results.isEmpty()) {
                        detailArea.setText("No results found for: " + searchQuery);
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        detailArea.setText("Error: " + ex.getMessage());
                        progress.setVisible(false);
                    });
                    return null;
                });
    }

    private void updateDetails(GbifSpecies species) {
        StringBuilder sb = new StringBuilder();
        sb.append("Scientific Name: ").append(species.scientificName()).append("\n");
        sb.append("Rank: ").append(species.rank()).append("\n\n");
        sb.append("Classification:\n");
        sb.append("  Kingdom: ").append(species.kingdom()).append("\n");
        sb.append("  Phylum:  ").append(species.phylum()).append("\n");
        sb.append("  Class:   ").append(species.clazz()).append("\n");
        sb.append("  Order:   ").append(species.order()).append("\n");
        sb.append("  Family:  ").append(species.family()).append("\n");
        sb.append("  Genus:   ").append(species.genus()).append("\n");
        sb.append("\nSource: GBIF");

        detailArea.setText(sb.toString());

        imageView.setImage(null);
        GbifService.getInstance().getSpeciesMedia(species.key())
                .thenAccept(url -> {
                    if (url != null) {
                        Platform.runLater(() -> {
                            try {
                                imageView.setImage(new Image(url, true));
                            } catch (Exception e) {}
                        });
                    }
                });
    }

    @Override public String getName() { return I18n.getInstance().get("viewer.speciesbrowserviewer.name", "Species Browser"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.biology", "Biology"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.speciesbrowserviewer.desc", "Explore biological species."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.speciesbrowserviewer.longdesc", "GBIF Species Browser."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
