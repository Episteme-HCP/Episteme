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

/**
 * Species Browser Viewer using GBIF API.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpeciesBrowserViewer extends AbstractViewer {

    private TextField searchField;
    private ListView<org.jscience.biology.services.GbifService.GbifSpecies> resultList;
    private TextArea detailArea;
    private ImageView imageView;
    
    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.speciesbrowserviewer.name", "Species Browser"); }
    
    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); }

    public SpeciesBrowserViewer() {
        initUI();
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        setPadding(new Insets(10));

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 10, 0));

        searchField = new TextField("Panthera leo");
        searchField.setPrefWidth(300);

        Button searchBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("species.btn.search", "Search GBIF"));
        searchBtn.setDefaultButton(true);

        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(20, 20);
        progress.setVisible(false);

        searchBar.getChildren().addAll(new Label(org.jscience.ui.i18n.I18n.getInstance().get("species.label.species", "Species:")), searchField, searchBtn, progress);
        setTop(searchBar);

        // Main Content
        SplitPane split = new SplitPane();

        resultList = new ListView<>();

        VBox details = new VBox(10);
        details.setPadding(new Insets(0, 0, 0, 10));
        detailArea = new TextArea(org.jscience.ui.i18n.I18n.getInstance().get("species.prompt.search", "Search for a species to view details from GBIF..."));
        detailArea.setEditable(false);
        detailArea.setWrapText(true);
        detailArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        details.getChildren().addAll(new Label(org.jscience.ui.i18n.I18n.getInstance().get("species.label.taxonomy", "Taxonomy & Details:")), detailArea, imageView);

        split.getItems().addAll(resultList, details);
        split.setDividerPositions(0.35);
        setCenter(split);

        // Actions
        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                progress.setVisible(true);
                org.jscience.biology.services.GbifService.getInstance().searchSpecies(query)
                        .thenAccept(results -> javafx.application.Platform.runLater(() -> {
                            resultList.getItems().setAll(results);
                            progress.setVisible(false);
                            if (results.isEmpty()) {
                                detailArea.setText(org.jscience.ui.i18n.I18n.getInstance().get("species.msg.notfound", "No results found for: %s", query));
                            }
                        }))
                        .exceptionally(ex -> {
                            javafx.application.Platform.runLater(() -> {
                                detailArea.setText(org.jscience.ui.i18n.I18n.getInstance().get("species.error", "Error: %s", ex.getMessage()));
                                progress.setVisible(false);
                            });
                            return null;
                        });
            }
        });

        resultList.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null)
                updateDetails(val);
        });
    }

    private void updateDetails(org.jscience.biology.services.GbifService.GbifSpecies species) {
        StringBuilder sb = new StringBuilder();
        sb.append(org.jscience.ui.i18n.I18n.getInstance().get("species.detail.name", "Scientific Name: %s", species.scientificName())).append("\n");
        sb.append(org.jscience.ui.i18n.I18n.getInstance().get("species.detail.rank", "Rank: %s", species.rank())).append("\n\n");
        sb.append(org.jscience.ui.i18n.I18n.getInstance().get("species.detail.classification", "Classification:")).append("\n");
        sb.append("  Kingdom: ").append(species.kingdom()).append("\n");
        sb.append("  Phylum:  ").append(species.phylum()).append("\n");
        sb.append("  Class:   ").append(species.clazz()).append("\n");
        sb.append("  Order:   ").append(species.order()).append("\n");
        sb.append("  Family:  ").append(species.family()).append("\n");
        sb.append("  Genus:   ").append(species.genus()).append("\n");
        sb.append("\n").append(org.jscience.ui.i18n.I18n.getInstance().get("species.source", "Source: Global Biodiversity Information Facility (GBIF)"));

        detailArea.setText(sb.toString());

        // Fetch image
        imageView.setImage(null);
        org.jscience.biology.services.GbifService.getInstance().getSpeciesMedia(species.key())
                .thenAccept(url -> {
                    if (url != null) {
                        javafx.application.Platform.runLater(() -> {
                            try {
                                javafx.scene.image.Image img = new javafx.scene.image.Image(url, true);
                                imageView.setImage(img);
                            } catch (Exception e) {
                                // Silent fail for image loading
                            }
                        });
                    }
                });
    }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.speciesbrowserviewer.desc", "Explore biological species data using the Global Biodiversity Information Facility (GBIF) API."); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.speciesbrowserviewer.longdesc", "Connects to the Global Biodiversity Information Facility (GBIF) to search and retrieve detailed taxonomic classification, scientific names, and media for millions of species."); }
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
