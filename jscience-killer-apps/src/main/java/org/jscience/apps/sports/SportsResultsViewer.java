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

package org.jscience.apps.sports;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.apps.framework.I18nManager;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.timeseries.TimeSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Sports Results Management System using JScience TimeSeries and Real.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SportsResultsViewer extends Application {

    private final I18nManager i18n = I18nManager.getInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<String> matchHistory = FXCollections.observableArrayList();

    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // --- Header ---
        Label headerVal = new Label(i18n.get("sports.header"));
        headerVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox header = new HBox(headerVal);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        root.setTop(header);

        // --- Center: League Table ---
        TableView<Team> table = new TableView<>();
        table.setItems(teams);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Team, Integer> rankCol = new TableColumn<>("#");
        rankCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        rankCol.setPrefWidth(40);

        TableColumn<Team, String> nameCol = new TableColumn<>(i18n.get("sports.col.team"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Team, Integer> playedCol = new TableColumn<>(i18n.get("sports.col.played"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));

        TableColumn<Team, Number> pointsCol = new TableColumn<>(i18n.get("sports.col.pts"));
        pointsCol.setCellValueFactory(cell -> cell.getValue().points);
        pointsCol.setStyle("-fx-font-weight: bold;");

        TableColumn<Team, String> avgCol = new TableColumn<>("Trend (SMA)");
        avgCol.setCellValueFactory(cell -> cell.getValue().trend);

        table.getColumns().addAll(rankCol, nameCol, playedCol, pointsCol, avgCol);

        VBox centerBox = new VBox(10, new Label(i18n.get("sports.label.standings")), table);
        centerBox.setPadding(new Insets(20));
        root.setCenter(centerBox);

        // --- Right: Controls ---
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(350);

        ComboBox<Team> homeBox = new ComboBox<>(teams);
        homeBox.setPromptText(i18n.get("sports.form.home"));
        homeBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Team> awayBox = new ComboBox<>(teams);
        awayBox.setPromptText(i18n.get("sports.form.away"));
        awayBox.setMaxWidth(Double.MAX_VALUE);

        TextField hScore = new TextField();
        hScore.setPromptText("Home");
        hScore.setPrefWidth(50);
        TextField aScore = new TextField();
        aScore.setPromptText("Away");
        aScore.setPrefWidth(50);
        HBox scoreBox = new HBox(10, hScore, new Label("-"), aScore);
        scoreBox.setAlignment(Pos.CENTER);

        Button addBtn = new Button(i18n.get("sports.button.add"));
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> {
            try {
                processMatch(homeBox.getValue(), awayBox.getValue(),
                        Integer.parseInt(hScore.getText()), Integer.parseInt(aScore.getText()));
                hScore.clear();
                aScore.clear();
            } catch (Exception ex) {
            }
        });

        Button simBtn = new Button(i18n.get("sports.button.simulate"));
        simBtn.setMaxWidth(Double.MAX_VALUE);
        simBtn.setOnAction(e -> simulateSeason());

        ListView<String> historyView = new ListView<>(matchHistory);
        historyView.setPrefHeight(300);

        rightPanel.getChildren().addAll(new Label(i18n.get("sports.form.title")), homeBox, awayBox, scoreBox, addBtn,
                simBtn, new Separator(), historyView);
        root.setRight(rightPanel);

        initTeams();

        Scene scene = new Scene(root, 1100, 750);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(i18n.get("sports.title"));
        stage.setScene(scene);
        stage.show();
    }

    private void initTeams() {
        teams.addAll(new Team("Man City"), new Team("Arsenal"), new Team("Liverpool"), new Team("Aston Villa"),
                new Team("Tottenham"));
    }

    private void processMatch(Team home, Team away, int hs, int as) {
        if (home == null || away == null || home == away)
            return;
        matchHistory.add(0, home.name.get() + " " + hs + " - " + as + " " + away.name.get());

        home.addResult(hs, as);
        away.addResult(as, hs);

        FXCollections.sort(teams, (t1, t2) -> t2.points.get() - t1.points.get());
    }

    private void simulateSeason() {
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < 5; i++) {
            processMatch(teams.get(r.nextInt(teams.size())), teams.get(r.nextInt(teams.size())), r.nextInt(4),
                    r.nextInt(4));
        }
    }

    public static class Team {
        public final SimpleStringProperty name;
        public final SimpleIntegerProperty played = new SimpleIntegerProperty(0);
        public final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        public final SimpleStringProperty trend = new SimpleStringProperty("--");
        private final List<Real> pointsHistory = new ArrayList<>();

        public Team(String name) {
            this.name = new SimpleStringProperty(name);
            pointsHistory.add(Real.ZERO);
        }

        public void addResult(int gf, int ga) {
            played.set(played.get() + 1);
            int p = (gf > ga) ? 3 : (gf == ga ? 1 : 0);
            points.set(points.get() + p);
            pointsHistory.add(Real.of(points.get()));
            updateTrend();
        }

        private void updateTrend() {
            if (pointsHistory.size() < 3)
                return;
            Real[] data = pointsHistory.toArray(new Real[0]);
            Real[] sma = TimeSeries.movingAverage(data, Math.min(data.length, 3));
            if (sma.length > 0) {
                trend.set(String.format("%.1f", sma[sma.length - 1].doubleValue()));
            }
        }

        public String getName() {
            return name.get();
        }

        public int getPlayed() {
            return played.get();
        }

        public int getPoints() {
            return points.get();
        }
    }

    public static void show(Stage stage) {
        new SportsResultsViewer().start(stage);
    }
}
